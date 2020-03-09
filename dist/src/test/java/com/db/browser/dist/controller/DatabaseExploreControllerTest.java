package com.db.browser.dist.controller;

import com.db.browser.dist.CoreApplication;
import com.db.browser.dist.entity.Registration;
import com.db.browser.dist.entity.RegistrationsRepository;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class DatabaseExploreControllerTest {

    public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres");
    private Integer id;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RegistrationsRepository repository;
    private ObjectMapper mapper = new ObjectMapper();


    @BeforeClass
    @SneakyThrows
    public static void beforeClass() {
        postgresContainer.addExposedPort(5432);
        postgresContainer.start();

        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            conn.createStatement().executeUpdate("create schema test_app");
            conn.createStatement().executeUpdate("create table test_app.test_table (\n" +
                    "    id          integer constraint firstkey primary key,\n" +
                    "    title       varchar(40) not null,\n" +
                    "    score       integer not null,\n" +
                    "    live_date   date,\n" +
                    "    len         interval hour to minute\n" +
                    ")");
            conn.createStatement().executeUpdate("insert into test_app.test_table(id, title, score, live_date, len) values (1, 'One', 10, '1994-09-18', '2h 50m')");
            conn.createStatement().executeUpdate("insert into test_app.test_table(id, title, score, live_date, len) values (2, 'Two', 100, '1994-09-20', '4h 50m')");
            conn.createStatement().executeUpdate("insert into test_app.test_table(id, title, score, live_date, len) values (3, 'Three', 20, '1994-09-20', '4h 50m')");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @AfterClass
    public static void afterClass() {
        postgresContainer.stop();
    }

    @Before
    public void before() {
        Registration registration = new Registration();
        registration.setName("test");
        registration.setPort(postgresContainer.getMappedPort(5432));
        registration.setUsername(postgresContainer.getUsername());
        registration.setPassword(postgresContainer.getPassword());
        registration.setHostname(postgresContainer.getContainerIpAddress());
        registration.setDatabaseName(postgresContainer.getDatabaseName());
        repository.save(registration);
        id = repository.save(registration).getId();
    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void test_list_schemas() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[6].catalogName").value("test"))
                .andExpect(jsonPath("$[6].schemaName").value("test_app"))
                .andExpect(jsonPath("$[6].schemaOwner").value("test"));

    }

    @Test
    public void test_list_tables() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas/test_app/tables")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].tableCatalog").value("test"))
                .andExpect(jsonPath("$[0].tableSchema").value("test_app"))
                .andExpect(jsonPath("$[0].tableName").value("test_table"))
                .andExpect(jsonPath("$[0].isInsertableInto").value("YES"))
                .andExpect(jsonPath("$[0].isTyped").value("NO"));
    }

    @Test
    public void test_list_column_metadata() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas/test_app/tables/test_table/columns")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].isNullable").value("NO"))
                .andExpect(jsonPath("$[0].columnName").value("id"))
                .andExpect(jsonPath("$[0].isUpdatable").value("YES"))
                .andExpect(jsonPath("$[0].isIdentity").value("NO"))
                .andExpect(jsonPath("$[0].isGenerated").value("NEVER"))
                .andExpect(jsonPath("$[0].dataType").value("integer"))
                .andExpect(jsonPath("$[0].udtName").value("int4"));
    }

    @Test
    public void test_list_column_data() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas/test_app/tables/test_table/columns/preview")
                )
                .andExpect(status().isOk())
                .andDo(rs -> {
                    String s = rs.getResponse().getContentAsString();
                    System.out.println();
                })
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].rowId").value(0))
                .andExpect(jsonPath("$[1].rowId").value(1))
                .andExpect(jsonPath("$[2].rowId").value(2))

                .andExpect(jsonPath("$[0].content.score").value(10))
                .andExpect(jsonPath("$[1].content.score").value(100))
                .andExpect(jsonPath("$[2].content.score").value(20))

                .andExpect(jsonPath("$[0].content.len").value("02:50:00"))
                .andExpect(jsonPath("$[1].content.len").value("04:50:00"))
                .andExpect(jsonPath("$[2].content.len").value("04:50:00"))

                .andExpect(jsonPath("$[0].content.id").value(1))
                .andExpect(jsonPath("$[1].content.id").value(2))
                .andExpect(jsonPath("$[2].content.id").value(3))

                .andExpect(jsonPath("$[0].content.title").value("One"))
                .andExpect(jsonPath("$[1].content.title").value("Two"))
                .andExpect(jsonPath("$[2].content.title").value("Three"))

                .andExpect(jsonPath("$[0].content.live_date").value("1994-09-18"))
                .andExpect(jsonPath("$[1].content.live_date").value("1994-09-20"))
                .andExpect(jsonPath("$[2].content.live_date").value("1994-09-20"));
    }
}


