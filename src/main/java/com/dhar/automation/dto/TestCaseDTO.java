package com.dhar.automation.dto;

import com.dhar.automation.domain.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */
public class TestCaseDTO {
    public TestCaseDTO() {
    }

    public TestCaseDTO(Long id) {
        this.id = id;
    }

    public Long id;

    public String name;
    public String description;
    public boolean isNeedResource;

    public Long projectId;

    public List<CommandDTO> commands = new ArrayList<>();


    public TestCaseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
