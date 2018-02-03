package com.dhar.automation.service;

import com.dhar.automation.RunType;
import com.dhar.automation.command.CommandFactory;
import com.dhar.automation.common.*;
import com.dhar.automation.dao.TestCaseDao;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.Schedule;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestCaseExecution;
import com.dhar.automation.dto.CommandDTO;
import com.dhar.automation.dto.ScheduleStatusDTO;
import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.dto.TestCaseExecutionStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class TestCaseService {
    private static Logger LOG = LoggerFactory.getLogger(TestCaseService.class);

    @Autowired
    TestCaseDao testCaseDao;

    @Resource
    CommandFactory commandFactory;

    public TestCaseDTO createTestCase(TestCaseDTO dto){

        dto.id = null;
        TestCase testCase = MapEntityDto.buildTestCase(dto);
        List<Long> testCaseIdList = new ArrayList<>();
        if(testCaseDao.cycleExist(testCase, testCaseIdList)){
            throw new RuntimeException("a testcase cannot include/invoke testcase which is already added by parent to avoid infinite testcase.");
        }

        TestCase savedTestCase = testCaseDao.createTestCase(testCase);

        return MapEntityDto.buildTestCaseDTO(savedTestCase);
    }

    public TestCaseDTO updateTestCase(TestCaseDTO dto){

        TestCase testCase = MapEntityDto.buildTestCase(dto);

        List<Long> testCaseIdList = new ArrayList<>();
        testCaseIdList.add(testCase.getId());
        if(testCaseDao.cycleExist(testCase, testCaseIdList)){
            System.out.println("cycle");
            throw new RuntimeException("a testcase cannot include/invoke testcase which is already added by parent to avoid infinite testcase.");
        }

        testCaseDao.updateTestCase(testCase);

        return MapEntityDto.buildTestCaseDTO(testCase);
    }

    public TestCaseDTO findTestCase(Long id){
        TestCase testCase = testCaseDao.findTestCase(id);

        TestCaseDTO testCaseDTO = MapEntityDto.buildTestCaseDTO(testCase);

        if (testCaseDTO != null && testCaseDTO.commands.size() > 0 ) {
            for(CommandDTO command : testCaseDTO.commands){
                if(command.name.equals("include") || command.name.equals("invoke")){
                    command.testCaseName = testCaseDao.findTestCase(Long.valueOf(command.value)).getName();
                }
            }
        }
        return testCaseDTO;
    }

    public void saveTestCaseExecution(TestCaseExecution testCaseExecution){
        //LOG.debug("saving testcaseexecution : scheduleId = {}, executionListID = {}", testCaseExecution.ge);
        testCaseDao.saveTestCaseExecution(testCaseExecution);
    }

    public void saveScheduleStatus(Schedule schedule) {
        testCaseDao.saveScheduleStatus(schedule);
    }

    public Set<TestCase> findTestCaseByTestSuiteId(Long testSuiteId) {
        Set<TestCase> testCases = testCaseDao.findTestCaseByTestSuiteId(testSuiteId);
        //testCases.size();
        for(TestCase testCase : testCases){
            testCase.getCommands().size();
            testCase.getCommands().get(0);
        }
        HibernateDetachUtility.deProxy(testCases);
        return testCases;
    }

    public ScheduleStatusDTO findTestSuiteLastScheduleStatus(Long testSuiteId){
        List<Schedule> schedules = testCaseDao.findTestSuiteScheduleStatus(testSuiteId);
        ScheduleStatusDTO scheduleStatusDTO = new ScheduleStatusDTO();
        if (schedules.size() > 0) {
            Schedule schedule = schedules.get(0);
            scheduleStatusDTO.testCaseSuiteId = testSuiteId;
            scheduleStatusDTO.scheduleId = schedule.getScheduleId();
            scheduleStatusDTO. runType = RunType.TESTSUITE;
            scheduleStatusDTO.testCasesIds = schedule.getTestCases();
            List<TestCaseExecutionStatusDTO> testCaseExecutionStatusDTOs = new ArrayList<>();
            scheduleStatusDTO.executionStatus = testCaseExecutionStatusDTOs;
            for(String testCaseId : scheduleStatusDTO.testCasesIds.split(",")){
                List<TestCaseExecution> testCaseExecutions = testCaseDao.findTestCaseExecution(schedule.getScheduleId(), Long.valueOf(testCaseId));
                testCaseExecutionStatusDTOs.add(MapEntityDto.buildTestCaseExecutionStatusDTO(testCaseExecutions));
            }
        }

        return scheduleStatusDTO;
    }

    public PaginatedList<TestCaseDTO> findTestCasesByProject(Long projectId){
        List<TestCase> testCaseList = testCaseDao.findTestCasesByProject(projectId);
        List<TestCaseDTO> testCaseDTOs = new ArrayList<>();

        if(testCaseList != null && testCaseList.size() > 0){
            HibernateUtil hibernateUtil = new HibernateUtil();
            for(TestCase testCase : testCaseList){
                testCaseDTOs.add(MapEntityDto.buildTestCaseDTO(hibernateUtil.deProxy(testCase)));
            }
        }

        return new PaginatedList<>(1, testCaseDTOs.size(), testCaseDTOs, testCaseDTOs.size());
    }

    public List<String> getAllCommandName(){
        List<String> commandNames = commandFactory.getAllCommandName();
        commandNames.add(Constants.COMMAND_INCLUDE);
        commandNames.add(Constants.COMMAND_INVOKE);
        Collections.sort(commandNames);
        return commandNames;
    }

    public List<TestCaseDTO> findIncludableTestCase(Long projectId, Long testCaseId, PaginatedRequest paginatedRequest){
        List<TestCaseDTO> testCaseDTOList = new ArrayList<>();
        List<TestCase> testCaseList = testCaseDao.findIncludableTestCase(projectId, testCaseId, paginatedRequest);

        if(testCaseList != null && testCaseList.size() > 0){
            for(TestCase testCase : testCaseList){
                testCaseDTOList.add(MapEntityDto.buildTestCaseDTO(testCase));
            }
        }
        return testCaseDTOList;
    }
}
