package com.dhar.automation.command.selenium;

import com.dhar.automation.Execution;
import com.dhar.automation.command.*;
import com.dhar.automation.common.Util;
import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component
@CommandType(name = "wait")
public class WaitCommand implements CommandExecutor {

    private WebDriver webDriver;

    public WaitCommand() {
    }

    public WaitCommand(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public CommandResult execute(Execution execution, Command command) {
        long startTime = System.nanoTime();
        Long waitPeriod = Long.valueOf(command.getValue()) * 1000;
        try {
            Thread.sleep(waitPeriod);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.nanoTime();
        return new CommandResult(CommandStatus.PASS, Util.formatTime(startTime, endTime), null);
    }
}
