package com.dhar.automation;

import java.util.List;
import java.util.Map;

/**
 * @author dharmendra.singh
 */
public class Status {
    public Long scheduleId;
    public Long id;
    public String type; //testcase or suite
    public String name;
    public List<TestStatus> testStatuses;

    public void updateStatus(Long id, Map<String, String> newrow){
        TestStatus testStatus = getTestCaseStatus(id);
        testStatus.statusDetails.add(newrow);
    }

    private TestStatus getTestCaseStatus(Long id){
        TestStatus testStatus = new TestStatus(id);
        if(testStatuses.contains(testStatus)){
            return testStatuses.get(testStatuses.indexOf(testStatus));
        }
        return null;
    }
}

class TestStatus {
    public Long id;
    public List<Map<String, String>> statusDetails;

    public TestStatus(Long id) {
        this.id = id;
    }

    public TestStatus() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestStatus that = (TestStatus) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
