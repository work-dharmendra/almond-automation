package com.dhar.automation;

import com.dhar.automation.database.LiquibaseMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Component
public class ApplicationInit implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationInit.class);

    @Resource
    LiquibaseMigration liquibaseMigration;

    @Resource
    Configuration configuration;

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("initializing automation");
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "fatal");
        System.setProperty("webdriver.chrome.args", "--disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        liquibaseMigration.update(new HashMap<String, String>());
    }

}
