package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "click")
public class ClickCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    public ClickCommand() {
    }

    public ClickCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String element = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        WebElement webElement = FindWebElement.getWebElement(webDriver, element);
        CommandResult commandResult;
        if (webElement != null) {

            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            webElement.click();
            commandResult = new CommandResult(CommandStatus.PASS);
            commandResult.screenshot = screenshot;
            commandResult.comment = element;
        } else {
            String screenshot = null;
            //wrap taking of screenshot in try catch because it may throw and we don't want to stop processing
            //just because we not able to take screenshot.
            try {
                screenshot = seleniumUtil.getScreenshot(webDriver, null);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.notfound");
            commandResult.screenshot = screenshot;
        }
        return commandResult;
    }

}
