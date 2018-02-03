package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "type")
public class TypeCommand implements CommandExecutor {

    private WebDriver webDriver;

    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    public TypeCommand() {
    }

    public TypeCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String element = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        WebElement webElement = FindWebElement.getWebElement(webDriver, element);
        String keys;
        CommandResult commandResult;
        if (webElement != null) {
            keys = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
            try {
                if(StringUtils.isNotEmpty(webElement.getAttribute("value"))){
                    webElement.clear();
                }
            } catch (Exception e) {

            }
            webElement.sendKeys(keys);
            commandResult = new CommandResult(CommandStatus.PASS, keys);
            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            commandResult.screenshot = screenshot;
        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.notfound");
        }
        return commandResult;
    }

}
