package com.dhar.automation.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Dharmendra Chouhan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class CommandFactoryIT {

    @Resource
    CommandFactory commandFactory;


    @Test
    public void init_shouldInitializeCommandsMap(){
        System.out.println(commandFactory.toString());
    }
}
