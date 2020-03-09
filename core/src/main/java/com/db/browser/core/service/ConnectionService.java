package com.db.browser.core.service;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public interface ConnectionService {

    /**
     * Implementations should return created JdbcTemplate from saved connection details.
     */
    Optional<JdbcTemplate> getById(Integer id);
}
