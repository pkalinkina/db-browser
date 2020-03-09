package com.db.browser.dist.service;

import com.db.browser.dist.entity.Registration;
import com.db.browser.dist.entity.RegistrationsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RegistrationService {
    private final RegistrationsRepository registrationsRepository;

    @Transactional
    public Registration create(@NonNull Registration details) {
        Registration registration = registrationsRepository.save(details);
        log.info("Saved details for {} database with id {}", registration.getDatabaseName(), registration.getId());
        return registration;
    }

    @Transactional
    public List<Registration> getAll() {
        return registrationsRepository.findAll();
    }

    @Transactional
    @CacheEvict("JdbcTemplate")
    public void delete(@NonNull Integer id) {
        registrationsRepository.deleteById(id);
        log.info("Deleted details for database with id {}", id);
    }

    @Transactional
    @CacheEvict(value = "JdbcTemplate", key = "#details.id")
    public Optional<Registration> update(@NonNull Registration details) {
        return registrationsRepository.findById(details.getId())
                .map(saved -> {
                    saved.setDatabaseName(details.getDatabaseName());
                    saved.setHostname(details.getHostname());
                    saved.setName(details.getName());
                    saved.setPassword(details.getPassword());
                    saved.setUsername(details.getUsername());
                    saved.setPort(details.getPort());
                    saved.setSchema(details.getSchema());
                    return saved;
                })
                .map(registrationsRepository::save);
    }
}
