package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.CommandExecutor;
import com.dhar.automation.command.CommandResult;
import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.command.CommandType;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "javascript")
public class JavascriptCommand implements CommandExecutor {

    private WebDriver webDriver;

    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    public JavascriptCommand() {
    }

    public JavascriptCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String value = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
        ((JavascriptExecutor)webDriver).executeScript(value);

        String screenshot = seleniumUtil.getScreenshot(webDriver, null);
        CommandResult commandResult = new CommandResult(CommandStatus.PASS);
        commandResult.screenshot = screenshot;

        return commandResult;
    }

}
