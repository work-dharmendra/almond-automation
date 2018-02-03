package com.dhar.automation.dao;

import com.dhar.automation.domain.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class EnvironmentDaoIT {

    @Resource
    EnvironmentDao environmentDao;

    @Test
    public void createEnvironment_shouldCreateEnvironmentSuccessfully(){
        Environment environment = new Environment();
        environment.setName("local");
        environment.setProject(new Project(1l));
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "baseurl");
        jsonObject.addProperty("value", "http://localhost:8080/test2/");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());

        Environment savedEnvironment = environmentDao.create(environment);
    }

    @Test
    public void updateEnvironment_shouldUpdateEnvironmentSuccessfully(){
        Environment environment = new Environment(1l);
        environment.setName("local");
        environment.setProject(new Project(1l));
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "baseurl");
        jsonObject.addProperty("value", "http://localhost:8080/test2/");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());

        environmentDao.update(environment);
    }

}
