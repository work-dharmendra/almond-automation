package com.dhar.automation.dao;

import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.domain.*;
import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.service.TestCaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
@EnableTransactionManagement
public class TestCaseDaoIT {

    @Resource
    TestCaseDao testCaseDao;
    @Resource
    TestCaseService testCaseService;

    @Test
    public void findTestCase_shouldFindTestCaseSuccessfully() {
        TestCaseDTO testCase1 = testCaseService.findTestCase(3l);
        System.out.println(testCase1);
    }

    @Test
    public void createTestCase_shouldCreateTestCaseSuccessfully(){
        TestCaseDTO testCase1 = testCaseService.findTestCase(1l);
        TestCase testCase = new TestCase();
        testCase.setName("test1" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "${baseurl}/test/login.html"));
        commands.add(new Command("waitForElementPresent", "id:userName", null));
        commands.add(new Command("type", "id:userName", "dharmendra.chouhan@synechron.com"));
        commands.add(new Command("type", "id:password", "welcome"));
        commands.add(new Command("click", "id:submit", null));
        commands.add(new Command("waitForElementPresent", "id:content", null));
        commands.add(new Command("verifyTextPresent", "id:content", "This is home page"));
        testCase.setCommands(commands);
        testCase.setProject(new Project(1l));

        testCaseDao.createTestCase(testCase);
    }

    @Test
    public void updateTestCase_shouldUpdateTestCaseSuccessfully(){
        TestCase testCase = new TestCase(2l);
        testCase.setName("test");
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "http://localhost:8080/test/login.html"));
        commands.add(new Command("waitForElementPresent", "id:userName", null));
        commands.add(new Command("type", "id:userName", "dharmendra.chouhan@synechron.com"));
        commands.add(new Command("type", "id:password", "welcome"));
        commands.add(new Command("click", "id:submit", null));
        commands.add(new Command("waitForElementPresent", "id:content", null));
        commands.add(new Command("verifyTextPresent", "id:content", "This is home page1"));
        testCase.setCommands(commands);
        testCase.setProject(new Project(1l));

        testCaseDao.updateTestCase(testCase);
    }

    @Test
    public void saveTestCaseExecution_shouldSaveTestCaseExecutionSuccessfully(){
        /*TestCaseExecution testCaseExecution = new TestCaseExecution(new ExecutionList(1l), null, CommandStatus.PASS, "100", null, null);

        testCaseDao.saveTestCaseExecution(testCaseExecution);*/
    }

    @Test
    public void findTestCaseExecution_shouldReturnTestCaseExecutionSuccessfully(){
        List<TestCaseExecution> testCaseExecutions = testCaseDao.findTestCaseExecution(1l, 1l);
        System.out.println(testCaseExecutions.size());
    }

    @Test
    public void findTestCaseExecution_shouldReturnTestCaseExecutionForGivenScheduleIdAndTestCaseId(){
        List<TestCaseExecution> testCaseExecutions = testCaseDao.findTestCaseExecution(24620548l, 2l);

        for(TestCaseExecution testCaseExecution : testCaseExecutions){
            System.out.println(testCaseExecution);
        }
    }

    @Test
    public void findSchedule_shouldReturnScheduleForTestCaseSuccessfully(){
        List<Schedule> schedules = testCaseDao.findTestCaseScheduleStatus(1l);
        for(Schedule schedule : schedules){
            System.out.println(schedule);
        }
    }



}
