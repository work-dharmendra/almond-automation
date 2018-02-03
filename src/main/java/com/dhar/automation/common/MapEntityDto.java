package com.dhar.automation.common;

import com.dhar.automation.domain.*;
import com.dhar.automation.dto.*;
import com.gargoylesoftware.htmlunit.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dharmendra.Singh
 */
public class MapEntityDto {

    public static ProjectDTO buildProjectDTO(Project project){
        ProjectDTO dto = new ProjectDTO();
        dto.id = project.getId();
        dto.name = project.getName();
        dto.description = project.getDescription();

        if(project.getEnvironments() != null && project.getEnvironments().size() > 0){
            Set<EnvironmentDTO> environmentDTOs = new HashSet<>();
            dto.environments = environmentDTOs;
            for(Environment environment : project.getEnvironments()){
                environmentDTOs.add(MapEntityDto.buildEnvironmentDTO(environment));
            }
        }
        return dto;
    }

    public static Project buildProject(ProjectDTO dto){
        Project project = new Project();
        project.setId(dto.id);
        project.setName(dto.name);
        project.setDescription(dto.description);

        return project;
    }

    public static EnvironmentDTO buildEnvironmentDTO(Environment environment){
        EnvironmentDTO dto = new EnvironmentDTO();
        dto.id = environment.getId();
        dto.name = environment.getName();
        dto.description = environment.getDescription();
        dto.variables = environment.getVariables();

        if(environment.getProject() != null){
            dto.projectId = environment.getProject().getId();
            dto.projectName = environment.getProject().getName();
        }

        return dto;
    }

    public static Environment buildEnvironment(EnvironmentDTO dto){
        Environment environment = new Environment();
        environment.setId(dto.id);
        environment.setName(dto.name);
        environment.setDescription(dto.description);
        environment.setProject(new Project(dto.projectId));
        environment.setVariables(dto.variables);

        return environment;
    }

    public static TestCase buildTestCase(TestCaseDTO dto){
        TestCase testCase = new TestCase();
        testCase.setId(dto.id);
        testCase.setName(dto.name);
        testCase.setDescription(dto.description);
        testCase.setNeedResource(dto.isNeedResource);
        testCase.setProject(new Project(dto.projectId));
        List<Command> commands = new ArrayList<>();
        for(CommandDTO commandDTO : dto.commands){
            commands.add(buildCommand(commandDTO));
        }
        testCase.setCommands(commands);

        return testCase;

    }

    public static TestCaseDTO buildTestCaseDTO(TestCase testCase){
        TestCaseDTO dto = new TestCaseDTO();
        dto.id = testCase.getId();
        dto.name = testCase.getName();
        dto.description = testCase.getDescription();
        dto.isNeedResource = testCase.isNeedResource();
        dto.projectId = testCase.getProject().getId();

        List<CommandDTO> commandDTOs = new ArrayList<>();
        if (testCase.getCommands() != null && testCase.getCommands().size() > 0 ) {
            for(Command command : testCase.getCommands()){
                commandDTOs.add(buildCommandDTO(command));
            }
        }
        dto.commands = commandDTOs;

        return dto;
    }

    public static Command buildCommand(CommandDTO dto){
        Command command = new Command();
        command.setName(dto.name);
        command.setElement(dto.element);
        command.setParams(dto.params);
        command.setValue(dto.value);

        return command;
    }

    public static CommandDTO buildCommandDTO(Command command){
        if(command == null){
            return null;
        }
        CommandDTO dto = new CommandDTO();
        dto.name = command.getName();
        dto.element = command.getElement();
        dto.params = command.getParams();
        dto.value = command.getValue();

        return dto;
    }

    public static User buildUser(UserDTO dto){
        User user = new User();
        user.setId(dto.id);
        user.setUsername(dto.username);
        user.setPassword(dto.password);
        user.setUserType(dto.userType);
        user.setEnvironment(new Environment(dto.environmentId));

        return user;

    }

    public static UserDTO buildUserDTO(User user){
        UserDTO dto = new UserDTO();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.userType = user.getUserType();
        dto.environmentId = user.getEnvironment().getId();

        return dto;
    }

    //TODO this method need more testing
    public static TestSuiteDTO buildTestSuiteDTO(TestSuite testSuite){
        TestSuiteDTO dto = new TestSuiteDTO();

        dto.id = testSuite.getId();
        dto.name = testSuite.getName();
        dto.description = testSuite.getDescription();
        dto.projectId = testSuite.getProject().getId();

        Set<TestCaseDTO> testCaseDTOs = new HashSet<>();
        if(testSuite.getTestCases() != null && testSuite.getTestCases().size() > 0){
            for(TestCase testCase : testSuite.getTestCases()){
                testCaseDTOs.add(buildTestCaseDTO(testCase));
            }
        }
        dto.testCases = testCaseDTOs;

        Set<TestSuiteDTO> testSuiteDTOs = new HashSet<>();
        if(testSuite.getTestSuites() != null && testSuite.getTestSuites().size() > 0){
            for(TestSuite suite : testSuite.getTestSuites()){
                testSuiteDTOs.add(buildTestSuiteDTO(suite));
            }
        }
        dto.testSuites = testSuiteDTOs;

        return dto;
    }

    //TODO this method need more testing
    public static TestSuite buildTestSuite(TestSuiteDTO dto){
        TestSuite suite = new TestSuite();

        suite.setId(dto.id);
        suite.setName(dto.name);
        suite.setDescription(dto.description);
        suite.setProject(new Project(dto.projectId));

        Set<TestCase> testCases = new HashSet<>();
        if(dto.testCases != null){
            for(TestCaseDTO testCaseDTO : dto.testCases){
                testCases.add(buildTestCase(testCaseDTO));
            }
        }
        suite.setTestCases(testCases);

        Set<TestSuite> testSuites = new HashSet<>();
        if(dto.testSuites != null){
            for(TestSuiteDTO  testSuiteDTO : dto.testSuites){
                testSuites.add(buildTestSuite(testSuiteDTO));
            }
        }
        suite.setTestSuites(testSuites);

        return suite;
    }

    public static TestCaseExecutionStatusDTO buildTestCaseExecutionStatusDTO(List<TestCaseExecution> testCaseExecutions){
        TestCaseExecutionStatusDTO statusDTO = new TestCaseExecutionStatusDTO();
        List<String> status = new ArrayList<>();
        List<String> screenshots = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        for(TestCaseExecution testCaseExecution : testCaseExecutions){
            //statusDTO.testCaseId = testCaseExecution.getTestCase().getId();
            //statusDTO.scheduleId = testCaseExecution.getScheduleId();
            //statusDTO.name = testCaseExecution.getTestCase().getName();
            /*String stepStatus = testCaseExecution.getStatus();

            try{
                JsonObject json = gson.fromJson(stepStatus, JsonObject.class);
                if(testCaseExecution.getScreenshot() != null){
                    json.addProperty("screenshot", testCaseExecution.getScreenshot());
                }
                stepStatus = gson.toJson(json);
            } catch(Exception e){
                e.printStackTrace();
            }

            status.add(stepStatus);*/

        }
        //statusDTO.screenshots = screenshots;
        statusDTO.status = status;

        return statusDTO;
    }

    public static ScheduleDTO buildScheduleDTO(Schedule schedule){
        return new ScheduleDTO(schedule.getId(), schedule.getScheduleId(), schedule.getEnvironment().getId(), null, null, schedule.getScheduleDate());
    }
}
