package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import org.apache.commons.lang3.StringUtils;
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
@CommandType(name = "untilVerifyTextPresent")
public class UntilVerifyTextPresentCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();
    public UntilVerifyTextPresentCommand() {
    }

    public UntilVerifyTextPresentCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {

        CommandResult commandResult;
        String screenshot = null;
        try{
            Condition condition = new Condition(webDriver, command, execution);
            WaitUntil waitUntil = new WaitUntil(condition);
            waitUntil.until();
            commandResult = new CommandResult(condition.isMatch()
                    ? CommandStatus.PASS : CommandStatus.FAIL
                    , condition.comment);

            screenshot = seleniumUtil.getScreenshot(webDriver, condition.webElement);


        } catch (AutomationBaseRuntimeException e){
            commandResult = new CommandResult(CommandStatus.FAIL, "error.timeout");
            screenshot = seleniumUtil.getScreenshot(webDriver, null);
        }
        commandResult.screenshot = screenshot;
        /*if(actual != null){
            if(command.getValue() != null){
                String value = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
                if (StringUtils.isEmpty(command.getParams()) || "case_insensitive".equals(command.getParams())) {
                    commandResult = new CommandResult(actual.toLowerCase().equals(value.toLowerCase())
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
        }*/
        return commandResult;
    }

    class Condition implements ICondition{

        private WebDriver webDriver;
        private Command command;
        private Execution execution;
        private WebElement webElement;
        private String comment;
        private boolean isMatch = false;

        public Condition(WebDriver webDriver, Command command, Execution execution) {
            this.webDriver = webDriver;
            this.command = command;
            this.execution = execution;
        }

        public WebElement getWebElement() {
            return webElement;
        }

        public String getComment() {
            return comment;
        }

        public boolean isMatch() {
            return isMatch;
        }

        @Override
        public boolean done() {
            boolean result = false;

            String element = MacroUtil.substituteMacro(command.getElement(), this.execution.getRunConfig());
            WebElement webElement = FindWebElement.getWebElement(webDriver, element);

            if (webElement != null) {
                String actual = webElement.getText();
                boolean caseInsensitive = true;
                boolean exactMatch = false;
                //TODO reset caseInsensitive and exactMatch based on command.getParams

                if(actual != null){
                    if(command.getValue() != null){
                        String value = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
                        if (caseInsensitive && !exactMatch ) {
                            if (actual.toLowerCase().contains(value.toLowerCase())) {
                                result = true;
                                isMatch = true;
                                this.webElement = webElement;
                                this.comment = "case insensitive : actual = " + actual + ", expected = " + value;
                            } else {
                                isMatch = false;
                                this.webElement = webElement;
                                this.comment = "case insensitive : actual = " + actual + ", expected = " + value;
                            }
                        }
                    }
                }
            }

            return result;
        }
    }

}
