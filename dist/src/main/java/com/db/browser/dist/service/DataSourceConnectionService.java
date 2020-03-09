package com.db.browser.dist.service;

import com.db.browser.core.service.ConnectionDetailsProvider;
import com.db.browser.core.service.ConnectionService;
import com.db.browser.dist.entity.Registration;
import com.db.browser.dist.entity.RegistrationsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DataSourceConnectionService implements ConnectionService {

    private final RegistrationsRepository registrationsRepository;
    private final ConnectionDetailsProvider connectionDetailsProvider;


    @Cacheable("JdbcTemplate")
    @Override
    public Optional<JdbcTemplate> getById(@NonNull Integer id) {
        return registrationsRepository.findById(id)
                .map(this::createDataSource)
                .map(JdbcTemplate::new);
    }

    private DataSource createDataSource(@NonNull Registration details) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(connectionDetailsProvider.getDriverName());
        if (details.getSchema() != null) {
            dataSource.setSchema(details.getSchema());
        }
        dataSource.setUrl(connectionDetailsProvider.getUrl(details.getHostname(), details.getPort(), details.getDatabaseName()));
        dataSource.setUsername(details.getUsername());
        dataSource.setPassword(details.getPassword());
        log.info("Created DataSource {} with id {}", details.getDatabaseName(), details.getId());
        return dataSource;
    }
}
