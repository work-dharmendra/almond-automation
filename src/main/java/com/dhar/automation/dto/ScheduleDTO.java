package com.dhar.automation.dto;

import com.dhar.automation.RunType;
import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.domain.Schedule;
import com.dhar.automation.domain.Status;

import java.util.*;

/**
 * @author Dharmendra.Singh
 */
public class ScheduleDTO {
    public ScheduleDTO() {
    }

    public ScheduleDTO(Long id, Long scheduleId, String name, Status status, Long environmentId, RunType runType, String testCases, Date scheduleDate) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.name = name;
        this.status = status;
        this.environmentId = environmentId;
        this.runType = runType;
        this.testCases = testCases;
        this.scheduleDate = scheduleDate;
    }

    public Long id;
    public Long scheduleId;
    public EnvironmentDTO environment;
    public int totalTestCase;
    public int totalNotStartedTestCase;
    public int totalPassTestCase;
    public int totalFailTestCase;
    public int totalInProgressCase;
    public String gridUrl;
    public Map<String, String> params;
    public List<ExecutionListDTO> executionList = new ArrayList<>();

    //convert single schedule(tabular structure) into tree like structure
    public ScheduleDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.scheduleId = schedule.getScheduleId();
        this.gridUrl = schedule.getGridUrl();
        this.params = schedule.getParamsMap();

        this.environment = new EnvironmentDTO(schedule.getEnvironment());

        List<ExecutionList> executionLists = schedule.getExecutionList();

        if (executionLists != null && executionLists.size() > 0) {

            for (ExecutionList executionList : executionLists) {
                //check if this execution list is executed from other testcase or testsuite, if yes then find parent and
                //this executionlist into parent executiondtolist
                if (executionList.getParent() != null && executionList.getParent() != 0) {
                    ExecutionListDTO executionListDTO = findExecutionListDTO(this.executionList, executionList.getParent());

                    List<ExecutionListDTO> executionListDTOList = executionListDTO.executionListDTOList;

                    if (executionListDTOList == null) {
                        executionListDTOList = new ArrayList<>();
                        executionListDTO.executionListDTOList = executionListDTOList;
                    }

                    executionListDTOList.add(new ExecutionListDTO(executionList));

                } else {
                    this.executionList.add(new ExecutionListDTO(executionList));
                }
            }

            //Currently user can either schedule testcase or testsuite, if in future user can mix testcase and testsuite
            //then we need to change it
            //set status of TestSuite by iterating all children of testsuite,
            //set pass only if all testcases are pass, set fail if any one is fail,
            //set to in progress if anyone is still in progress
            for (ExecutionListDTO executionList : this.executionList) {
                if (executionList.testSuite != null) {
                    executionList.timeTaken = getTestCaseTimeTaken(executionList);
                    List<Status> statusList = new ArrayList<>();
                    getTestSuiteStatus(executionList, statusList);
                    Status executionStatus = null;
                    if (statusList.size() > 0) {
                        for (Status status : statusList) {
                            if (status == Status.INPROGRESS || status == Status.NOTSTARTED) {
                                executionStatus = status;
                                break;
                            }
                        }

                        if (executionStatus == null) {
                            for (Status status : statusList) {
                                if (status == Status.FAIL) {
                                    executionStatus = status;
                                    break;
                                }
                            }
                        }

                        if (executionStatus == null) {
                            int totalPass = 0;
                            for (Status status : statusList) {
                                if (status == Status.PASS) {
                                    totalPass++;
                                }
                            }

                            if (totalPass == statusList.size()) {
                                executionStatus = Status.PASS;
                            }
                        }
                    }

                    executionList.status = executionStatus;
                }

            }
        }

        //iterate and set time, need to refactor
        if (this.executionList != null && this.executionList.size() > 0) {
            for (ExecutionListDTO executionListDTO : this.executionList) {
                iterateExecutionList(executionListDTO);
            }
        }

    }

    private void iterateExecutionList(ExecutionListDTO executionList) {
        if (executionList.testCase != null && executionList.testCaseExecutionDTOList != null && executionList.testCaseExecutionDTOList.size() > 0) {
            long timeTaken = 0;
            for (TestCaseExecutionDTO testCaseExecutionDTO : executionList.testCaseExecutionDTOList) {
                timeTaken = timeTaken + Long.parseLong(testCaseExecutionDTO.timeTaken);
            }

            executionList.timeTaken = timeTaken;
        }
        if (executionList.executionListDTOList != null && executionList.executionListDTOList.size() > 0) {
            for (ExecutionListDTO childExecutionList : executionList.executionListDTOList) {
                iterateExecutionList(childExecutionList);
            }
        }
    }

    private long getTestCaseTimeTaken(ExecutionListDTO executionList) {
        long timeTaken = 0;
        if (executionList.testCaseExecutionDTOList != null && executionList.testCaseExecutionDTOList.size() > 0) {
            for (TestCaseExecutionDTO testCaseExecutionDTO : executionList.testCaseExecutionDTOList) {
                timeTaken = timeTaken + Long.parseLong(testCaseExecutionDTO.timeTaken);
            }
        }

        if (executionList.executionListDTOList != null && executionList.executionListDTOList.size() > 0) {
            for (ExecutionListDTO childExecutionList : executionList.executionListDTOList) {
                timeTaken = timeTaken + getTestCaseTimeTaken(childExecutionList);
            }
        }

        return timeTaken;
    }

    private void getTestSuiteStatus(ExecutionListDTO executionList, List<Status> statusList) {
        if (executionList.testSuite != null) {
            for (ExecutionListDTO list : executionList.executionListDTOList) {
                if (list.testCase != null) {
                    statusList.add(list.status);
                } else {
                    getTestSuiteStatus(list, statusList);
                }

            }
        }
    }

    private ExecutionListDTO findExecutionListDTO(List<ExecutionListDTO> executionList, Long id) {
        for (ExecutionListDTO executionListDTO : executionList) {
            if (executionListDTO.id == id) {
                return executionListDTO;
            }

            if (executionListDTO.executionListDTOList != null && executionListDTO.executionListDTOList.size() > 0) {
                return findExecutionListDTO(executionListDTO.executionListDTOList, id);
            }
        }

        return null;
    }

    public String name;//testcase or testsuitename
    public Status status;
    public Long environmentId;
    public RunType runType;
    public String testCases;
    public Date scheduleDate;

    public ScheduleDTO(Long id, Long scheduleId, Long envId, RunType runType, String testCases, Date scheduleDate) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.environmentId = envId;
        this.runType = runType;
        this.testCases = testCases;
        this.scheduleDate = scheduleDate;
    }
}
