package com.dhar.automation.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class LiquibaseMigrationIT {

    @Resource
    LiquibaseMigration liquibaseMigration;

    @Test
    public void shouldUpdateDatabaseSuccessfully(){
        Map<String, String> params = new HashMap<>();
        params.put("host", "localhost:3306");
        params.put("database", "automation");
        params.put("username", "automation");
        params.put("password", "automation");
        liquibaseMigration.update(params);
    }
}
