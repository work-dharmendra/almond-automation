package com.dhar.automation.dto;

import com.dhar.automation.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dharmendra Chouhan
 */
public class ExecutionListDTO {

    public Long id;
    public TestCaseDTO testCase;
    public TestSuiteDTO testSuite;
    public Long parent;//this contains id of this table.
    public Integer commandIndex;
    public Status status;
    public String customParamters;
    public List<TestCaseExecutionDTO> testCaseExecutionDTOList;
    public Date whenCreated;
    public Date whenModified;
    public String errorMessage;
    public Date whenComplete;
    public long timeTaken;

    public List<ExecutionListDTO>  executionListDTOList;


    public ExecutionListDTO(ExecutionList executionList) {
        this.id = executionList.getId();
        this.commandIndex = executionList.getCommandIndex();
        this.customParamters = executionList.getCustomParamters();
        this.errorMessage = executionList.getErrorMessage();
        this.status = executionList.getStatus();
        this.whenCreated = executionList.getWhenCreated();
        this.whenModified = executionList.getWhenModified();
        this.whenComplete = executionList.getWhenComplete();
        this.parent = executionList.getParent();

        TestCase testCase = executionList.getTestCase();
        if (testCase != null) {
            this.testCase = new TestCaseDTO(testCase.getId(), testCase.getName());
        }

        TestSuite testSuite = executionList.getTestSuite();
        if (testSuite != null) {
            this.testSuite = new TestSuiteDTO(testSuite.getId(), testSuite.getName());
        }

        List<TestCaseExecution> testCaseExecutionList = executionList.getTestCaseExecutionList();

        if(testCaseExecutionList != null && testCaseExecutionList.size() > 0){
            testCaseExecutionDTOList = new ArrayList<>();
            for(TestCaseExecution testCaseExecution : testCaseExecutionList){
                testCaseExecutionDTOList.add(new TestCaseExecutionDTO(testCaseExecution));
            }
        }
    }
}
