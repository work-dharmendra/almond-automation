package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.common.Util;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "waitForElementPresent")
public class WaitForElementCommand implements CommandExecutor {

    private WebDriver webDriver;

    public WaitForElementCommand() {
    }

    public WaitForElementCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        long startTime = System.nanoTime();
        String element = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());

        WaitUntil waitUntil = new WaitUntil(new Condition(webDriver, element, execution));

        waitUntil.until();

        /*WebDriverWait wait = new WebDriverWait(webDriver, 60);
        String [] selectors = command.getElement().split(";");
        for(String selector : selectors){
            wait.until(ExpectedConditions.visibilityOfElementLocated(ByLocator.findBy(selector)));
        }*/

        long endTime = System.nanoTime();
        return new CommandResult(CommandStatus.PASS, Util.formatTime(startTime, endTime), null);
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
