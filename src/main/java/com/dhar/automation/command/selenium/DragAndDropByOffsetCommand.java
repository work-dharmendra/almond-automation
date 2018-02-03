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
@CommandType(name = "dragAndDropByOffset")
public class DragAndDropByOffsetCommand implements CommandExecutor {

    private WebDriver webDriver;
    private static SeleniumUtil seleniumUtil = new SeleniumUtil();
    public DragAndDropByOffsetCommand() {
    }

    public DragAndDropByOffsetCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        CommandResult commandResult;

        String source = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());

        String [] position = command.getValue().split(";");

        int x = 0;
        int y = 0;

        for(String offset : position){
            String[] coordinates = offset.split("=");

            if(coordinates[0].equals("x")){
                x = Integer.parseInt(coordinates[1]);
            } else if(coordinates[0].equals("y")){
                y = Integer.parseInt(coordinates[1]);
            }

        }

        WebElement sourceElement = FindWebElement.getWebElement(webDriver, source);

        Actions actions = new Actions(webDriver);
        actions.clickAndHold(sourceElement)
                .moveByOffset(x, y)
                .release()
                .build()
                .perform();

        commandResult = new CommandResult(CommandStatus.PASS);
        try {
            commandResult.screenshot = seleniumUtil.getScreenshot(webDriver, sourceElement);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandResult;

    }


}
