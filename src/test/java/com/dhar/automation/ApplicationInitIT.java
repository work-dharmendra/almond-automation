package com.dhar.automation;

import com.dhar.automation.common.Constants;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.domain.*;
import com.dhar.automation.dto.*;
import com.dhar.automation.service.*;
import com.dhar.automation.test.CreateSchema;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class ApplicationInitIT {

    @Resource
    ProjectService projectService;
    @Resource
    EnvironmentService environmentService;
    @Resource
    TestCaseService testCaseService;
    @Resource
    TestSuiteService testSuiteService;
    @Resource
    ConfigService configService;

    @org.junit.Test
    @Deprecated
    public void shouldPopulateBasicData(){
        CreateSchema.main(new String[]{});
        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.gridUrl = "http://localhost:4444/wd/hub";
        configService.addConfig(Constants.GRID_URL_KEY, settingsDTO.gridUrl);

        Project project = new Project();
        project.setName("test");
        project.setDescription("test");
        ProjectDTO projectDTO = projectService.createProject(MapEntityDto.buildProjectDTO(project));
        project.setId(projectDTO.id);
        Environment environment = new Environment();
        environment.setName("local");
        environment.setProject(project);
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "baseurl");
        jsonObject.addProperty("value", "http://localhost:9080/test2/");
        jsonArray.add(jsonObject);
        environment.setVariables(jsonArray.toString());

        EnvironmentDTO savedEnvironment = environmentService.createEnvironment(MapEntityDto.buildEnvironmentDTO(environment));

        TestCase testCaseInclude = new TestCase();
        testCaseInclude.setName("test-include");
        List<Command> commandsInclude = new ArrayList<>();
        commandsInclude.add(new Command("click", "id=submit", null));
        testCaseInclude.setCommands(commandsInclude);
        testCaseInclude.setProject(project);

        TestCaseDTO testCaseDTOInclude = testCaseService.createTestCase(MapEntityDto.buildTestCaseDTO(testCaseInclude));

        TestCase testCase1 = new TestCase();
        testCase1.setName("test1");
        List<Command> commands1 = new ArrayList<>();
        commands1.add(new Command("open", null, "${baseurl}login.html"));
        commands1.add(new Command("waitForElementPresent", "id=userName", null));
        commands1.add(new Command("type", "id=userName", "{username}"));
        commands1.add(new Command("include", null, String.valueOf(testCaseDTOInclude.id)));
        testCase1.setCommands(commands1);
        testCase1.setProject(project);

        TestCaseDTO testCaseDTO = testCaseService.createTestCase(MapEntityDto.buildTestCaseDTO(testCase1));

        TestCase testCasAdv = new TestCase();
        testCasAdv.setName("test-adv");
        List<Command> commandsAdv = new ArrayList<>();
        commandsAdv.add(new Command("open", null, "${baseurl}advertiser.html"));
        commandsAdv.add(new Command("waitForElementPresent", "id=advertiser", null));
        commands1.add(new Command("type", "id=userName", "{username}"));
        testCasAdv.setCommands(commandsAdv);
        testCasAdv.setProject(project);

        TestCaseDTO testCaseAdvDTO = testCaseService.createTestCase(MapEntityDto.buildTestCaseDTO(testCasAdv));

        TestCase testCase2 = new TestCase();
        testCase2.setName("test-invoke");
        List<Command> commands2 = new ArrayList<>();
        commands2.add(new Command("open", null, "${baseurl}login.html"));
        commands2.add(new Command("waitForElementPresent", "id=userName", null));
        commands2.add(new Command("type", "id=password", "welcome"));
        commands2.add(new Command("invoke", null, String.valueOf(testCaseAdvDTO.id)));
        testCase2.setCommands(commands2);
        testCase2.setProject(project);

        TestCaseDTO testCaseDTO2 = testCaseService.createTestCase(MapEntityDto.buildTestCaseDTO(testCase2));

        TestSuite suite = new TestSuite();
        suite.setName("testSuite");
        suite.getTestCases().add(new TestCase(testCaseDTO2.id));
        TestSuiteDTO testSuiteDTO = new TestSuiteDTO();
        testSuiteDTO.name = "testsuite";
        Set<TestCaseDTO> testCaseDTOs = new HashSet<>();
        testCaseDTOs.add(testCaseDTO2);
        testSuiteDTO.testCases = testCaseDTOs;
        testSuiteDTO.projectId = projectDTO.id;
        testSuiteService.createTestSuite(testSuiteDTO);


    }
}
