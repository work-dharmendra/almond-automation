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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "verifyElementPresent")
public class VerifyElementPresentCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();
    public VerifyElementPresentCommand() {
    }

    public VerifyElementPresentCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String element = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        WebElement webElement = FindWebElement.getWebElement(webDriver, element);

        CommandResult commandResult;
        if(webElement != null){
            commandResult = new CommandResult(CommandStatus.PASS);
        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "element.exist:" + webElement.getTagName());
        }
        String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
        commandResult.screenshot = screenshot;
        return commandResult;
    }


}
