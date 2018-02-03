package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "typeAndStore")
public class TypeAndStoreCommand implements CommandExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(TypeAndStoreCommand.class);

    private WebDriver webDriver;

    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    private static Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
    public TypeAndStoreCommand() {
    }

    public TypeAndStoreCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        WebElement webElement = FindWebElement.getWebElement(webDriver, command.getElement());
        String keys;
        CommandResult commandResult;
        if (webElement != null) {
            keys = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
            String variableName = getVariableName(command.getParams(), execution);
            execution.getRunConfig().getParams().put(variableName, keys);
            webElement.sendKeys(keys);
            commandResult = new CommandResult(CommandStatus.PASS, keys);
            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            commandResult.screenshot = screenshot;
        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.notfound");
        }
        return commandResult;
    }

    private String getVariableName(String param, Execution execution){
        String result ;
        Matcher matcher = pattern.matcher(param);
        if(matcher.matches()){
            result = execution.getRunConfig().getParams().get(matcher.group(1));
        } else {
            result = param;
        }

        return result;
    }
}
