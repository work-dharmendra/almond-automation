package com.dhar.automation.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dharmendra.Singh
 */
public class ProjectDTO {
    public Long id;
    public String name;
    public String description;
    public Set<EnvironmentDTO> environments = new HashSet<>();
}
