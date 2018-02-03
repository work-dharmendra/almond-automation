package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.RunType;
import com.dhar.automation.command.CommandFactory;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.TestCase;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
@RunWith(JMockit.class)
public class NewTestExecutorIT {

    Logger LOG = LoggerFactory.getLogger(NewTestExecutorIT.class);
    @Autowired
    ApplicationContext applicationContext;

    @Injectable
    DataSource dataSource;

    @Injectable
    CommandFactory commandFactory;

    @Test
    public void testBasic() throws MalformedURLException {

        TestCase testCase = new TestCase();
        testCase.setId(1l);
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "http://10.80.70.124:8080/login.htm"));
        commands.add(new Command("waitForElement", "id:userName", null));
        commands.add(new Command("type", "id:userName", "ss"));
        commands.add(new Command("type", "id:password", "aaa"));
        commands.add(new Command("click", "id:submit", null));
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
        TestExecutor testExecutor = new TestExecutor();
        testExecutor.setExecution(execution);

        //WebDriver driver =  new RemoteWebDriver(new URL(getGridUrl), chrome());

        try {
            //new Thread(testExecutor).start();
            testExecutor.run();
        } catch (Exception e) {
            LOG.error("exception", e);
        }

    }
}
