package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.common.Util;
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
@CommandType(name = "waitForElementAndClick")
public class WaitForElementAndClickCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    public WaitForElementAndClickCommand() {
    }

    public WaitForElementAndClickCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String element = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());

        WaitUntil waitUntil = new WaitUntil(new Condition(webDriver, element, execution));

        waitUntil.until();

        WebElement webElement = FindWebElement.getWebElement(webDriver, element);
        CommandResult commandResult;
        if (webElement != null) {

            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            webElement.click();
            commandResult = new CommandResult(CommandStatus.PASS);
            commandResult.screenshot = screenshot;
            commandResult.comment = element;
        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.notfound");
        }

        return commandResult;
    }

    class Condition implements ICondition{

        private WebDriver webDriver;
        private String element;
        private Execution execution;
        private String comment;

        public Condition(WebDriver webDriver, String element, Execution execution) {
            this.webDriver = webDriver;
            this.element = element;
            this.execution = execution;
        }

        @Override
        public boolean done() {
            boolean result = false;
            try{

                WebElement webElement = FindWebElement.getWebElement(this.webDriver, this.element);
                if(webElement != null && webElement.isDisplayed()){
                    result = true;
                }
            } catch (Exception e){

            }
            return result;
        }
    }

}
