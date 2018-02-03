package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.RunType;
import com.dhar.automation.database.LiquibaseMigration;
import com.dhar.automation.domain.*;
import com.dhar.automation.dto.TestSuiteDTO;
import com.dhar.automation.pool.EnvironmentResourcePool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
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
public class TestSchedulerIT {

    Logger LOG = LoggerFactory.getLogger(TestSchedulerIT.class);
    @Autowired
    ApplicationContext applicationContext;

    @Resource
    EnvironmentResourcePool resourcePool;
    @Resource
    TestScheduler testScheduler;

    @Test
    public void testBasic() throws MalformedURLException, InterruptedException {

        TestCase testCase = new TestCase();
        testCase.setId(1l);
        System.setProperty("webdriver.chrome.driver", "C:\\dhar\\Software\\Selenium\\drivers\\chromedriver.exe");
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
        environment.setId(1l);
        //execution.setEnvironment(environment);
        RunConfig runConfig = new RunConfig();
        Map<String, String> params = new HashMap<>();
        String gridUrl = "http://localhost:4444/wd/hub";
        params.put("config.grid_url", gridUrl);
        runConfig.setParams(params);
        runConfig.setEnvironment(environment);
        execution.setRunConfig(runConfig);
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        TestScheduler testScheduler = applicationContext.getBean(TestScheduler.class);
        long scheduleId = testScheduler.scheduleTestCase(testCase, runConfig);
        System.out.println("scheduleId = " + scheduleId);
        scheduleId = testScheduler.scheduleTestCase(testCase, runConfig);
        System.out.println("scheduleId = " + scheduleId);
        //Thread.sleep(5000);
        //resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        Thread.sleep(20000);
        /*final TestRunner testRunner = applicationContext.getBean(TestRunner.class);
        testRunner.execute();*/
    }

    @Test
    public void scheduleTestCase_shouldExecuteSingleTestCaseSuccessfully() throws InterruptedException {
        /*Environment environment = new Environment(1l);
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        testScheduler.scheduleTestCase(1l, 1l);*/

        Map<String, String> params = new HashMap<>();
        params.put("username", "dddddd");
        testScheduler.scheduleTestCase(4l, 1l, null, params);
        Thread.sleep(30000);
    }

    @Test
    public void scheduleTestCase_shouldExecuteSingleTestCaseWithoutResourceSuccessfully() throws InterruptedException {
        testScheduler.scheduleTestCase(4l, 1l);
        Thread.sleep(30000);
    }

    @Test
    public void scheduleTestSuite_shouldExecuteSingleTestSuiteSuccessfully() throws InterruptedException {
        Environment environment = new Environment(1l);
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        List<TestSuiteDTO> testSuiteDTOs = new ArrayList<>();
        testSuiteDTOs.add( new TestSuiteDTO(1l));
        List<TestSuite> testSuites = new ArrayList<>();
        testSuites.add(new TestSuite(1l));
        testScheduler.scheduleTestSuite(testSuites, 1l, null, null);
        Thread.sleep(40000);
    }

    @Test
    public void scheduleTestSuite_shouldExecuteSingleTestSuiteWithMulpleTestCaseParallelSuccessfully() throws InterruptedException {
        Environment environment = new Environment(1l);
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        resourcePool.addResourceToEnvironment(environment, new EnvironmentResource());
        List<TestSuiteDTO> testSuiteDTOs = new ArrayList<>();
        testSuiteDTOs.add( new TestSuiteDTO(1l));
        testScheduler.scheduleTestSuite(testSuiteDTOs, 1l);
        Thread.sleep(20000);
    }
}
