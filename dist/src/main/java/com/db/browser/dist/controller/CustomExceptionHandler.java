package com.db.browser.dist.controller;

import com.db.browser.core.service.exception.ResourceNotFound;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ResourceNotFound.class})
    protected ResponseEntity<Void> handleResourceNotFound(ResourceNotFound ex) {
        return notFound().build();
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ExceptionDescription> handleException(Exception ex) {
        ExceptionDescription exceptionDescription = new ExceptionDescription(ex.getMessage());
        log.warn("Caught exception {} with id {}", ex, exceptionDescription.getUuid());
        return status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDescription);
    }

    @Getter
    public static class ExceptionDescription {
        private String exceptionDescription;
        private String uuid;

        public ExceptionDescription(String exceptionDescription) {
            this.exceptionDescription = exceptionDescription;
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
