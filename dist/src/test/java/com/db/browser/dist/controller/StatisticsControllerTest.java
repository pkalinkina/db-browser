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
public class StatisticsControllerTest {
    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres");
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
            conn.close();
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
        id = repository.save(registration).getId();
    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void test_table_statistics() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas/test_app/tables/test_table/statistics")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.numberOfRecords").value(3))
                .andExpect(jsonPath("$.numberOfAttributes").value(5));

    }

    @Test
    public void test_columns_statistics() throws Exception {
        mvc
                .perform(
                        get("/" + id + "/schemas/test_app/tables/test_table/columns/statistics")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].name").value("id"))
                .andExpect(jsonPath("$[0].min").value(1.0))
                .andExpect(jsonPath("$[0].max").value(3.0))
                .andExpect(jsonPath("$[0].avg").value(2.0))
                .andExpect(jsonPath("$[0].median").value(2.0))

                .andExpect(jsonPath("$[1].name").value("score"))
                .andExpect(jsonPath("$[1].min").value(10.0))
                .andExpect(jsonPath("$[1].max").value(100.0))
                .andExpect(jsonPath("$[1].avg").value(43.333333333333336))
                .andExpect(jsonPath("$[1].median").value(20.0));

    }
}
