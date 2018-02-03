package com.dhar.automation.controller;

import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.Project;
import com.google.gson.GsonBuilder;

/**
 * @author Dharmendra.Singh
 */
public class Test {

    public static void main(String []args){
        Environment environment = new Environment();
        environment.setName("qa");
        environment.setDescription("description");
    }
}
