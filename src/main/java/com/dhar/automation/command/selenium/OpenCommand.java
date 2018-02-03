package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.MacroUtil;
import com.dhar.automation.common.Util;
import com.dhar.automation.domain.Command;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "open")
public class OpenCommand implements CommandExecutor {

    private WebDriver webDriver;

    private SeleniumUtil seleniumUtil = new SeleniumUtil();

    public OpenCommand() {
    }

    public OpenCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        String url = MacroUtil.substituteMacro(command.getValue(), execution.getRunConfig());
        webDriver.navigate().to(url);

        //String screenshot = seleniumUtil.getScreenshot(webDriver, null);
        CommandResult commandResult = new CommandResult(CommandStatus.PASS, url);
        //commandResult.screenshot = screenshot;
        return commandResult;
    }
}
