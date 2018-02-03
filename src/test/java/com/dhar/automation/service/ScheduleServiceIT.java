package com.dhar.automation.service;

import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.common.SortOrder;
import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.dto.PaginationDTO;
import com.dhar.automation.dto.ScheduleDTO;
import com.dhar.automation.dto.ScheduleStatusDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author Dharmendra.Singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class ScheduleServiceIT {

    @Resource
    ScheduleService scheduleService;

    @Resource
    ExecutionListService executionListService;

    @Test
    public void findScheduleByEnvironment_shouldReturnListOfSchedulesForEnvironmentId(){
        PaginatedList<ScheduleDTO> paginatedList = scheduleService.findScheduleByEnvironment(1l, new PaginationDTO(0, 100, SortOrder.DESC));//latest schedule should come first
        System.out.println(paginatedList.count);
    }

    @Test
    public void findScheduleStatusById_shouldReturnScheduleStatus(){
        scheduleService.findScheduleStatusNew(50l);
    }

    @Test
    public void findScheduleByTestCaseEnvironment_shouldReturnListOfSchedulesForTestCaseEnvironmentId(){
        PaginatedList<ScheduleDTO> paginatedList = scheduleService.findScheduleByTestCaseAndEnvironment(1l, 1l, new PaginationDTO(0, 100, SortOrder.DESC));//latest schedule should come first
        System.out.println(paginatedList.count);
    }

    @Test
    public void findScheduleStatusByTestCaseScheduleId_shouldReturnListOfScheduleStatusForTestCaseScheduleId(){
        ScheduleStatusDTO statusDTO = scheduleService.findScheduleStatusTestCase(1l, 31563852l);
        System.out.println(statusDTO);
    }

    @Test
    public void findExecutionList(){
        ExecutionList executionList = executionListService.getExecutionList(50l);
        System.out.println(executionList.getTestCase());
    }
}
