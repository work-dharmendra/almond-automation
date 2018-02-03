package com.dhar.automation.domain;

import javax.persistence.*;

/**
 * @author Dharmendra.Singh
 */
@Entity
@Table(name = "config", uniqueConstraints =
@UniqueConstraint(columnNames = {"name"}))
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Environment environment;

    private String name;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
