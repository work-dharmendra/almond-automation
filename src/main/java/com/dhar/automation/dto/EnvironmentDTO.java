package com.dhar.automation.dto;

import com.dhar.automation.domain.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dharmendra.Singh
 */
public class EnvironmentDTO {
    public Long id;
    public String name;
    public String description;

    public Long projectId;
    public String projectName;
    public String variables;

    public EnvironmentDTO() {
    }

    public EnvironmentDTO(Environment environment) {
        this.id = environment.getId();
        this.name = environment.getName();
        this.description = environment.getDescription();
        this.projectId = environment.getProject().getId();
        this.projectName = environment.getProject().getName();
        this.variables = environment.getVariables();
    }
}
