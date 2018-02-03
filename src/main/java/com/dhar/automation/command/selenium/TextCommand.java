package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.Util;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
//@CommandType(name = "text")
public class TextCommand implements CommandExecutor {

    @Autowired
    FindWebElement findWebElement;

    private WebDriver webDriver;

    public TextCommand() {
    }

    public TextCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        long startTime = System.nanoTime();
        WebElement webElement = FindWebElement.getWebElement(webDriver, command.getElement());
        webElement.sendKeys(command.getValue());
        long endTime = System.nanoTime();
        return new CommandResult(CommandStatus.PASS, Util.formatTime(startTime, endTime), null);
    }

}
