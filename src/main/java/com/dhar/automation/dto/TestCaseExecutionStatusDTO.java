package com.dhar.automation.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This dto represents complete steps of execution for given testcase and scheduleid.
 */
public class TestCaseExecutionStatusDTO {
    public Long scheduleId;
    public Long testCaseId;
    public String name;
    public List<String> status =  new ArrayList<>();
    public List<String> screenshots = new ArrayList<>();

    @Override
    public String toString() {
        return "TestCaseExecutionStatusDTO{" +
                "scheduleId=" + scheduleId +
                ", testCaseId=" + testCaseId +
                ", name='" + name + '\'' +
                ", status=" + status.size() +
                '}';
    }
}
