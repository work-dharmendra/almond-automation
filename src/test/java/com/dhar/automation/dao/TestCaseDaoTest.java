package com.dhar.automation.dao;

import com.dhar.automation.common.Constants;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.TestCase;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JMockit.class)
public class TestCaseDaoTest {

    @Mocked
    TestCaseDao testCaseDao;

    /**
     * It create simple mock database with following hierarchy to test cycle while creating and updating testcase.
     * 1
     *  - 2
     *  - 3
     *   - 4
     *   - 5
     * 6
     */
    public void createTestCaseDatabase() {

        //final TestCaseDao testCaseDao = new TestCaseDao();

        final Map<Long, TestCase> testCaseMap = new HashMap<>();

        testCaseMap.put(1l,addCommandToTestCase(new TestCase(1l)
                ,new Command("open", null, "url")
                ,new Command(Constants.COMMAND_INCLUDE, null, "2")
                ,new Command(Constants.COMMAND_INCLUDE, null, "3")));
        testCaseMap.put(2l, new TestCase(2l));
        testCaseMap.put(3l,addCommandToTestCase(new TestCase(3l)
                ,new Command(Constants.COMMAND_INCLUDE, null, "4")
                ,new Command(Constants.COMMAND_INVOKE, null, "5")));
        testCaseMap.put(4l, new TestCase(4l));
        testCaseMap.put(5l, new TestCase(5l));
        testCaseMap.put(6l, new TestCase(6l));

        new Expectations(TestCaseDao.class) {{
            testCaseDao.findTestCase(anyLong);
            minTimes=0;
            result = new Delegate<TestCaseDao>() {

               public TestCase findTestCase(Long id){
                    return testCaseMap.get(id);
               }
            };


        }};
    }

    @Test
    public void cycleExist_shouldReturnFalseWhenTestCaseContainNoIncludeCommand() {
        TestCase testCase = new TestCase();

        Assert.assertFalse(testCaseDao.cycleExist(testCase, new ArrayList<Long>()));
    }

    @Test
    public void cycleExist_shouldReturnTrueWhenExistingTestCaseContainIncludeInvokeCommandAndItCreateCycle(){
        createTestCaseDatabase();
        TestCase testCase = new TestCase(5l);

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INCLUDE, null, "1"));

        List<Long> testCaseIdList = new ArrayList<>();
        testCaseIdList.add(5l);
        Assert.assertTrue(testCaseDao.cycleExist(testCase, testCaseIdList));

        testCase = new TestCase(5l);

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INVOKE, null, "1"));
        Assert.assertTrue(testCaseDao.cycleExist(testCase, testCaseIdList));
    }

    @Test
    public void cycleExist_shouldReturnTrueWhenExistingTestCaseContainIncludeCommandAndNoCycleExist(){
        createTestCaseDatabase();
        List<Long> testCaseIdList = new ArrayList<>();
        testCaseIdList.add(5l);
        TestCase testCase = new TestCase(5l);

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INCLUDE, null, "6"));

        Assert.assertFalse(testCaseDao.cycleExist(testCase, testCaseIdList));
    }

    @Test
    public void cycleExist_shouldReturnTrueWhenNewTestCaseContainIncludeCommandAndNoCycleExist(){
        createTestCaseDatabase();
        List<Long> testCaseIdList = new ArrayList<>();
        TestCase testCase = new TestCase();

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INCLUDE, null, "6"));

        Assert.assertFalse(testCaseDao.cycleExist(testCase, testCaseIdList));
    }

    @Test
    public void cycleExist_shouldReturnTrueWhenTestCaseContainIncludeInvokeItself(){
        createTestCaseDatabase();
        List<Long> testCaseIdList = new ArrayList<>();
        testCaseIdList.add(5l);
        TestCase testCase = new TestCase(5l);

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INCLUDE, null, "5"));

        Assert.assertTrue(testCaseDao.cycleExist(testCase, testCaseIdList));

        testCase = new TestCase(5l);

        addCommandToTestCase(testCase, new Command(Constants.COMMAND_INVOKE, null, "5"));

        Assert.assertTrue(testCaseDao.cycleExist(testCase, testCaseIdList));
    }

    private TestCase addCommandToTestCase(TestCase testCase, Command ... commandList){
        List<Command> commands = new ArrayList<>();

        for(Command command : commandList){
            commands.add(command);
        }

        testCase.setCommands(commands);

        return testCase;
    }

    private List<Command> createCommandList() {
        List<Command> commandList = new ArrayList<>();

        return commandList;
    }

}
