package com.dhar.automation.command;

import com.dhar.automation.Execution;
import com.dhar.automation.domain.Command;

/**
 *
 * @author Dharmendra.Singh
 */
public interface CommandExecutor {
    CommandResult execute(Execution execution, Command command);

}
