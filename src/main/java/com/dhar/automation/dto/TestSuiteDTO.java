package com.dhar.automation.dto;

import com.dhar.automation.domain.Project;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestSuite;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dharmendra.Singh
 */
public class TestSuiteDTO {

    public TestSuiteDTO() {
    }

    public Long id;
    public String name;
    public String description;

    public Set<TestCaseDTO> testCases = new HashSet<>();

    public Set<TestSuiteDTO> testSuites = new HashSet<>();

    public Long projectId;

    public TestSuiteDTO(long id) {
        this.id = id;
    }

    public TestSuiteDTO(TestSuite testSuite) {

    }

    public TestSuiteDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
