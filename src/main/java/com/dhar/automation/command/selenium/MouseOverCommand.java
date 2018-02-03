package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.CommandExecutor;
import com.dhar.automation.command.CommandResult;
import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.command.CommandType;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "mouseOver")
public class MouseOverCommand implements CommandExecutor {

    private WebDriver webDriver;

    private SeleniumUtil seleniumUtil = new SeleniumUtil();

    public MouseOverCommand() {
    }

    public MouseOverCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String mouseOverElement = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        String mouseClickElement = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());

        WebElement mouseOver = FindWebElement.getWebElement(webDriver, mouseOverElement);

        Actions builder = new Actions(webDriver);
        builder.moveToElement(mouseOver)
                .build()
                .perform();
        String screenshot = seleniumUtil.getScreenshot(webDriver, null);

        CommandResult commandResult = new CommandResult(CommandStatus.PASS, "mouseOver=" + mouseOverElement + ", click=" + mouseClickElement);
        commandResult.screenshot = screenshot;
        return commandResult;
    }
}
