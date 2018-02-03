package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.CommandExecutor;
import com.dhar.automation.command.CommandResult;
import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.command.CommandType;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.apache.commons.lang3.StringUtils;
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
@CommandType(name = "dragAndDrop")
public class DragAndDropCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();
    public DragAndDropCommand() {
    }

    public DragAndDropCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        CommandResult commandResult;

        String source = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        String target = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
        WebElement sourceElement = FindWebElement.getWebElement(webDriver, source);
        WebElement targetElement = FindWebElement.getWebElement(webDriver, target);

        Actions actions = new Actions(webDriver);
        actions.clickAndHold(sourceElement)
                .moveToElement(targetElement)
                .release(targetElement)
                .build()
                .perform();

        commandResult = new CommandResult(CommandStatus.PASS);
        try {
            commandResult.screenshot = seleniumUtil.getScreenshot(webDriver, sourceElement);
        } catch (Exception e) {

        }

        return commandResult;

    }


}
