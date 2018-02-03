package com.dhar.automation.common;

import com.dhar.automation.RunConfig;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.Project;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author dharmendra.singh
 */
public class MacroUtilTest {

    @Test
    public void substituteMacro_shouldSubstituteBaseUrlFromRunConfigEnvironment(){
        RunConfig runConfig = new RunConfig();
        Environment environment = new Environment();
        environment.setName("local");
        environment.setProject(new Project(1l));
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "baseurl");
        jsonObject.addProperty("value", "http://baseurl");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());
        runConfig.setEnvironment(environment);
        Assert.assertEquals("http://baseurl", MacroUtil.substituteMacro("${baseurl}", runConfig));
    }

    @Test
    public void substituteMacro_shouldReturnDefaultValueWhenNoVariableFoundForNonPredefinedMacro(){
        RunConfig runConfig = createRunConfig();
        Assert.assertEquals("default", MacroUtil.substituteMacro("${name:default}", runConfig));
    }

    @Test
    public void substituteMacro_shouldReturnNonDefaultValueWhenNoVariableFoundForNonPreDefinedMacro(){
        RunConfig runConfig = createRunConfig();
        runConfig.getParams().put("name", "nondefault");
        MacroUtil.substituteMacro("${name:${timestamp}}", runConfig);
        Assert.assertEquals("nondefault", MacroUtil.substituteMacro("${name:default}", runConfig));
    }

    @Test
    public void substituteMacro_shouldReturnCorrectValueWhenDefaultContainsSemiColonForNonPredefinedMacro(){
        RunConfig runConfig = createRunConfig();
        //runConfig.getParams().put("name", "nondefault");
        Assert.assertEquals("http://test.com", MacroUtil.substituteMacro("${name:http://test.com}", runConfig));
    }

    @Test
    public void substituteMacro_shouldReturnEmptyValueWhenDefaultContainsEmptyStringForNonPredefinedMacro(){
        RunConfig runConfig = createRunConfig();
        Assert.assertEquals("", MacroUtil.substituteMacro("${name:}", runConfig));
    }

    private RunConfig createRunConfig(){
        RunConfig runConfig = new RunConfig();
        Environment environment = new Environment();
        environment.setName("local");
        environment.setProject(new Project(1l));
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "baseurl");
        jsonObject.addProperty("value", "http://baseurl");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());
        runConfig.setEnvironment(environment);

        return runConfig;
    }

}
