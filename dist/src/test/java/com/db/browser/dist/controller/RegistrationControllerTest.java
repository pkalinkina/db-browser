package com.db.browser.dist.controller;

import com.db.browser.dist.CoreApplication;
import com.db.browser.dist.entity.Registration;
import com.db.browser.dist.entity.RegistrationsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RegistrationsRepository repository;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void create_new_registration() throws Exception {
        Registration registration = create();
        mvc
                .perform(
                        post("/register")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(registration))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.databaseName").value(registration.getDatabaseName()))
                .andExpect(jsonPath("$.hostname").value(registration.getHostname()))
                .andExpect(jsonPath("$.name").value(registration.getName()))
                .andExpect(jsonPath("$.password").value(registration.getPassword()))
                .andExpect(jsonPath("$.username").value(registration.getUsername()))
                .andExpect(jsonPath("$.port").value(registration.getPort()))
                .andExpect(jsonPath("$.schema").value(registration.getSchema()))
                .andDo(
                        rs -> {
                            Registration response = mapper.readValue(rs.getResponse().getContentAsString(), Registration.class);
                            Integer id = response.getId();
                            Registration saved = repository.findById(id).get();
                            assertEquals(registration.getDatabaseName(), saved.getDatabaseName());
                            assertEquals(registration.getHostname(), saved.getHostname());
                            assertEquals(registration.getName(), saved.getName());
                            assertEquals(registration.getPassword(), saved.getPassword());
                            assertEquals(registration.getUsername(), saved.getUsername());
                            assertEquals(registration.getPort(), saved.getPort());
                            assertEquals(registration.getSchema(), saved.getSchema());
                            repository.deleteById(id);
                        }
                );
    }

    @Test
    public void update_existing_registration() throws Exception {
//        given
        Registration registration = create();
        Integer id = repository.save(registration).getId();

//        when
        registration.setName("newName");
        mvc
                .perform(
                        put("/register")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(registration))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.databaseName").value(registration.getDatabaseName()))
                .andExpect(jsonPath("$.hostname").value(registration.getHostname()))
                .andExpect(jsonPath("$.name").value(registration.getName()))
                .andExpect(jsonPath("$.password").value(registration.getPassword()))
                .andExpect(jsonPath("$.username").value(registration.getUsername()))
                .andExpect(jsonPath("$.port").value(registration.getPort()))
                .andExpect(jsonPath("$.schema").value(registration.getSchema()))
                .andDo(
                        rs -> {
                            Registration response = mapper.readValue(rs.getResponse().getContentAsString(), Registration.class);
                            Registration saved = repository.findById(id).get();
                            assertEquals(id, response.getId());
                            assertEquals(registration.getDatabaseName(), saved.getDatabaseName());
                            assertEquals(registration.getHostname(), saved.getHostname());
                            assertEquals(registration.getName(), saved.getName());
                            assertEquals(registration.getPassword(), saved.getPassword());
                            assertEquals(registration.getUsername(), saved.getUsername());
                            assertEquals(registration.getPort(), saved.getPort());
                            assertEquals(registration.getSchema(), saved.getSchema());
                            repository.deleteById(id);
                        }
                );
    }

    @Test
    public void update_non_existing_registration() throws Exception {
        Registration registration = create();
        registration.setId(1);

        mvc
                .perform(
                        put("/register")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(mapper.writeValueAsString(registration))
                )
                .andExpect(status().isNotFound());
    }


    @Test
    public void delete_existing_registration() throws Exception {
        //        given
        Registration registration = create();
        Integer id = repository.save(registration).getId();

        //        when
        mvc
                .perform(
                        delete("/register/" + id)
                )
                .andExpect(status().isNoContent())
                .andDo(
                        rs -> {
                            assertFalse(repository.findById(id).isPresent());
                        }
                );
    }

    @Test
    public void delete_non_existing_registration() throws Exception {
        //        when
        mvc
                .perform(
                        delete("/register/1")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.exceptionDescription").isNotEmpty())
                .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    public void get_all_registration() throws Exception {
        Registration registration = create();
        Integer id = repository.save(registration).getId();

        mvc
                .perform(
                        get("/register")
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].databaseName").value(registration.getDatabaseName()))
                .andExpect(jsonPath("$[0].hostname").value(registration.getHostname()))
                .andExpect(jsonPath("$[0].name").value(registration.getName()))
                .andExpect(jsonPath("$[0].password").value(registration.getPassword()))
                .andExpect(jsonPath("$[0].username").value(registration.getUsername()))
                .andExpect(jsonPath("$[0].port").value(registration.getPort()))
                .andExpect(jsonPath("$[0].schema").value(registration.getSchema()));
        repository.deleteById(id);
    }

    private Registration create() {
        Registration registration = new Registration();
        registration.setDatabaseName("postgresql");
        registration.setHostname("localhost");
        registration.setName("test");
        registration.setPassword("test");
        registration.setUsername("test");
        registration.setPort(5432);
        return registration;
    }

}
