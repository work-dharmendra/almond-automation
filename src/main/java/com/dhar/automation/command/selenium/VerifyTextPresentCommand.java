package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.apache.commons.lang3.StringUtils;
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
@CommandType(name = "verifyTextPresent")
public class VerifyTextPresentCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();
    public VerifyTextPresentCommand() {
    }

    public VerifyTextPresentCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        WebElement webElement = FindWebElement.getWebElement(webDriver, command.getElement());

        String actual = webElement.getText();
        CommandResult commandResult;
        if(actual != null){
            if(command.getValue() != null){
                String value = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
                if (StringUtils.isEmpty(command.getParams()) || "case_insensitive".equals(command.getParams())) {
                    commandResult = new CommandResult(actual.toLowerCase().contains(value.toLowerCase())
                            ? CommandStatus.PASS : CommandStatus.FAIL
                            , "case insensitive : actual = " + actual + ", expected = " + value);
                } else {
                    commandResult = new CommandResult(actual.equals(value)
                            ? CommandStatus.PASS : CommandStatus.FAIL
                            , "case sensitive : actual = " + actual + ", expected = " + value);
                }
            } else {
                commandResult = new CommandResult(CommandStatus.FAIL, "error.command.value.null");
            }
            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            commandResult.screenshot = screenshot;
        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.value.null");
        }
        return commandResult;
    }


}
