package com.dhar.automation.domain;

import com.dhar.automation.common.Util;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
@Entity
@Table(name = "environmentresource")
public class EnvironmentResource {

    public EnvironmentResource() {
    }

    public EnvironmentResource(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String variables;

    @ManyToOne
    private Environment environment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getVariable(String key){
        String value = null;

        try{
            Map<String, String> params = Util.getObjectFromJsonArray(variables, "key", key);
            value = params.get("value");
        }catch(Exception e){}

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnvironmentResource that = (EnvironmentResource) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
