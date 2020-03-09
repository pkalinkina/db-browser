package com.db.browser.dist.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "REGISTRATION")
@SequenceGenerator(name = "REGISTRATION_SEQ", sequenceName = "REGISTRATION_SEQ")
public class Registration {
    @Id
    @GeneratedValue(generator = "REGISTRATION_SEQ")
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String hostname;
    @Column
    private Integer port;
    @Column(nullable = false)
    private String databaseName;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private String schema;
}
