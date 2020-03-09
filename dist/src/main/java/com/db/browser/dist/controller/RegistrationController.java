package com.db.browser.dist.controller;

import com.db.browser.dist.entity.Registration;
import com.db.browser.dist.service.RegistrationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Api(description = "CRUD to manage connection details", tags = {"Registration"})
@Controller
@RequestMapping(path = "/register", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RegistrationController {

    private final RegistrationService registrationService;

    private static <T> ResponseEntity<T> resolve(Optional<T> update) {
        return update.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Registration> create(@RequestBody Registration details) {
        return ok(registrationService.create(details));
    }

    @RequestMapping(method = PUT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Registration> update(@RequestBody Registration details) {
        return resolve(registrationService.update(details));
    }

    @RequestMapping(method = DELETE, path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        registrationService.delete(id);
        return noContent().build();
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Registration>> getAll() {
        return ok(registrationService.getAll());
    }
}
