package com.dhar.automation.service;

import com.dhar.automation.RunType;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.dao.ScheduleDao;
import com.dhar.automation.dao.TestCaseDao;
import com.dhar.automation.domain.Schedule;
import com.dhar.automation.domain.TestCaseExecution;
import com.dhar.automation.dto.PaginationDTO;
import com.dhar.automation.dto.ScheduleDTO;
import com.dhar.automation.dto.ScheduleStatusDTO;
import com.dhar.automation.dto.TestCaseExecutionStatusDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
@Service
public class ScheduleService {

    private static Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    ScheduleDao scheduleDao;
    @Resource
    TestCaseDao testCaseDao;

    @Resource
    ScheduleService scheduleService;

    public PaginatedList<ScheduleDTO> findScheduleByEnvironment(Long environmentId, PaginationDTO paginationDTO) {
        LOG.debug("getting schedules for environment = {}, start = {}, pagesize = {}", environmentId, paginationDTO.start, paginationDTO.pageSize);
        PaginatedList<Schedule> scheduleList = scheduleDao.findScheduleByEnvironment(environmentId, paginationDTO);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        //scheduleDao.findScheduleByEnvironment is returning duplicate data, create list and do not send duplicate records to ui.
        //TODO fix dao function
        List<Long> scheduleIdList = new ArrayList<>();
        if (scheduleList.count != 0) {
            LOG.debug("schedules found for environment = {}, start = {}, pagesize = {}, count = {}", environmentId
                    , paginationDTO.start, paginationDTO.pageSize, scheduleList.count);
            for (Schedule schedule : scheduleList.list) {
                if(!scheduleIdList.contains(schedule.getScheduleId())){
                    scheduleIdList.add(schedule.getScheduleId());
                    scheduleDTOs.add(MapEntityDto.buildScheduleDTO(schedule));
                }

            }
        } else {
            LOG.debug("NO schedules found for environment = {}, start = {}, pagesize = {}", environmentId, paginationDTO.start, paginationDTO.pageSize);
        }

        return new PaginatedList<>(paginationDTO.start, paginationDTO.pageSize, scheduleDTOs, scheduleDTOs.size());
    }

    public PaginatedList<ScheduleDTO> findScheduleByTestCaseAndEnvironment(Long testCaseId, Long environmentId, PaginationDTO paginationDTO) {
        LOG.debug("getting schedules for testcase = {}, environment = {}, start = {}, pagesize = {}"
                , testCaseId, environmentId, paginationDTO.start, paginationDTO.pageSize);
        PaginatedList<Schedule> scheduleList = scheduleDao.findScheduleByTestCaseAndEnvironment(testCaseId, environmentId, paginationDTO);
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        if (scheduleList.count != 0) {
            LOG.debug("schedules found for testcase = {}, environment = {}, start = {}, pagesize = {}, count = {}",
                    testCaseId, environmentId, paginationDTO.start, paginationDTO.pageSize, scheduleList.count);
            for (Schedule schedule : scheduleList.list) {
                scheduleDTOs.add(MapEntityDto.buildScheduleDTO(schedule));
            }
        } else {
            LOG.debug("NO schedules found for testcase = {}, environment = {}, start = {}, pagesize = {}",testCaseId, environmentId, paginationDTO.start, paginationDTO.pageSize);
        }

        return new PaginatedList<>(paginationDTO.start, paginationDTO.pageSize, scheduleDTOs, scheduleList.count);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public ScheduleStatusDTO findScheduleStatus(Long scheduleId) {
        LOG.debug("getting schedule information for scheduleid = {}", scheduleId);
        /*List<Schedule> schedules = testCaseDao.findSchedule(scheduleId);
        if (schedules == null || schedules.size() == 0 ) {
            LOG.error("No schedule found for given id = {}", scheduleId);
            throw new RuntimeException("no schedule found for given id");
        }
        ScheduleStatusDTO statusDTO = new ScheduleStatusDTO();
        statusDTO.scheduleId = scheduleId;
        LOG.debug("schedule found for scheduleid = {}", scheduleId);

        List<TestCaseExecutionStatusDTO> executionStatusDTOs = new ArrayList<>();
        statusDTO.executionStatus = executionStatusDTOs;
        StringBuilder testCaseIds = new StringBuilder();*/
        /*for (Schedule schedule : schedules) {
            if(schedule.getRunType() == RunType.TESTSUITE){
                LOG.debug("getting testCaseExecution for scheduleid = {}, testsuite = {}", scheduleId, schedule.getTestCaseSuiteId());
                String [] testCases = schedule.getTestCases().split(",");
                for(String testCaseId : testCases){
                    List<TestCaseExecution> testCaseExecution = testCaseDao.findTestCaseExecution(scheduleId, Long.parseLong(testCaseId));
                    TestCaseExecutionStatusDTO testCaseExecutionStatusDTO = MapEntityDto.buildTestCaseExecutionStatusDTO(testCaseExecution);
                    executionStatusDTOs.add(testCaseExecutionStatusDTO);
                }
                if(testCaseIds.toString().equals("")){
                    testCaseIds.append(schedule.getTestCases());
                } else {
                    testCaseIds.append("," + schedule.getTestCases());
                }
            } else {
                LOG.debug("getting testCaseExecution for scheduleid = {}, testcase = {}", schedule.getScheduleId(), schedule.getTestCaseSuiteId());
                List<TestCaseExecution> testCaseExecution = testCaseDao.findTestCaseExecution(schedule.getScheduleId(), schedule.getTestCaseSuiteId());
                executionStatusDTOs.add(MapEntityDto.buildTestCaseExecutionStatusDTO(testCaseExecution));
                if(testCaseIds.toString().equals("")){
                    testCaseIds.append(schedule.getTestCaseSuiteId());
                } else {
                    testCaseIds.append("," + schedule.getTestCaseSuiteId());
                }
            }

        }
        statusDTO.testCasesIds = testCaseIds.toString();*/

        //ScheduleStatusDTO statusDTO = scheduleService.findScheduleStatusNew(scheduleId);

        return null;
    }

    public ScheduleStatusDTO findScheduleStatusTestCase(Long testCaseId, Long scheduleId) {
        LOG.debug("getting schedule information for testCase = {}, scheduleid = {}", testCaseId, scheduleId);
        Schedule schedule = testCaseDao.findScheduleByTestCaseId(testCaseId, scheduleId);
        if (schedule == null || schedule.getId() == null) {
            LOG.error("No schedule found for given testcase = {}, id = {}", testCaseId, scheduleId);
            throw new RuntimeException("no schedule found for given testcase and scheduleid");
        }
        ScheduleStatusDTO statusDTO = new ScheduleStatusDTO();
        statusDTO.scheduleId = scheduleId;
        statusDTO.testCasesIds = schedule.getTestCases();
        statusDTO.testCaseSuiteId = testCaseId;
        statusDTO.runType = RunType.TESTCASE;
        LOG.debug("schedule found for scheduleid = {}, testcases = {}", scheduleId, schedule.getTestCases());

        List<TestCaseExecutionStatusDTO> executionStatusDTOs = new ArrayList<>();
        statusDTO.executionStatus = executionStatusDTOs;
        List<TestCaseExecution> testCaseExecution = testCaseDao.findTestCaseExecution(scheduleId, testCaseId);
        executionStatusDTOs.add(MapEntityDto.buildTestCaseExecutionStatusDTO(testCaseExecution));

        return statusDTO;
    }

    public ScheduleDTO findScheduleStatusNew(Long scheduleId){
        PaginatedList<Schedule> statusDTOPaginatedList = scheduleDao.findSchedule(scheduleId);

        Schedule schedule = statusDTOPaginatedList.list.get(0);
        ScheduleDTO scheduleDTO = new ScheduleDTO(schedule);

        ScheduleDTO scheduleStats = scheduleDao.findScheduleStats(scheduleId);

        scheduleDTO.totalTestCase = scheduleStats.totalTestCase;
        scheduleDTO.totalPassTestCase = scheduleStats.totalPassTestCase;
        scheduleDTO.totalFailTestCase = scheduleStats.totalFailTestCase;
        scheduleDTO.totalInProgressCase = scheduleStats.totalInProgressCase;
        scheduleDTO.totalNotStartedTestCase = scheduleStats.totalNotStartedTestCase;

        return scheduleDTO;
    }
}
