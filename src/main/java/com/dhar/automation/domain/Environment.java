package com.dhar.automation.domain;

import com.dhar.automation.common.Constants;
import com.dhar.automation.common.Util;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dharmendra.singh
 */
@Entity
@Table(name = "environment", uniqueConstraints =
@UniqueConstraint(columnNames = {"name", "project_id"}))
public class Environment implements Serializable {

    public Environment() {
    }

    public Environment(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    private String variables;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "environment")
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getBaseUrl(){
        return getVariable(Constants.BASE_URL_KEY);
    }

    public String getVariable(String key){
        String value = null;

        try{
            Map<String, String> params = Util.getObjectFromJsonArray(variables, "key", key);
            value = params.get("value");
        }catch(Exception e){}

        return value;
    }
}
