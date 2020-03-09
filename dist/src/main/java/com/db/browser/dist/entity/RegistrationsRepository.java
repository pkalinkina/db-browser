package com.db.browser.dist.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationsRepository extends JpaRepository<Registration, Integer> {
}
