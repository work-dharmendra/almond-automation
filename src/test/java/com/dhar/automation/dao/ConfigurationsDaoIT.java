package com.dhar.automation.dao;

import com.dhar.automation.common.Constants;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.Project;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class ConfigurationsDaoIT {

    @Resource
    ConfigurationDao configurationDao;

    @Test
    public void addConfig_shouldAddConfigSuccessfully(){
        configurationDao.addConfig(Constants.GRID_URL_KEY, "http://localhost:4444/wd/hub");
    }

}
