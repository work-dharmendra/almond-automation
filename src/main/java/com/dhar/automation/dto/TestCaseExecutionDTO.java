package com.dhar.automation.dto;

import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.domain.TestCaseExecution;

import java.util.Date;

/**
 * @author Dharmendra Chouhan
 */
public class TestCaseExecutionDTO {

    public Long id;
    public CommandStatus status;
    public String timeTaken;
    public String errorMessage;
    public Date whenCreated;
    public String screenshot;
    public String command;
    public String comment;

    public TestCaseExecutionDTO() {
    }

    public TestCaseExecutionDTO(TestCaseExecution testCaseExecution){
        this.id = testCaseExecution.getId();
        this.status = testCaseExecution.getStatus();
        this.command = testCaseExecution.getCommand();
        this.timeTaken = testCaseExecution.getTimeTaken();
        this.errorMessage = testCaseExecution.getErrorMessage();
        this.whenCreated = testCaseExecution.getWhenCreated();
        this.comment = testCaseExecution.getComment();
        this.screenshot = testCaseExecution.getScreenshot();
    }
}
