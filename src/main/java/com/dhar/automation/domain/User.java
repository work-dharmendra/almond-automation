package com.dhar.automation.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dharmendra.Singh on 4/16/2015.
 */
@Entity
@Table(name = "user", uniqueConstraints =
@UniqueConstraint(columnNames = {"username", "environment_id"}))
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String userType;

    @ManyToOne
    private Environment environment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
