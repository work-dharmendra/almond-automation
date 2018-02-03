package com.dhar.automation.service;

import com.dhar.automation.command.CommandStatus;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.domain.*;
import com.dhar.automation.dto.ScheduleStatusDTO;
import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.dto.TestCaseExecutionStatusDTO;
import com.dhar.automation.service.TestCaseService;
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
public class TestCaseServiceIT {

    @Resource
    TestCaseService testCaseService;

    @Test
    public void createTestCase_shouldCreateTestCaseSuccessfully(){
        TestCase testCase = new TestCase();
        testCase.setName("test ->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        testCase.setNeedResource(false);
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "${baseurl}/test/login.html"));
        commands.add(new Command("waitForElementPresent", "id:userName", null));
        commands.add(new Command("type", "id:userName", "dharmendra.chouhan@synechron.com"));
        commands.add(new Command("type", "id:password", "welcome"));
        commands.add(new Command("click", "id:submit", null));
        commands.add(new Command("waitForElementPresent", "id:content", null));
        commands.add(new Command("verifyTextPresent", "id:content", "This is home page"));
        testCase.setCommands(commands);
        testCase.setProject(new Project(1l));
        MapEntityDto.buildTestCaseDTO(testCase);
        testCaseService.createTestCase(MapEntityDto.buildTestCaseDTO(testCase));
    }

    @Test
    public void updateTestCase_shouldUpdateTestCaseSuccessfully(){
        TestCase testCase = new TestCase(1l);
        testCase.setName("test");
        List<Command> commands = new ArrayList<>();
        commands.add(new Command("open", null, "${baseurl}/login.html"));
        commands.add(new Command("waitForElementPresent", "id=userName", null));
        commands.add(new Command("type", "id=userName", "dharmendra.chouhan@synechron.com"));
        commands.add(new Command("type", "id=password", "welcome"));
        commands.add(new Command("click", "id=submit", null));
        commands.add(new Command("waitForElementPresent", "id=content", null));
        commands.add(new Command("verifyTextPresent", "id=content", "This is home page"));
        testCase.setCommands(commands);
        testCase.setProject(new Project(1l));

        testCaseService.updateTestCase(MapEntityDto.buildTestCaseDTO(testCase));
    }

    @Test
    public void saveTestCaseExecution_shouldSaveTestCaseExecutionSuccessfully(){


        TestCaseExecution testCaseExecution = new TestCaseExecution(new ExecutionList(1l), null, CommandStatus.PASS, "100", null, null, null);

        testCaseService.saveTestCaseExecution(testCaseExecution);

    }

    @Test
    public void findTestCaseExecution_shouldReturnTestCaseExecutionSuccessfully(){
        /*List<TestCaseExecution> testCaseExecutions = testCaseDao.findTestCaseExecution(1l, 1l);
        System.out.println(testCaseExecutions.size());*/
    }

    @Test
    public void findTestCaseExecution_shouldReturnTestCaseExecutionForGivenScheduleIdAndTestCaseId(){
        /*List<TestCaseExecution> testCaseExecutions = testCaseDao.findTestCaseExecution(24620548l, 2l);

        for(TestCaseExecution testCaseExecution : testCaseExecutions){
            System.out.println(testCaseExecution);
        }*/
    }

    @Test
    public void findSchedule_shouldReturnScheduleForTestCaseSuccessfully(){
        /*List<Schedule> schedules = testCaseDao.findTestCaseScheduleStatus(1l);
        for(Schedule schedule : schedules){
            System.out.println(schedule);
        }*/
    }

    @Test
    public void findTestSuiteLastScheduleStatus_shouldReturnLastTestSuiteScheduleStatusSuccessfully(){
        final Gson gson1 = new GsonBuilder().create();
        final Gson gson = new GsonBuilder().registerTypeAdapter(TestCaseExecutionStatusDTO.class, new JsonSerializer<TestCaseExecutionStatusDTO>() {
            @Override
            public JsonElement serialize(TestCaseExecutionStatusDTO testCaseExecutionStatusDTO, Type type, JsonSerializationContext jsonSerializationContext) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("scheduledId", testCaseExecutionStatusDTO.scheduleId);
                jsonObject.addProperty("testCaseId", testCaseExecutionStatusDTO.testCaseId);
                JsonArray jsonArray = new JsonArray();

                for(String s : testCaseExecutionStatusDTO.status){
                    jsonArray.add(gson1.fromJson(s, JsonObject.class));
                }

                jsonObject.add("steps", jsonArray);

                return jsonObject;
            }


        }).create();
        ScheduleStatusDTO scheduleStatusDTO = testCaseService.findTestSuiteLastScheduleStatus(3l);
        System.out.println(gson.toJson(scheduleStatusDTO));
    }

}
