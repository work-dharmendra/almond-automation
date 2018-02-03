package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.command.CommandExecutor;
import com.dhar.automation.command.CommandFactory;
import com.dhar.automation.command.CommandResult;
import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.common.Constants;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.common.Util;
import com.dhar.automation.dao.ConfigurationDao;
import com.dhar.automation.dao.TestCaseDao;
import com.dhar.automation.domain.*;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.pool.EnvironmentResourcePool;
import com.dhar.automation.service.ExecutionListService;
import com.dhar.automation.service.TestCaseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

/**
 * @author Dharmendra.Singh
 */
@Scope("prototype")
@Component
public class TestExecutor implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(TestExecutor.class);

    @Resource
    CommandFactory commandFactory;

    @Resource
    TestCaseService testCaseService;
    @Resource
    ExecutionListService executionListService;
    @Resource
    TestCaseDao testCaseDao;
    @Resource
    EnvironmentResourcePool environmentResourcePool;
    @Resource
    TestScheduler testScheduler;
    @Resource
    ConfigurationDao configurationDao;

    private Execution execution;
    private WebDriver driver;

    public TestExecutor() {

    }

    public TestExecutor(Execution execution) {
        this.execution = execution;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void run() {

        ExecutionList executionList = null;
        Schedule schedule = null;
        Environment environment = null;
        TestCase testCase = null;
        try {

            executionList = executionListService.getExecutionList(execution.getExecutionListId());
            schedule = executionList.getSchedule();
            environment = schedule.getEnvironment();
            testCase = executionList.getTestCase();

            LOG.debug("Executing new test case starts : execution = {}, testCaseId = {}, commandIndex = {}", schedule.getId(), testCase.getId(), executionList.getCommandIndex());

            Map<String, String> params = new HashMap<>();
            Map<String, String> commonParams = configurationDao.getConfigurations();
            params.putAll(commonParams);
            params.putAll(schedule.getParamsMap());
            params.putAll(executionList.getParamsMap());

            RunConfig runConfig = new RunConfig();

            runConfig.setParams(params);
            runConfig.setEnvironment(environment);

            execution.setRunConfig(runConfig);
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("status", "started");
                //TestCaseExecution testCaseExecution = new TestCaseExecution(execution.getScheduledId(), execution.getTestCase(), jsonObject.toString());
                executionListService.updateStatus(executionList.getId(), Status.INPROGRESS, null, new Date(), null);
                /*TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, testCase, jsonObject.toString(), null);
                testCaseService.saveTestCaseExecution(testCaseExecution);*/
                //driver = getWebDriver(schedule.getGridUrl());
                driver = execution.getWebDriver();
                boolean success = true;
                int index = 0;
                Long scheduleId = schedule.getScheduleId();
                List<Command> commandList = testCase.getCommands();

                for(int i = 0; i < commandList.size(); i++){
                    commandList.get(i).setIndex(i);
                }
                boolean isMoreCommand = commandList.size() > 0;

                while (isMoreCommand) {
                    Command command = commandList.get(index);
                    LOG.debug("Executing command for scheduleId = {}, testcase = {}, command = {}"
                            , scheduleId, testCase.getId(), command.getName());

                    try {
                        //check  if command is include
                        if ("include".equalsIgnoreCase(command.getName())) {
                            LOG.debug("including testcase, scheduleId = {}, testcase = {}, include = {}", scheduleId
                                    , testCase.getId(), command.getValue());
                            long startTime = System.nanoTime();
                            TestCase includedTestCase = testCaseDao.findTestCaseCommands(Long.valueOf(command.getValue()));
                            LOG.debug("got test case details, scheduleId = {}, testcase = {}, includedTestCase commands = {}"
                                    , scheduleId, testCase, includedTestCase.getCommands().size());
                            index++;
                            commandList.addAll(index, includedTestCase.getCommands());
                            Map<String, String> includeParams = getExecutionParams(execution, command);
                            LOG.debug("included params, scheduleId = {}, testcase = {}, includedTestCase params = {}",
                                    scheduleId, testCase.getId(), includeParams);
                            runConfig.getParams().putAll(includeParams);
                            long endTime = System.nanoTime();
                            long timeTaken = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);//convert nano to ms
                            CommandResult commandResult = new CommandResult(CommandStatus.PASS, timeTaken + "", includedTestCase.getName() + " : " + includedTestCase.getCommands().size() + " commands");
                            TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, getCommand(command), commandResult.commandStatus,
                                    String.valueOf(timeTaken), commandResult.comment, null, commandResult.screenshot);
                            testCaseService.saveTestCaseExecution(testCaseExecution);

                        } else if (Constants.COMMAND_INVOKE.equals(command.getName())) {
                            LOG.debug("invoking testcase, getExecutionListId = {}, value = {}", scheduleId
                                    , execution.getExecutionListId(), command.getValue());
                            long startTime = System.nanoTime();

                            ExecutionList invokeExecutionList = executionListService.findExecutionListForScheduleAndCommand(schedule.getId(), command.getIndex());

                            Map<String, String> invokeParams = getExecutionParams(execution, command);

                            executionListService.updateParams(invokeExecutionList.getId(), invokeParams);

                            LOG.debug("testcase({}) has been scheduled with executionList={} commandIndex = {}"
                                    , command.getValue(), invokeExecutionList.getId(), command.getIndex());
                            long endTime = System.nanoTime();
                            long timeTaken = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);//convert nano to ms
                            CommandResult commandResult = new CommandResult(CommandStatus.PASS, timeTaken + "", "testcase = " + command.getValue() + ", executionListId = " + invokeExecutionList.getId());
                            TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, getCommand(command), commandResult.commandStatus, String.valueOf(timeTaken),
                                    commandResult.comment, null, commandResult.screenshot);
                            testCaseService.saveTestCaseExecution(testCaseExecution);

                            testScheduler.addToQueue(invokeExecutionList);

                            /*testScheduler.addToQueue(invokeExecutionList);

                            Execution invokeExecution = new Execution();

                            //create runconfig for new testcase;
                            RunConfig invokeRunConfig = new RunConfig();
                            runConfig.setEnvironment(this.execution.getRunConfig().getEnvironment());
                            Map<String, String> params = new HashMap<>();
                            params.putAll(configurationDao.getConfigurations());//TODO do not call getConfigurations here, will change in future
                            params.putAll(getExecutionParams(this.execution, command));
                            runConfig.setParams(params);
                            //testScheduler.scheduleTestCase(Long.valueOf(command.getValue()), runConfig, this.execution.getScheduledId());
                            Long parentScheduleId;

                            if (this.execution.getParentScheduleId() != null) {
                                parentScheduleId = this.execution.getParentScheduleId();
                            } else {
                                parentScheduleId = this.execution.getScheduledId();
                            }
                            Long invokedScheduleId = testScheduler.scheduleTestCase(Long.valueOf(command.getValue()), parentScheduleId, runConfig);
                            LOG.debug("testcase({}) has been scheduled with id={} from scheduledId = {}, testcase = {}"
                                    , command.getValue(), invokedScheduleId, this.execution.getScheduledId(), this.execution.getTestCase().getId());
                            long endTime = System.nanoTime();
                            long timeTaken = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);//convert nano to ms
                            CommandResult commandResult = new CommandResult(CommandStatus.PASS, timeTaken + "", "testcase = " + command.getValue() + ", scheduledId = " + invokedScheduleId);
                            TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, getCommand(command), commandResult.commandStatus, String.valueOf(timeTaken),
                                    commandResult.comment, null, commandResult.screenshot);
                            testCaseService.saveTestCaseExecution(testCaseExecution);*/
                            index++;
                        } else {
                            CommandExecutor commandExecutor = commandFactory.getCommandExecutor(driver, command);
                            long startTime = System.nanoTime();
                            CommandResult commandResult;
                            try {
                                commandResult = commandExecutor.execute(execution, command);
                            } catch (AutomationBaseRuntimeException automationException) {
                                commandResult = new CommandResult(CommandStatus.FAIL, automationException.getDefaultMessage());
                            }
                            long endTime = System.nanoTime();

                            long timeTaken = TimeUnit.NANOSECONDS.toMillis((endTime - startTime));//convert nano to ms
                            commandResult.timeTaken = timeTaken + "";
                            LOG.debug("Executing command name = {}, element = {}, value = {}, result = {}, timetaken = {}, comment = {}"
                                    , command.getName(), command.getElement(), command.getValue(), commandResult.commandStatus
                                    , commandResult.timeTaken, commandResult.comment);

                            TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, getCommand(command), commandResult.commandStatus, String.valueOf(timeTaken),
                                    commandResult.comment, null, commandResult.screenshot);
                            testCaseService.saveTestCaseExecution(testCaseExecution);
                            if (commandResult.commandStatus == CommandStatus.FAIL) {
                                success = false;
                                LOG.debug("command fail : execution = {}, testCaseId = {}, commandName = {}"
                                        , execution.getExecutionListId(), testCase.getId(), command.getName());
                                break;
                            }
                            index++;
                        }

                        isMoreCommand = commandList.size() > index;

                    } catch (Exception e) {
                        success = false;
                        LOG.debug("exception in command execution : execution = {}, stackTrace = {}"
                                , execution.getExecutionListId(), Util.stackTraceToString(e));
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("status", CommandStatus.EXCEPTION.toString());
                        jsonObject.addProperty("stackTrace", Util.stackTraceToString(e));
                        TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, null, CommandStatus.EXCEPTION, null, e.getMessage(), Util.stackTraceToString(e),
                                null);
                        testCaseService.saveTestCaseExecution(testCaseExecution);
                        //executionListService.updateStatus(executionList.getId(), Status.FAIL, e.getMessage());
                        //if any exception occurs in any command, do not execute further commands
                        break;
                    }
                }

                /*jsonObject = new JsonObject();
                jsonObject.addProperty("status", String.valueOf(success ? CommandStatus.PASS : CommandStatus.FAIL));
                TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, jsonObject.toString(), null);
                testCaseService.saveTestCaseExecution(testCaseExecution);*/

                executionListService.updateStatus(executionList.getId(), success ? Status.PASS : Status.FAIL, null, new Date(), new Date());

            } finally {

            }

        } catch (Exception e) {
            //e.printStackTrace();
            LOG.error("Exception in executing testcase for execution = {}, exception = {}"
                    , execution.getExecutionListId(), e);
            /*JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("status", CommandStatus.EXCEPTION.toString());
            jsonObject.addProperty("stackTrace", Util.stackTraceToString(e));
            jsonObject.addProperty("comment", e.getMessage());
            TestCaseExecution testCaseExecution = new TestCaseExecution(executionList, jsonObject.toString(), null);
            testCaseService.saveTestCaseExecution(testCaseExecution);*/

            executionListService.updateStatus(executionList.getId(), Status.FAIL, e.getMessage(), new Date(), new Date());
            throw new RuntimeException("error.execute.testcase");
        } finally {
            //TODO don't know if this is correct place to add resource back to pool or we need to put this code in some other class
            /*if (execution.getTestCase().isNeedResource()) {
                LOG.debug("Putting EnvironmentResource in queue for TestCase = {} with scheduledId = {}", execution.getTestCase().getId(), execution.getScheduledId());
                environmentResourcePool.addResourceToEnvironment(execution.getRunConfig().getEnvironment(), execution.getRunConfig().getEnvironmentResource());
            } else {
                LOG.debug("Since isNeedResource is false not putting EnvironmentResource in queue for TestCase = {} with scheduledId = {}", execution.getTestCase().getId(), execution.getScheduledId());
            }*/

            if (driver != null) {
                LOG.debug("closing webdriver for execution = {}", execution.getExecutionListId());
                driver.quit();
            }
        }
        LOG.debug("Executing new test case end : execution = {}", execution.getExecutionListId());
    }

    /**
     * This method return WebDriver instance from grid configuration.
     * Currently it simply return RemoteWebDriver instance.
     * Selenium grid maybe correct but we cannot obtains RemoteWebDriver instance because of load.
     * TODO This function should identify different exception(recoverable and unrecoverable).
     * It should return immediately for unrecoverable exception like hostname not valid but
     * try for recoverable exception.
     *
     * @return
     */
    private WebDriver getWebDriver(String gridUrl) {
        if (this.driver != null) {
            return this.driver;
        }

        try {
            this.driver = new RemoteWebDriver(new URL(gridUrl), chrome());
            //driver = new RemoteWebDriver(new URL(execution.getRunConfig().getGridUrl()), firefox());
            this.driver.manage().window().maximize();
        } catch (Exception e) {
            LOG.error("Exception in creating webdriver", e);
            throw new RuntimeException("error.selenium.grid.down");
        }
        return driver;
    }

    private String getStatusAsJson(Command command, CommandResult commandResult) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new GsonBuilder().create();
        jsonObject.addProperty("status", commandResult.commandStatus.toString());
        jsonObject.addProperty("timeTaken", commandResult.timeTaken);
        jsonObject.addProperty("comment", commandResult.comment);
        jsonObject.add("command", gson.toJsonTree(command));
        return jsonObject.toString();
    }

    private String getCommand(Command command){
        return new Gson().toJson(command);
    }

    private Map<String, String> getExecutionParams(Execution execution, Command command) {
        Map<String, String> result = new HashMap<>();
        if(command.getParams() == null){
            return result;
        }

        String[] params = command.getParams().split(";");

        for (String param : params) {
            if (StringUtils.isNotEmpty(param)) {
                String[] variable = param.split("=");
                String key = MacroUtil.substituteMacro(variable[0], execution.getRunConfig());
                String value = MacroUtil.substituteMacro(variable[1], execution.getRunConfig());
                result.put(key, value);
            }

        }
        return result;
    }


}
