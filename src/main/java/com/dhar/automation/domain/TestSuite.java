package com.dhar.automation.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dharmendra.Singh
 */
@Entity
@Table(name = "testsuite", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "project_id"}))
public class TestSuite {
    public TestSuite() {
    }

    public TestSuite(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @ManyToMany
    private Set<TestCase> testCases = new HashSet<>();

    @ManyToMany
    private Set<TestSuite> testSuites = new HashSet<>();

    @ManyToOne
    private Project project;

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

    public Set<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        this.testCases = testCases;
    }

    public Set<TestSuite> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(Set<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestSuite suite = (TestSuite) o;

        if (!id.equals(suite.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
