package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.RunType;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class TestExecutorIT {

    Logger LOG = LoggerFactory.getLogger(TestExecutorIT.class);
    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testBasic() throws MalformedURLException {

        TestCase testCase = new TestCase();
        testCase.setId(1l);
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "http://localhost:8080/test/login.html"));
        commands.add(new Command("waitForElementPresent", "id:userName", null));
        commands.add(new Command("type", "id:userName", "dharmendra.chouhan@synechron.com"));
        commands.add(new Command("type", "id:password", "welcome"));
        commands.add(new Command("click", "id:submit", null));
        commands.add(new Command("waitForElementPresent", "id:content", null));
        commands.add(new Command("verifyTextPresent", "id:content", "This is home page1"));
        testCase.setCommands(commands);
        Execution execution = new Execution(1l, testCase, RunType.TESTCASE, null);
        execution.setTestCase(testCase);
        Environment environment = new Environment();
        environment.setName("dev");
        //execution.setEnvironment(environment);
        RunConfig runConfig = new RunConfig();
        Map<String, String> params = new HashMap<>();
        String gridUrl = "http://localhost:4444/wd/hub";
        params.put("config.grid_url", gridUrl);
        runConfig.setParams(params);
        execution.setRunConfig(runConfig);
        TestExecutor testExecutor = applicationContext.getBean(TestExecutor.class);
        testExecutor.setExecution(execution);

        //WebDriver driver =  new RemoteWebDriver(new URL(getGridUrl), DesiredCapabilities.chrome());
        //testExecutor.setDriver(driver);
        try {
            //new Thread(testExecutor).start();
            testExecutor.run();
        } catch (Exception e) {
            LOG.error("exception", e);
        }

    }
}
