package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.pool.EnvironmentResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Dharmendra.Singh
 */
@Component
public class TestRunner {
    private static Logger LOG = LoggerFactory.getLogger(TestRunner.class);

    @Autowired
    ApplicationContext applicationContext;

    public void execute(Execution execution){
        TestExecutor testExecutor = applicationContext.getBean(TestExecutor.class);
        testExecutor.setExecution(execution);
        //LOG.debug("start executing new testcase, scheduledId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
        new Thread(testExecutor).start();
        //testExecutor.run();
    }

}
