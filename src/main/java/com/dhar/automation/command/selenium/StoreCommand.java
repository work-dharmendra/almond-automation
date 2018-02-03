package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "store")
public class StoreCommand implements CommandExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(StoreCommand.class);

    private WebDriver webDriver;

    private static SeleniumUtil seleniumUtil = new SeleniumUtil();

    private static Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
    public StoreCommand() {
    }

    public StoreCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String key = MacroUtil.substituteMacro(command.getElement(), execution.getRunConfig());
        String value = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
        execution.getRunConfig().getParams().put(key, value);

        return new CommandResult(CommandStatus.PASS, "key = " + key + ", value = " + value);
    }

}
