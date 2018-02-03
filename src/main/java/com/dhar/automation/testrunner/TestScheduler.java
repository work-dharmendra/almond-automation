package com.dhar.automation.testrunner;

import com.dhar.automation.Execution;
import com.dhar.automation.RunConfig;
import com.dhar.automation.RunType;
import com.dhar.automation.ScheduleGenerator;
import com.dhar.automation.common.Constants;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.dao.ConfigurationDao;
import com.dhar.automation.dao.EnvironmentDao;
import com.dhar.automation.dao.ExecutionListDao;
import com.dhar.automation.dao.TestCaseDao;
import com.dhar.automation.domain.*;
import com.dhar.automation.dto.CommandDTO;
import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.dto.TestSuiteDTO;
import com.dhar.automation.pool.EnvironmentQueuePool;
import com.dhar.automation.pool.GridQueuePool;
import com.dhar.automation.service.ExecutionListService;
import com.dhar.automation.service.TestCaseService;
import com.dhar.automation.service.TestSuiteService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Dharmendra.Singh
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class TestScheduler {
    private static Logger LOG = LoggerFactory.getLogger(TestScheduler.class);

    @Resource
    ScheduleGenerator scheduleGenerator;
    @Resource
    EnvironmentQueuePool environmentQueuePool;

    @Resource
    GridQueuePool gridQueuePool;
    @Resource
    TestCaseDao testCaseDao;
    @Resource
    ExecutionListDao executionListDao;
    @Resource
    ExecutionListService executionListService;
    @Resource
    TestCaseService testCaseService;
    @Resource
    TestSuiteService testSuiteService;
    @Resource
    EnvironmentDao environmentDao;
    @Resource
    ConfigurationDao configurationDao;
    @Resource
    TestRunner testRunner;

    public Long scheduleTestCase(TestCase testCase, RunConfig runConfig){
        Long scheduleId = scheduleGenerator.getNextScheduledId();
        //Schedule schedule = new Schedule(scheduleId, runConfig.getEnvironment(), testCase.getId(), RunType.TESTCASE, testCase.getId() + "");
        Schedule schedule = null;
        LOG.debug("saving schedulestatus for testcase = {} with scheduleId = {} for environment = {}", testCase.getId(), scheduleId, runConfig.getEnvironment().getId());
        testCaseDao.saveScheduleStatus(schedule);
        Execution execution = new Execution(scheduleId, testCase, RunType.TESTCASE, runConfig);
        if (testCase.isNeedResource()) {
            LOG.debug("TestCase = {} has been put into queue = {} with scheduledId = {}", testCase.getId(), scheduleId);
            environmentQueuePool.getEnvironmentQueue(execution.getRunConfig().getEnvironment()).put(execution);
        } else {
            LOG.debug("TestCase = {} will execute with scheduledId = {} because it doesn't require any shared resource", testCase.getId(), scheduleId);
            testRunner.execute(execution);
        }
        LOG.debug("TestCase = {} has been scheduled to execute with scheduleId = {}", testCase.getId(), scheduleId);
        return scheduleId;
    }

    /**
     * This method is created for scheduling testsuite.
     * This method is now also used when one testcase invoke another testcase.
     * This method doesn't generate scheduleid and not save any schedule details in database.
     */
    public Long scheduleTestCase(TestCase testCase, RunConfig runConfig, Long scheduleId){
        Execution execution = new Execution(scheduleId, testCase, RunType.TESTCASE, runConfig);
        if (testCase.isNeedResource()) {
            LOG.debug("TestCase = {} has been scheduled to execute with scheduleId = {}", testCase.getId(), scheduleId);
            environmentQueuePool.getEnvironmentQueue(execution.getRunConfig().getEnvironment()).put(execution);
        } else {
            LOG.debug("TestCase = {} will execute with scheduledId = {} because it doesn't require any shared resource", testCase.getId(), scheduleId);
            testRunner.execute(execution);
        }

        return scheduleId;
    }

    /**
     * This method is created for invoke command which passes testcaseid, scheduledid and run config
     * @param testCaseId
     * @param runConfig
     * @param scheduleId scheduled id of testcase which invoke testcase
     * @return
     */
    public Long scheduleTestCase(Long testCaseId, RunConfig runConfig, Long scheduleId){
        TestCase testCase = testCaseDao.findTestCase(testCaseId);
        //Schedule schedule = new Schedule(scheduleId, runConfig.getEnvironment(), testCase.getId(), RunType.TESTCASE, testCase.getId() + "");
        Schedule schedule = null;
        LOG.debug("saving schedulestatus for testcase = {} with scheduleId = {} for environment = {}", testCase.getId(), scheduleId, runConfig.getEnvironment().getId());
        testCaseDao.saveScheduleStatus(schedule);
        Execution execution = new Execution(scheduleId, testCase, RunType.TESTCASE, runConfig);
        if (testCase.isNeedResource()) {
            LOG.debug("TestCase = {} has been scheduled to execute with scheduleId = {}", testCase.getId(), scheduleId);
            environmentQueuePool.getEnvironmentQueue(execution.getRunConfig().getEnvironment()).put(execution);
        } else {
            LOG.debug("TestCase = {} will execute with scheduledId = {} because it doesn't require any shared resource", testCase.getId(), scheduleId);
            testRunner.execute(execution);
        }

        return scheduleId;
    }

    public Long scheduleTestCase(Long testCaseId, RunConfig runConfig){
        TestCase testCase = testCaseDao.findTestCase(testCaseId);
        return scheduleTestCase(testCase, runConfig);
    }

    public Long scheduleTestCase(Long testCaseId, Long environmentId){
        Environment environment = environmentDao.findEnvironment(environmentId);
        RunConfig runConfig = new RunConfig();
        runConfig.setParams(configurationDao.getConfigurations());
        runConfig.setEnvironment(environment);
        return scheduleTestCase(testCaseId, runConfig);
    }

    public Long scheduleTestSuite(final List<TestSuiteDTO> testSuiteDTOs, final Long environmentId){

        if(testSuiteDTOs  == null || testSuiteDTOs.size() == 0){
            throw new RuntimeException("no testsuite given");
        }
        final Set<TestCase> testCases = new HashSet<>();
        StringBuilder suiteIds = new StringBuilder();
        //this field store suite id to command separated list of testcases
        //we need this store comma separated list of testcase per testsuite
        final Map<Long, String> suiteIdTestCases = new HashMap<>();
        for(TestSuiteDTO suiteDTO : testSuiteDTOs){
            Set<TestCase> testCaseSet = testCaseService.findTestCaseByTestSuiteId(suiteDTO.id);
            StringBuilder testCasesIds = new StringBuilder();//this is used to save comma separated list of testcaseids for testsuite
            for(TestCase testCase : testCaseSet){
                testCasesIds.append((testCasesIds.toString().equals("") ? "" : ",") + testCase.getId());
            }
            suiteIdTestCases.put(suiteDTO.id, testCasesIds.toString());
            suiteIds.append(suiteDTO.id);
            testCases.addAll(testCaseSet);
        }

        Long scheduleId = 0l;
        if(testCases.size() > 0){
            scheduleId = scheduleGenerator.getNextScheduledId();
            final Long finalScheduleId = scheduleId;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Environment environment = environmentDao.findEnvironment(environmentId);

                    for(TestCase testCase : testCases){
                        RunConfig runConfig = new RunConfig();
                        runConfig.setParams(configurationDao.getConfigurations());
                        runConfig.setEnvironment(environment);
                        scheduleTestCase(testCase, runConfig, finalScheduleId);
                    }
                    for(TestSuiteDTO suiteDTO : testSuiteDTOs){
                        if(!StringUtils.isEmpty(suiteIdTestCases.get(suiteDTO.id))){
                            //Schedule schedule = new Schedule(finalScheduleId, environment, suiteDTO.id, RunType.TESTSUITE, suiteIdTestCases.get(suiteDTO.id));
                            Schedule schedule = null;
                            LOG.debug("saving schedulestatus for testsuite = {} with scheduleId = {}", suiteDTO.id, finalScheduleId);
                            testCaseService.saveScheduleStatus(schedule);
                        } else {
                            LOG.debug("NOT saving schedulestatus for testsuite = {} because it contains no testcase", suiteDTO.id);
                        }
                    }
                }
            }).start();

        } else {
            LOG.debug("no testcases for testsuite = {}", suiteIds.toString());
        }
        return scheduleId;
    }

    public Long scheduleTestCaseNew(Long testCaseId, Long environmentId){

        String gridUrl = configurationDao.getConfigurations().get(Constants.GRID_URL_KEY);
        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.add(new TestCase(testCaseId));

        return scheduleTestCase(testCaseList, environmentId, gridUrl, null);
    }

    public Long scheduleTestCase(Long testCaseId, Long environmentId, String gridUrl, Map<String, String> params){

        List<TestCase> testCaseList = new ArrayList<>();
        testCaseList.add(new TestCase(testCaseId));

        return scheduleTestCase(testCaseList, environmentId, gridUrl, params);
    }

    public Long scheduleTestCase(final List<TestCase> testCases, final Long environmentId, String gridUrl, final Map<String, String> customParameters){

        if (gridUrl == null) {
            gridUrl = configurationDao.getConfigurations().get(Constants.GRID_URL_KEY);
        }
        if(testCases.size() > 0){
            final Long scheduleId = scheduleGenerator.getNextScheduledId();
            final String finalGridUrl = gridUrl;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Environment environment = environmentDao.findEnvironment(environmentId);

                    Schedule schedule = new Schedule(scheduleId, environment, finalGridUrl, customParameters == null ? null : customParameters);
                    testCaseService.saveScheduleStatus(schedule);

                    for(TestCase testCase : testCases){
                        /*LOG.debug("saving executionlist for testcase = {} with scheduleId = {}", testCase.getId(), scheduleId);
                        ExecutionList executionList = new ExecutionList(schedule, testCase);
                        executionListService.create(executionList);*/

                        createExecutionList(schedule, testCase, null, null);
                    }

                    addScheduleToQueue(schedule.getId());
                }
            }).start();
            return scheduleId;
        } else {
            LOG.debug("no testcases to execute");
            return -1l;
        }
    }

    public Long scheduleTestSuite(final List<TestSuite> testSuiteList, final Long environmentId, String gridUrl, final Map<String, String> customParameters){

        if (gridUrl == null) {
            gridUrl = configurationDao.getConfigurations().get(Constants.GRID_URL_KEY);
        }
        if(testSuiteList.size() > 0){
            LOG.debug("scheduling testsuite {} for environment {}", testSuiteList.size(), environmentId);
            final Long scheduleId = scheduleGenerator.getNextScheduledId();
            final String finalGridUrl = gridUrl;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Environment environment = environmentDao.findEnvironment(environmentId);

                    LOG.debug("saving schedule for testsuite , scheduleId = {}", scheduleId);
                    Schedule schedule = new Schedule(scheduleId, environment, finalGridUrl, customParameters == null ? null : customParameters);
                    testCaseService.saveScheduleStatus(schedule);

                    for(TestSuite testSuite : testSuiteList){
                        TestSuite testSuite1 = MapEntityDto.buildTestSuite(testSuiteService.findTestSuite(testSuite.getId()));
                        ExecutionList executionList = createExecutionList(schedule, testSuite1, null, null);

                        for(TestCase testCase : testSuite1.getTestCases()){
                            createExecutionList(schedule, testCase, executionList.getId(), null);
                        }
                    }

                    addScheduleToQueue(schedule.getId());
                }
            }).start();
            return scheduleId;
        } else {
            LOG.debug("no testcases to execute");
            return -1l;
        }
    }

    public void createExecutionList(Schedule schedule, TestCase testCase, Long parentId, Integer commandIndex){
        LOG.debug("saving executionlist for testcase = {} with scheduleId = {}, parentId = {}", testCase.getId(), schedule.getId(), parentId);
        ExecutionList executionList = new ExecutionList(schedule, testCase, parentId, commandIndex);
        executionListService.create(executionList);

        TestCaseDTO testCase1 = testCaseService.findTestCase(testCase.getId());

        List<CommandDTO> commandList = testCase1.commands;

        if(commandList != null && commandList.size() > 0){
            for(int i = 0; i < commandList.size(); i++){
                CommandDTO commandDTO = commandList.get(i);
                if(commandDTO.name.equals("invoke")){
                    createExecutionList(schedule, new TestCase(Long.valueOf(commandDTO.value)), executionList.getId(), i);
                }
            }
        }
    }

    public ExecutionList createExecutionList(Schedule schedule, TestSuite testSuite, Long parentId, Integer commandIndex){
        LOG.debug("saving executionlist for testsuite = {} with scheduleId = {}, parentId = {}", testSuite.getId(), schedule.getId(), parentId);
        ExecutionList executionList = new ExecutionList(schedule, testSuite, parentId, commandIndex);
        return executionListService.create(executionList);
    }

    /**
     * This method is created when user invoke testcase. In this case, we have to set parentScheduleId to current scheduleid
     * BROKEN
     */
    public Long scheduleTestCase(final Long testCaseId, final Long parentScheduledId, final RunConfig runConfig) {
        final Long scheduleId = scheduleGenerator.getNextScheduledId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Environment environment = runConfig.getEnvironment();
                //Schedule schedule = new Schedule(scheduleId, environment, testCaseId, RunType.TESTCASE, String.valueOf(testCaseId), parentScheduledId);
                Schedule schedule = null;
                testCaseService.saveScheduleStatus(schedule);
                TestCase testCase = testCaseDao.findTestCase(testCaseId);
                scheduleTestCase(testCase, runConfig, scheduleId);
            }
        }).start();

        return scheduleId;
    }

    //new methods for scheduling from here
    /*public void scheduleTestCase(TestCase testCase, Schedule schedule){
        ExecutionList executionList = new ExecutionList(schedule, testCase);
        executionListDao.createExecutionList(executionList);

        addToQueue(executionList);
    }*/



    public void addScheduleToQueue(Long scheduleId){
        LOG.debug("adding execution list of schedule to queue {}", scheduleId);
        List<ExecutionList> executionLists = executionListService.findExecutionListForSchedule(scheduleId);

        LOG.debug("total number of execution list for schedule {} is {}", scheduleId, executionLists.size());
        for(ExecutionList executionList : executionLists){
            //execution list could be testsuite which we don't need to execute
            if (executionList.getTestCase() != null) {
                addToQueue(executionList);
            } else if(executionList.getTestSuite() != null) {
                //for testsuite
                LOG.debug("execution list is testsuite, getting list of execution list for scheduleid = {}, parentid = {}", scheduleId, executionList.getId());
                List<ExecutionList> executionListsForSuite = executionListService.findExecutionListForParent(scheduleId, executionList.getId());

                for(ExecutionList executionList1 : executionListsForSuite){
                    addToQueue(executionList1);
                }
            }
        }
    }

    public void addToQueue(ExecutionList executionList){
        LOG.debug("adding execution list to queue {}", executionList.getId());
        String gridUrl = executionList.getSchedule().getGridUrl();
        Execution execution = new Execution(executionList.getId());
        execution.setGridUrl(gridUrl);

        gridQueuePool.getGridQueue(gridUrl).put(execution);

    }
}
