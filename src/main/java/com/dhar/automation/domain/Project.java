package com.dhar.automation.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dharmendra.singh
 */
@Entity
@Table(name = "project", uniqueConstraints =
@UniqueConstraint(columnNames = {"name"}))
public class Project implements Serializable{

    public Project() {
    }

    public Project(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @OneToMany(mappedBy = "project")
    private Set<TestCase> testCases = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<TestSuite> testSuites = new HashSet<>();

    public Set<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        this.testCases = testCases;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Environment> environments = new HashSet<>();

    public Set<Environment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(Set<Environment> environments) {
        this.environments = environments;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
