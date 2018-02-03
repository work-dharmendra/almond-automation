package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra Chouhan
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "select")
public class SelectCommand implements CommandExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(SelectCommand.class);

    private WebDriver webDriver;

    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    public SelectCommand() {
    }

    public SelectCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        //WebElement webElement = FindWebElement.getWebElement(webDriver, command.getElement());
        CommandResult commandResult;
        Select select = getElement(command.getElement());
        if (select != null) {
            String[] selectArray = command.getValue().split("=");
            String typeSelectSelector;
            String value;
            if (selectArray.length == 1) {
                typeSelectSelector = "value";
                value = MacroUtil.substituteMacro(selectArray[0], execution.getRunConfig());
            } else {
                typeSelectSelector = selectArray[0];
                value = MacroUtil.substituteMacro(selectArray[1], execution.getRunConfig());
            }

            switch (typeSelectSelector) {
                case "index":
                    select.selectByIndex(Integer.parseInt(value));
                    break;
                case "value":
                    select.selectByValue(value);
                    break;
                case "text":
                    select.selectByVisibleText(value);
                    break;
                default:
                    throw new RuntimeException("error.element.invalid.selector");
            }
            commandResult = new CommandResult(CommandStatus.PASS, value);
            WebElement webElement = FindWebElement.getWebElement(webDriver, command.getElement());
            String screenshot = seleniumUtil.getScreenshot(webDriver, webElement);
            commandResult.screenshot = screenshot;

        } else {
            commandResult = new CommandResult(CommandStatus.FAIL, "error.element.notfound");
        }
        return commandResult;
    }

    private Select getElement(String element) {
        String[] command = element.split("=");
        String selectorType = command[0];
        String elementPath = command[1];
        Select select = null;
        try {
            if ("id".equals(selectorType.toLowerCase())) {
                select = new Select(webDriver.findElement(By.id(elementPath)));
            }
            if ("name".equals(selectorType.toLowerCase())) {
                select = new Select(webDriver.findElement(By.name(elementPath)));
            }
            if ("class".equals(selectorType.toLowerCase())) {
                select = new Select(webDriver.findElement(By.className(elementPath)));
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("no such element for 1" + element, e);
            throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_ELEMENT_NOTFOUND, "element", element);
        } catch (Exception e) {
            LOGGER.error("exception in finding element", e);
            throw new RuntimeException("exception in finding select element", e);
        }
        return select;
    }

}
