package com.dhar.automation.command;

/**
 * @author Dharmendra.Singh
 */
public class CommandResult {
    public CommandStatus commandStatus;
    public String timeTaken;
    public String comment;
    public String screenshot;

    public CommandResult(CommandStatus commandStatus, String timeTaken, String comment) {
        this.commandStatus = commandStatus;
        this.timeTaken = timeTaken;
        this.comment = comment;
    }

    public CommandResult(CommandStatus commandStatus, String comment) {
        this.commandStatus = commandStatus;
        this.comment = comment;
    }

    public CommandResult(CommandStatus commandStatus) {
        this.commandStatus = commandStatus;
    }
}
