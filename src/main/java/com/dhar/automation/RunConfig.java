package com.dhar.automation;

import com.dhar.automation.common.Constants;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.EnvironmentResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
public class RunConfig {
    private Environment environment;
    private EnvironmentResource environmentResource;
    private Map<String, String> params = new HashMap<>();

    public RunConfig() {
    }

    public RunConfig(Environment environment, EnvironmentResource environmentResource, Map<String, String> params) {
        this.environment = environment;
        this.environmentResource = environmentResource;
        this.params = params;
    }

    public EnvironmentResource getEnvironmentResource() {
        return environmentResource;
    }

    public void setEnvironmentResource(EnvironmentResource environmentResource) {
        this.environmentResource = environmentResource;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }



    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String
    getVariableValue(String key){
        String value = null;
        //first check it in params

        value = params.get(key);

        if(value == null && environmentResource != null){
            value = environmentResource.getVariable(key);
        }
        if(value == null){
            value = environment.getVariable(key);
        }

        return value;
    }

    public String getGridUrl(){
        return getVariableValue(Constants.GRID_URL_KEY);
    }

    public static void main(String [] args){
        RunConfig runConfig = new RunConfig();
        Map<String, String> params = new HashMap<>();
        params.put("name", "aaa");
        params.put("name1", "aaa");
        runConfig.setParams(params);
    }
}
