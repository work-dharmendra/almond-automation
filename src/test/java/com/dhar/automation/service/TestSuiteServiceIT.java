package com.dhar.automation.service;

import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.dto.TestSuiteDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dharmendra.singh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:**/automation-servlet-test.xml"})
public class TestSuiteServiceIT {

    @Resource
    TestSuiteService testSuiteService;


    @Test
    public void createTestSuite_shouldCreateTestSuiteSuccessfully(){
        TestSuiteDTO testSuiteDTO = new TestSuiteDTO();
        testSuiteDTO.name = "test 2";
        Set<TestCaseDTO> testCaseDTOs = new HashSet<>();
        testCaseDTOs.add(new TestCaseDTO(1l));
        testCaseDTOs.add(new TestCaseDTO(2l));

        testSuiteDTO.testCases = testCaseDTOs;
        testSuiteDTO.projectId = 1l;
        testSuiteService.createTestSuite(testSuiteDTO);

    }

    @Test
    public void updateTestSuite_shouldUpdateTestSuiteSuccessfully(){
        TestSuiteDTO testSuiteDTO = new TestSuiteDTO();
        testSuiteDTO.id = 1l;
        testSuiteDTO.name = "new-test";
        TestCaseDTO testCaseDTO = new TestCaseDTO(1l);
        Set<TestCaseDTO> testCaseDTOs = new HashSet<>();
        testCaseDTOs.add(new TestCaseDTO(1l));
        testCaseDTOs.add(new TestCaseDTO(2l));

        testSuiteDTO.testCases = testCaseDTOs;
        testSuiteDTO.projectId = 1l;
        testSuiteService.updateTestSuite(testSuiteDTO);

    }
}
