package com.dhar.automation.controller;

import com.dhar.automation.Configuration;
import com.dhar.automation.common.Constants;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.common.PaginatedRequest;
import com.dhar.automation.common.SortOrder;
import com.dhar.automation.database.LiquibaseMigration;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestSuite;
import com.dhar.automation.dto.*;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.service.*;
import com.dhar.automation.testrunner.TestScheduler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Main controller of project, contains all api.
 * Created by Dharmendra.Singh on 4/16/2015.
 */
@RestController
public class UIController  extends ResponseEntityExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(UIController.class);

    @Autowired
    TestCaseService testCaseService;

    @Autowired
    TestSuiteService testSuiteService;

    @Autowired
    ProjectService projectService;

    @Autowired
    EnvironmentService environmentService;

    @Autowired
    TestScheduler testScheduler;

    @Autowired
    ScheduleService scheduleService;

    @Resource
    LiquibaseMigration liquibaseMigration;

    @Resource
    ConfigService configService;

    @Resource
    DebugSessionService debugSessionService;

    @RequestMapping(value = "testcase", method = RequestMethod.POST)
    public String saveTestCase(final HttpServletResponse response, @RequestBody TestCaseDTO dto) {
        TestCaseDTO savedTestCase = testCaseService.createTestCase(dto);

        try {
            Gson gson = new GsonBuilder().create();

            response.getWriter().write(gson.toJson(savedTestCase));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @RequestMapping(value = "project/{id}", method = RequestMethod.PUT)
    public ProjectDTO updateProject(@PathVariable Long id, @RequestBody ProjectDTO project) {
        project.id = id;
        projectService.updateProject(project);
        return projectService.findProject(id);
    }

    @RequestMapping(value = "project", method = RequestMethod.POST)
    public ProjectDTO createProject(@RequestBody ProjectDTO project) {
        return projectService.createProject(project);
    }

    @RequestMapping(value = "project/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ProjectDTO getProject(@PathVariable Long id) {
        return projectService.findProject(id);
    }

    @RequestMapping(value = "project", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectDTO> getAllProject() {
        System.out.println("DDDDD+++");
        return projectService.findAllProjects();
    }

    @RequestMapping(value = "environment", method = RequestMethod.GET)
    @ResponseBody
    public PaginatedList<EnvironmentDTO> getAllEnvironment(final HttpServletResponse response) {
        return environmentService.findAllEnvironment();
    }

    @RequestMapping(value = "project/{projectId}/testcase", method = RequestMethod.GET)
    @ResponseBody
    public PaginatedList<TestCaseDTO> getTestCasesForProject(@PathVariable Long projectId) {
        return testCaseService.findTestCasesByProject(projectId);
    }

    @RequestMapping(value = "project/{projectId}/testsuite", method = RequestMethod.GET)
    @ResponseBody
    public PaginatedList<TestSuiteDTO> getTestSuiteForProject(@PathVariable Long projectId) {
        return testSuiteService.findTestSuiteByProject(projectId);
    }

    @RequestMapping(value = "environment/{envid}", method = RequestMethod.PUT)
    public EnvironmentDTO updateEnvironment(@PathVariable Long envid, @RequestBody EnvironmentDTO environment) {
        environmentService.updateEnvironment(environment);
        return environmentService.findEnvironment(envid);
    }

    @RequestMapping(value = "project/{projectid}/environment", method = RequestMethod.POST)
    public EnvironmentDTO createEnvironment(@PathVariable Long projectid, @RequestBody EnvironmentDTO environment) {
        environment.projectId = projectid;
        return environmentService.createEnvironment(environment);
    }

    @RequestMapping(value = "schedule", method = RequestMethod.POST)
    public ScheduleDTO schedule(@RequestBody String body) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        JsonObject jsonObject = new GsonBuilder().create().fromJson(body, JsonObject.class);
        Long envId = jsonObject.get("environmentId").getAsLong();
        String gridUrl = !jsonObject.get("grid").isJsonNull() ? jsonObject.get("grid").getAsString() : null;
        Map<String, String> customParameters = getMapFromCustomParameters(jsonObject);

        if (jsonObject.has("testCases") && !jsonObject.get("testCases").isJsonNull()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("testCases");

            List<TestCase> testCaseList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                testCaseList.add(new TestCase(jsonArray.get(i).getAsLong()));
            }

            scheduleDTO.scheduleId = testScheduler.scheduleTestCase(testCaseList, envId, gridUrl, customParameters);
        } else {
            JsonArray jsonArray = jsonObject.getAsJsonArray("testSuites");

            List<TestSuite> testSuiteList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                testSuiteList.add(new TestSuite(jsonArray.get(i).getAsLong()));
            }

            scheduleDTO.scheduleId = testScheduler.scheduleTestSuite(testSuiteList, envId, gridUrl, customParameters);
        }
        return scheduleDTO;

    }

    private Map<String, String> getMapFromCustomParameters(JsonObject jsonObject) {
        Map<String, String> result = new HashMap<>();

        if (!jsonObject.get("parameters").isJsonNull()) {
            String customParam = jsonObject.get("parameters").getAsString();
            String[] params = customParam.split(";");

            for (String param : params) {
                String[] keyValue = param.split("=");
                result.put(keyValue[0], keyValue[1]);
            }
        }

        return result;
    }

    @RequestMapping(value = "environment/{envid}", method = RequestMethod.GET)
    public EnvironmentDTO getEnvironment(@PathVariable Long envid) {
        return environmentService.findEnvironment(envid);
    }

    @RequestMapping(value = "schedule/{scheduleId}", method = RequestMethod.GET)
    public ScheduleDTO getScheduleStatus(@PathVariable Long scheduleId) {
        return scheduleService.findScheduleStatusNew(scheduleId);
    }

    @RequestMapping(value = "environment/{envid}/schedule", method = RequestMethod.GET)
    public PaginatedList<ScheduleDTO> getSchedulesByEnvironment(@PathVariable Long envid) {
        return scheduleService.findScheduleByEnvironment(envid, new PaginationDTO(0, 1000, SortOrder.DESC));
    }

    @RequestMapping(value = "testcase/{id}", method = RequestMethod.GET)
    public TestCaseDTO getTestCase(@PathVariable Long id) {
        return testCaseService.findTestCase(id);
    }

    @RequestMapping(value = "include", method = RequestMethod.GET)
    public List<TestCaseDTO> getIncludeableTestCase(@RequestParam(value = "projectId") Long projectId,
                                                    @RequestParam(value = "testCaseId", required = false) Long testCaseId
                                                    , @RequestParam(value = "searchTerm", required = false) String searchTerm) {
        PaginatedRequest paginatedRequest = new PaginatedRequest();
        paginatedRequest.searchTerm = searchTerm;
        return testCaseService.findIncludableTestCase(projectId, testCaseId, paginatedRequest);
    }

    @RequestMapping(value = "testcase/{id}", method = RequestMethod.PUT)
    public TestCaseDTO updateTestCase(@PathVariable Long id, @RequestBody TestCaseDTO dto) {
        dto.id = id;
        return testCaseService.updateTestCase(dto);
    }

    @RequestMapping(value = "project/{projectId}/testcase", method = RequestMethod.POST)
    public TestCaseDTO createTestCase(@PathVariable Long projectId,
                                      @RequestBody TestCaseDTO dto) {
        dto.projectId = projectId;
        return testCaseService.createTestCase(dto);
    }

    @RequestMapping(value = "testsuite/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TestSuiteDTO getTestSuite(@PathVariable Long id) {
        return testSuiteService.findTestSuite(id);
    }

    @RequestMapping(value = "project/{projectId}/testsuite", method = RequestMethod.POST)
    public TestSuiteDTO createTestSuite(@PathVariable Long projectId, @RequestBody TestSuiteDTO dto) {
        dto.projectId = projectId;
        return testSuiteService.createTestSuite(dto);
    }

    @RequestMapping(value = "testsuite/{id}", method = RequestMethod.PUT)
    public TestSuiteDTO updateTestSuite(@PathVariable Long id,
                                        @RequestBody TestSuiteDTO dto) {
        dto.id = id;
        return testSuiteService.updateTestSuite(dto);
    }

    @RequestMapping(value = "database", method = RequestMethod.GET)
    public void redirectDatabase(final HttpServletResponse response) throws IOException {
        try {
            Gson gson = new GsonBuilder().create();
            JsonObject jsonObject = new JsonObject();
            if (Configuration.isDatabaseConfigure()) {
                LOG.info("Database is already configured1, return success message");
                jsonObject.addProperty("database", "configured");
                response.sendRedirect("index.html");
                //return gson.toJson(jsonObject);
            }
            jsonObject.addProperty("database", "notconfigured");
            response.sendRedirect("database.html");
            //response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            //response.getWriter().write(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return null;
    }

    @RequestMapping(value = "preparedatabase", method = RequestMethod.POST)
    public String prepareDatabase(final HttpServletResponse response, @RequestBody Map<String, String> params) throws IOException {
        LOG.info("updating database");

        String[] host = params.get("host").split(":");
        if (host.length == 1) {
            //add default port if not specified
            params.put("host", host[0] + ":3306");
        }
        liquibaseMigration.update(params);

        LOG.info("Database is successfully updated");

        //update database details in database.properties
        Properties properties = new Properties();
        properties.load(new InputStreamReader(UIController.class.getResourceAsStream(Constants.DATABASE_FILE)));
        LOG.info("application.properties are loaded current keys = {}, storing new information", properties.keys());
        properties.setProperty("jdbc.url", "jdbc:mysql://" + params.get("host") + "/" + params.get("database"));
        properties.setProperty("jdbc.username", params.get("username"));
        properties.setProperty("jdbc.password", params.get("password"));
        properties.store(new OutputStreamWriter(new FileOutputStream(UIController.class.getResource(Constants.DATABASE_FILE).getFile())), "database update");
        Configuration.setDatabaseConfigure(true);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", "success");

        return new GsonBuilder().create().toJson(jsonObject);

    }

    @ExceptionHandler( { AutomationBaseRuntimeException.class } )
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        AutomationBaseRuntimeException ire = (AutomationBaseRuntimeException) e;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, ire, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler( { RuntimeException.class } )
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException e, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, e, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @RequestMapping(value = "settings", method = RequestMethod.GET)
    public SettingsDTO settings() throws IOException {
        return configService.getSettings();
    }

    @RequestMapping(value = "settings", method = RequestMethod.POST)
    public SettingsDTO saveSettings(@RequestBody SettingsDTO settingsDTO) throws IOException {
        return configService.saveSettings(settingsDTO);
    }

    @RequestMapping(value = "commandlist", method = RequestMethod.GET)
    public String getAllCommandName(HttpServletRequest request) {
        List<String> commandName = testCaseService.getAllCommandName();

        Gson gson = new GsonBuilder().create();

        JsonObject json = new JsonObject();
        json.add("commands", gson.toJsonTree(commandName));

        return gson.toJson(json);
    }

    @RequestMapping(value = "debug", method = RequestMethod.POST)
    public DebugSessionDTO executeCommands(@RequestBody DebugSessionDTO debugSessionDTO) {
        return debugSessionService.executeCommands(debugSessionDTO);
    }

    @RequestMapping(value = "closedebug", method = RequestMethod.POST)
    public String closeDebug(@RequestBody String key) {
        debugSessionService.closeDebugSession(key);

        return "{\"status\": \"success\"}";
    }

}
