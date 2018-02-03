package com.dhar.automation.dao;

import com.dhar.automation.common.HibernateUtil;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.domain.Project;
import com.dhar.automation.domain.TestCase;
import com.dhar.automation.domain.TestSuite;
import com.dhar.automation.dto.TestCaseDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmendra.singh on 4/21/2015.
 */
@Repository
public class TestSuiteDao {
    private static Logger LOG = LoggerFactory.getLogger(TestSuiteDao.class);

    @Autowired
    SessionFactory sessionFactory;

    public TestSuite findTestSuite(Long id){
        return (TestSuite) sessionFactory.getCurrentSession().get(TestSuite.class, id);
    }

    public TestSuite createTestSuite(TestSuite testSuite){
        LOG.info("creating new testsuite : starts");
        Long id = (Long) sessionFactory.getCurrentSession().save(testSuite);
        testSuite.setId(id);
        return testSuite;
    }

    public TestSuite updateTestSuite(TestSuite testSuite){
        LOG.info("updating testsuite : starts for id = " + testSuite.getId());
        Session session = sessionFactory.getCurrentSession();
        session.update(testSuite);
        return testSuite;
    }

    public List<TestSuite> findTestSuiteByProject(Long projectId) {
        return sessionFactory.getCurrentSession()
                .createCriteria(TestSuite.class)
                .createAlias("project", "p")
                .add(Restrictions.eq("p.id", projectId))
                .addOrder(Order.asc("name"))
                .list();
    }

}
