package com.dhar.automation;

import com.dhar.automation.common.Constants;
import com.dhar.automation.service.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Component
public class Configuration {

    private static Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private static Map<String, String> configuration;
    private static boolean databaseConfigure = true;

    /*@Autowired
    CommonService commonService;
    public void init(){
        configuration = commonService.getConfigurations();
    }*/

    public static Map<String,String> getConfiguration(){
        return configuration;
    }

    public static String getConfiguration(String name){
        return configuration.get(name);
    }

    public static boolean isDatabaseConfigure() {
        return databaseConfigure;
    }

    public static void setDatabaseConfigure(boolean databaseConfigure) {
        Configuration.databaseConfigure = databaseConfigure;
    }

    /**
     * It return value of application.properties in map.
     * @return properties in application.properties
     */
    public Map<String, String> getApplicationProperties(){
        Properties properties = new Properties();
        Map<String,String> applicationProperties = new HashMap<>();
        try {
            properties.load(new InputStreamReader(this.getClass().getResourceAsStream(Constants.DATABASE_FILE)));
            for(String key : properties.stringPropertyNames()){
                applicationProperties.put(key, properties.getProperty(key));
            }

        } catch (IOException e) {
            LOGGER.error("Error in reading application.properties");
        }

        return applicationProperties;
    }

    public boolean isDatabaseDetailsValid(){
        boolean result = false;
        Map<String, String> configurations = new HashMap<>();

        if(StringUtils.isNotEmpty(configurations.get(Constants.JDBC_URL_KEY)) && StringUtils.isNotEmpty(Constants.JDBC_USERNAME_KEY)){
            result = true;
        }

        return result;
    }
}
