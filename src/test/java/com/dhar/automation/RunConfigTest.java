package com.dhar.automation;

import com.dhar.automation.common.Constants;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.EnvironmentResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
public class RunConfigTest {

    @Test
    public void getVariableValue_shouldReturnValueFromParamsWhenParamsContainsKey(){
        EnvironmentResource resource = new EnvironmentResource(1l);
        Map<String, String> params = new HashMap<>();
        params.put("testKey", "testValue");
        RunConfig runConfig = new RunConfig(new Environment(), resource, params);

        Assert.assertEquals("testValue", runConfig.getVariableValue("testKey"));
    }

    @Test
    public void getVariableValue_shouldReturnValueFromEnvResourceWhenParamsNotContainsKey(){
        EnvironmentResource resource = new EnvironmentResource(1l);
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "url");
        jsonObject.addProperty("value", "baseurl");
        jsonArray.add(jsonObject);
        resource.setVariables(jsonArray.toString());
        Map<String, String> params = new HashMap<>();
        RunConfig runConfig = new RunConfig(new Environment(), resource, params);

        Assert.assertEquals("baseurl", runConfig.getVariableValue("url"));
    }

    @Test
    public void getVariableValue_EnvResourceAndParams_NotContainsKey_ReturnNull(){
        EnvironmentResource resource = new EnvironmentResource(1l);
        Map<String, String> params = new HashMap<>();
        RunConfig runConfig = new RunConfig(new Environment(), resource, params);
        Assert.assertNull(runConfig.getVariableValue("key"));
    }

    @Test
    public void getGridUrl_shouldReturnValueFromEnvironmentVariableWhenParamsNotContainsGridUrl(){
        EnvironmentResource resource = new EnvironmentResource(1l);
        Map<String, String> params = new HashMap<>();
        Environment environment = new Environment();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", Constants.GRID_URL_KEY);
        jsonObject.addProperty("value", "value_of_baseurl");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());
        RunConfig runConfig = new RunConfig(environment, resource, params);
        Assert.assertEquals("value_of_baseurl", runConfig.getGridUrl());
    }
}
