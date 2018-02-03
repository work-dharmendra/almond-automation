package com.dhar.automation.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
@Entity
@Table(name = "testcase", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "project_id"}))
public class TestCase implements Serializable{

    public TestCase() {
    }

    public TestCase(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private boolean needResource;

    @ManyToOne
    private Project project;

    @ElementCollection
    @CollectionTable(name = "command")
    @OrderColumn
    private List<Command> commands;

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

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isNeedResource() {
        return needResource;
    }

    public void setNeedResource(boolean needResource) {
        this.needResource = needResource;
    }
}
