package com.dhar.automation.service;

import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.Project;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestCaseExecution;
import com.dhar.automation.dto.EnvironmentDTO;
import com.dhar.automation.dto.ScheduleStatusDTO;
import com.dhar.automation.dto.TestCaseExecutionStatusDTO;
import com.google.gson.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class EnvironmentServiceIT {

    @Resource
    EnvironmentService environmentService;

    @Test
    public void findAllEnvironment_shouldReturnListOfAllEnvironment(){
        PaginatedList<EnvironmentDTO> paginatedList = environmentService.findAllEnvironment();

        System.out.println(new GsonBuilder().create().toJson(paginatedList));
    }
}
