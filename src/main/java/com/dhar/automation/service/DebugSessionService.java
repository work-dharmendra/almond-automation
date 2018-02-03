package com.dhar.automation.service;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.command.CommandExecutor;
import com.dhar.automation.command.CommandFactory;
import com.dhar.automation.command.CommandResult;
import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.common.Constants;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.dao.ConfigurationDao;
import com.dhar.automation.dao.TestCaseDao;
import com.dhar.automation.debug.DebugSession;
import com.dhar.automation.debug.DebugSessionPool;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestCaseExecution;
import com.dhar.automation.dto.CommandDTO;
import com.dhar.automation.dto.DebugSessionDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.*;

import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

/**
 * @author Dharmendra Chouhan
 */
@Service
public class DebugSessionService {
    private static Logger LOGGER = LoggerFactory.getLogger(DebugSessionService.class);

    @Resource
    DebugSessionPool debugSessionPool;

    @Resource
    CommandFactory commandFactory;

    @Resource
    ConfigurationDao configurationDao;

    @Resource
    EnvironmentService environmentService;

    @Resource
    TestCaseDao testCaseDao;

    public DebugSessionDTO executeCommands(DebugSessionDTO requestDTO){
        DebugSessionDTO dto = new DebugSessionDTO();
        String key = requestDTO.uuid;
        RunConfig runConfig = requestDTO.config;
        List<CommandDTO> commandDTOs = requestDTO.commands;
        DebugSession debugSession = null;
        if(StringUtils.isNotEmpty(key)){
            debugSession = debugSessionPool.getDebugSession(key);
            if(debugSession != null){
                //TODO validate if webdriver is still accessible and valid, otherwise create new webdriver
                debugSession.setLastAccessTime(new Date());
                dto.uuid = debugSession.getUuid();
                runConfig = debugSession.getRunConfig();
            }
        }

        if(debugSession == null){
            debugSession = new DebugSession();
            runConfig.getParams().putAll(configurationDao.getConfigurations());//TODO create new map, add configurations then add all incoming params
            debugSession.setRunConfig(runConfig);
            runConfig.setEnvironment(MapEntityDto.buildEnvironment(environmentService.findEnvironment(runConfig.getEnvironment().getId())));
            debugSession.setWebDriver(getWebDriver(runConfig));

            //debugSession.setUuid(key);

            String uuid = debugSessionPool.addDebugSessionNew(debugSession);
            dto.uuid = uuid;
        }

        List<String> status = new ArrayList<>();
        dto.steps = status;
        List<Command> commandList = new ArrayList<>();
        for(CommandDTO commandDTO : commandDTOs){
            commandList.add(MapEntityDto.buildCommand(commandDTO));
        }

        boolean isMoreCommand = commandList.size() > 0;
        int index = 0;

        while (isMoreCommand) {
            Command command = commandList.get(index);
            LOGGER.debug("Executing command for debugId = {}, command = {}"
                    , dto.uuid, command.getName());
            try {
                Execution execution = new Execution();
                execution.setRunConfig(runConfig);
                if ("include".equalsIgnoreCase(command.getName())) {
                    LOGGER.debug("including testcase, debugId = {}, include = {}", dto.uuid, command.getValue());
                    TestCase includedTestCase = testCaseDao.findTestCaseCommands(Long.valueOf(command.getValue()));
                    LOGGER.debug("got test case details, debugId = {}, includedTestCase commands = {}"
                            , dto.uuid, includedTestCase.getCommands().size());
                    index++;
                    commandList.addAll(index, includedTestCase.getCommands());
                    Map<String, String> includeParams = getExecutionParams(execution, command);
                    LOGGER.debug("included params, debugId = {}, includedTestCase params = {}",
                            dto.uuid, includeParams);
                    execution.getRunConfig().getParams().putAll(includeParams);
                    CommandResult commandResult = new CommandResult(CommandStatus.PASS, includedTestCase.getName() + " : " + includedTestCase.getCommands().size() + " commands");
                    status.add(getStatusAsJson(command, commandResult));


                } else if (Constants.COMMAND_INVOKE.equals(command.getName())) {
                    //TODO need to fix invoke command in debug command
                    LOGGER.debug("invoking testcase, debugId = {}, invoke = {}, params", dto.uuid, command.getValue(), command.getParams());
                    RunConfig invokerunConfig = new RunConfig();
                    runConfig.setEnvironment(execution.getRunConfig().getEnvironment());
                    Map<String, String> params = new HashMap<>();
                    params.putAll(configurationDao.getConfigurations());//TODO do not call getConfigurations here, will change in future
                    params.putAll(getExecutionParams(execution, command));
                    runConfig.setParams(params);
                    index++;
                    /*TestCase includedTestCase = testCaseDao.findTestCaseCommands(Long.valueOf(command.getValue()));
                    LOGGER.debug("got test case details, debugId = {}, invokeTestCase commands = {}"
                            , dto.uuid, includedTestCase.getCommands().size());
                    commandList.addAll(index, includedTestCase.getCommands());
                    testScheduler.scheduleTestCase(Long.valueOf(command.getValue()), runConfig, this.execution.getScheduledId());
                    CommandResult commandResult = new CommandResult(CommandStatus.PASS, timeTaken + "", "testcase = " + command.getValue() + ", scheduledId = ");
                    testCaseExecution = new TestCaseExecution(execution.getScheduledId(), execution.getTestCase(), getStatusAsJson(command, commandResult), commandResult.screenshot);
                    testCaseService.saveTestCaseExecution(testCaseExecution);
                    index++;*/
                } else {
                    CommandExecutor commandExecutor = commandFactory.getCommandExecutor(debugSession.getWebDriver(), command);

                    CommandResult commandResult = commandExecutor.execute(execution, command);
                    status.add(getStatusAsJson(command, commandResult));

                    index++;
                }
                isMoreCommand = commandList.size() > index;

            } catch (Exception e) {
                CommandResult commandResult = new CommandResult(CommandStatus.FAIL, e.getMessage());
                status.add(getStatusAsJson(null, commandResult));
                break;
            }
        }

        return dto;
    }

    private WebDriver getWebDriver(RunConfig runConfig) {
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(runConfig.getGridUrl()), chrome());
            driver.manage().window().maximize();
        } catch (Exception e) {
            LOGGER.error("Exception in creating webdriver", e);
            throw new RuntimeException("error.selenium.grid.down");
        }
        return driver;
    }

    public void closeDebugSession(String key){
        LOGGER.debug("closing debug session for key = {}", key );
        DebugSession debugSession = debugSessionPool.getDebugSession(key);

        if(debugSession != null){
            LOGGER.debug("debug session found for key = {}", key);
            WebDriver webDriver = debugSession.getWebDriver();

            try {
                LOGGER.debug("webdriver is present for key = {}", key);
                if(webDriver != null){
                    webDriver.quit();
                    LOGGER.debug("closed web driver for key = {}", key);
                }
            } catch (Exception e) {

            }
            debugSessionPool.remove(key);
            LOGGER.debug("Debug session key removed from pool for key = {}", key);
        }
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

    private Map<String, String> getExecutionParams(Execution execution, Command command) {
        String[] params = command.getParams().split(";");
        Map<String, String> result = new HashMap<>();
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
