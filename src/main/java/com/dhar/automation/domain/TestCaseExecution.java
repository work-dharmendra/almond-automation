package com.dhar.automation.domain;

import com.dhar.automation.command.CommandStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dharmendra.singh
 */
@Entity
@Table(name = "testcaseexecution")
public class TestCaseExecution {

    public TestCaseExecution() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "execution_list_id")
    private ExecutionList executionList;

    /*@ManyToOne(optional = false)
    private TestCase testCase;*/

    @Enumerated(EnumType.STRING)
    private CommandStatus status;

    @Column(name = "command")
    private String command;

    @Column(name = "time_taken")
    private String timeTaken;

    @Column(name = "error_message", length = 5000)
    private String errorMessage;

    @Column(name = "comment", length = 5000)
    private String comment;

    @Column(length = 16777215)//16 mb
    private String screenshot;

    @Column(name = "when_created")
    private Date whenCreated;

    public TestCaseExecution(ExecutionList executionList, String command, CommandStatus status, String timeTaken, String comment, String errorMessage, String screenshot) {
        this.executionList = executionList;
        this.command = command;
        this.status = status;
        this.timeTaken = timeTaken;
        this.comment = comment;
        this.errorMessage = errorMessage;
        this.screenshot = screenshot;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TestCaseExecution{" +
                ", id=" + id +
                ", executionList.getId()='" + executionList.getId() + '\'' +
                ", command='" + command + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                '}';
    }
}
