package com.dhar.automation.domain;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */
@Entity
@Table(name = "executionlist")
@NamedQueries({
        @NamedQuery(name = "updateParamsForExecutionList", query = "update ExecutionList set params=:params where id=:id")
})
public class ExecutionList {

    public ExecutionList() {
    }

    public ExecutionList(Long id) {
        this.id = id;
    }

    public ExecutionList(Schedule schedule, TestCase testCase, Long parentId, Integer commandIndex) {
        this.schedule = schedule;
        this.testCase = testCase;
        this.parent = parentId;
        this.commandIndex = commandIndex;
    }

    public ExecutionList(Schedule schedule, TestSuite testSuite, Long parentId, Integer commandIndex) {
        this.schedule = schedule;
        this.testSuite = testSuite;
        this.parent = parentId;
        this.commandIndex = commandIndex;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;

    @OneToOne
    @JoinColumn(name = "test_suite_id")
    private TestSuite testSuite;

    @Column(nullable = true)
    private Long parent;//this contains id of this table.

    //it is used when this testcase/testsuite is invoked, it contain index of command which uses invoke
    //command to schedule this testcase/testsuite.
    @Column(name = "command_index")
    private Integer commandIndex;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "custom_parameters")
    private String customParamters;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="executionlist_params", joinColumns=@JoinColumn(name="executionlist_id"))
    private Map<String, String> params;

    @OneToMany(mappedBy = "executionList")
    private List<TestCaseExecution> testCaseExecutionList;

    @Column(name = "when_created")
    private Date whenCreated = new Date();//created date

    @Column(name = "when_modified")
    private Date whenModified;

    @Column(name = "error_message", length = 5000)
    private String errorMessage;

    @Column(name = "when_complete")
    private Date whenComplete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public Integer getCommandIndex() {
        return commandIndex;
    }

    public void setCommandIndex(Integer commandIndex) {
        this.commandIndex = commandIndex;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<TestCaseExecution> getTestCaseExecutionList() {
        return testCaseExecutionList;
    }

    public void setTestCaseExecutionList(List<TestCaseExecution> testCaseExecutionList) {
        this.testCaseExecutionList = testCaseExecutionList;
    }

    public Date getWhenModified() {
        return whenModified;
    }

    public void setWhenModified(Date whenModified) {
        this.whenModified = whenModified;
    }

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getWhenComplete() {
        return whenComplete;
    }

    public void setWhenComplete(Date whenComplete) {
        this.whenComplete = whenComplete;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getCustomParamters() {
        return customParamters;
    }

    public void setCustomParamters(String customParamters) {
        this.customParamters = customParamters;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getParamsMap(){
        Map<String, String> result = new HashMap<>();
        if(this.customParamters == null){
            return result;
        }
        String[] params = this.customParamters.split(";");

        for (String param : params) {
            if (StringUtils.isNotEmpty(param)) {
                String[] variable = param.split("=");
                String key = variable[0];
                String value = variable[1];
                result.put(key, value);
            }

        }
        return result;
    }

    @Override
    public String toString() {
        return "ExecutionList{" +
                "id=" + id +
                ", schedule=" + schedule.getId() +
                ", commandIndex=" + commandIndex +
                ", status=" + status +
                ", customParamters=" + customParamters +
                ", whenCreated=" + whenCreated +
                ", whenModified=" + whenModified +
                ", errorMessage='" + errorMessage + '\'' +
                ", whenComplete=" + whenComplete +
                '}';
    }
}
