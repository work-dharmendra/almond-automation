package com.dhar.automation.service;

import com.dhar.automation.common.HibernateUtil;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.dao.TestSuiteDao;
import com.dhar.automation.domain.TestSuite;
import com.dhar.automation.dto.TestSuiteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmendra.singh on 4/21/2015.
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class TestSuiteService {

    @Autowired
    TestSuiteDao testSuiteDao;

    public TestSuiteDTO createTestSuite(TestSuiteDTO testSuiteDTO){
        testSuiteDTO.id = null;
        TestSuite savedTestSuite = testSuiteDao.createTestSuite(MapEntityDto.buildTestSuite(testSuiteDTO));
        return MapEntityDto.buildTestSuiteDTO(savedTestSuite);
    }

    public TestSuiteDTO updateTestSuite(TestSuiteDTO dto){
        TestSuite savedSuite = testSuiteDao.updateTestSuite(MapEntityDto.buildTestSuite(dto));
        return MapEntityDto.buildTestSuiteDTO(savedSuite);
    }

    public TestSuiteDTO findTestSuite(Long testSuiteId){
        TestSuite testSuite = testSuiteDao.findTestSuite(testSuiteId);

        return MapEntityDto.buildTestSuiteDTO(testSuite);
    }

    public PaginatedList<TestSuiteDTO> findTestSuiteByProject(Long projectId){
        List<TestSuite> testSuiteList = testSuiteDao.findTestSuiteByProject(projectId);
        List<TestSuiteDTO> testSuiteDTOs = new ArrayList<>();

        if(testSuiteList != null && testSuiteList.size() > 0){
            HibernateUtil hibernateUtil = new HibernateUtil();
            for(TestSuite testSuite : testSuiteList){
                testSuiteDTOs.add(MapEntityDto.buildTestSuiteDTO(hibernateUtil.deProxy(testSuite)));
            }
        }

        return new PaginatedList<>(1, testSuiteDTOs.size(), testSuiteDTOs, testSuiteDTOs.size());
    }

}
