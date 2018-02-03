package com.dhar.automation;

import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.domain.TestCase;
import org.openqa.selenium.WebDriver;

/**
 * @author Dharmendra.Singh
 */
public class Execution {

    private Long scheduledId;
    private TestCase testCase;
    private RunType type;
    private String testSuiteName;
    //private Environment environment;
    private RunConfig runConfig;
    private Long parentScheduleId;
    private ExecutionList executionList;

    private Long executionListId;
    private WebDriver webDriver;
    private String gridUrl;

    public Execution(ExecutionList executionList) {
        this.executionList = executionList;
    }



    public Execution() {
    }

    public Execution(Long executionListId) {
        this.executionListId = executionListId;
    }

    public Execution(Long scheduledId, TestCase testCase, RunType type, RunConfig runConfig) {
        this.scheduledId = scheduledId;
        this.testCase = testCase;
        this.type = type;
        this.runConfig = runConfig;
    }

    public ExecutionList getExecutionList() {
        return executionList;
    }

    public Execution(Long scheduledId, TestCase testCase, RunType type, RunConfig runConfig, Long parentScheduleId) {
        this.scheduledId = scheduledId;
        this.testCase = testCase;
        this.type = type;
        this.runConfig = runConfig;
        this.parentScheduleId = parentScheduleId;
    }

    public Long getScheduledId() {
        return scheduledId;
    }

    public void setScheduledId(Long scheduledId) {
        this.scheduledId = scheduledId;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public RunType getType() {
        return type;
    }

    public void setType(RunType type) {
        this.type = type;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public RunConfig getRunConfig() {
        return runConfig;
    }

    public void setRunConfig(RunConfig runConfig) {
        this.runConfig = runConfig;
    }

    public Long getParentScheduleId() {
        return parentScheduleId;
    }

    public void setParentScheduleId(Long parentScheduleId) {
        this.parentScheduleId = parentScheduleId;
    }

    public Long getExecutionListId() {
        return executionListId;
    }

    public void setExecutionListId(Long executionListId) {
        this.executionListId = executionListId;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getGridUrl() {
        return gridUrl;
    }

    public void setGridUrl(String gridUrl) {
        this.gridUrl = gridUrl;
    }

    /*public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }*/
}
