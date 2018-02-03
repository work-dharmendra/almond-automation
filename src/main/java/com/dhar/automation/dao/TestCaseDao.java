package com.dhar.automation.dao;

import com.dhar.automation.RunType;
import com.dhar.automation.Test;
import com.dhar.automation.common.Constants;
import com.dhar.automation.common.PaginatedRequest;
import com.dhar.automation.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author dharmendra.singh
 */
@Repository
public class TestCaseDao {
    private static Logger LOG = LoggerFactory.getLogger(TestCaseDao.class);

    @Autowired
    SessionFactory sessionFactory;

    public TestCase createTestCase(TestCase testCase){
        LOG.info("creating new testcase : starts");
        Session session  = sessionFactory.getCurrentSession();
        Long id = (Long) session.save(testCase);
        //session.flush();
        //session.close();
        testCase.setId(id);

        return testCase;
    }

    public boolean cycleExist(TestCase testCase, List<Long> childTestCase){
        List<Command> commandList = testCase.getCommands();

        boolean result = false;
        if(commandList != null && commandList.size() > 0){
            for(Command command : commandList){
                if(command.getName().equalsIgnoreCase(Constants.COMMAND_INCLUDE) || command.getName().equalsIgnoreCase(Constants.COMMAND_INVOKE)){
                    if(testCase.getId() != null && testCase.getId().equals(Long.parseLong(command.getValue())) || childTestCase.contains((Long.parseLong(command.getValue())))){
                        return true;
                    }
                    childTestCase.add(Long.valueOf(command.getValue()));
                    boolean isCycleExist = cycleExist(findTestCase(Long.valueOf(command.getValue())), childTestCase);
                    if(isCycleExist){
                        return true;
                    }
                }
            }
        }

        return result;
    }

    public void updateTestCase(TestCase testCase){
        LOG.debug("updating testcase : starts for id = " + testCase.getId());
        Session session = sessionFactory.getCurrentSession();
        session.update(testCase);

        //session.flush();
        //session.close();
    }

    public TestCase findTestCase(Long id){
        TestCase testCase = (TestCase) sessionFactory.getCurrentSession().get(TestCase.class, id);
        testCase.getCommands().size();
        return testCase;
    }

    public Set<TestCase> findTestCaseByTestSuiteId(Long id){
        Session session = sessionFactory.getCurrentSession();
        TestSuite testSuite = (TestSuite) session.get(TestSuite.class, id);
        Set<TestCase> testCases = testSuite.getTestCases();
        testCases.size();
        //session.close();
        return testCases;
    }


    public void saveTestCaseExecution(TestCaseExecution testCaseExecution){
        /*LOG.debug("saving testcaseexecution : scheduleId = {}, testCaseId = {}"
                , testCaseExecution.getScheduleId(), testCaseExecution.getTestCase().getId());*/
        sessionFactory.getCurrentSession().save(testCaseExecution);
    }

    public List<TestCaseExecution> findTestCaseExecution(Long scheduleId, Long testCaseId){
        return sessionFactory.getCurrentSession().createCriteria(TestCaseExecution.class)
                .add(Restrictions.eq("scheduleId", scheduleId))
                .add(Restrictions.eq("testCase", new TestCase(testCaseId)))
                .addOrder(Order.asc("id"))
                .list();
    }

    public void saveScheduleStatus(Schedule schedule){
        sessionFactory.getCurrentSession().save(schedule);
    }

    public List<Schedule> findTestCaseScheduleStatus(Long testCaseId){
        List<Schedule> scheduleStatuses = sessionFactory.getCurrentSession().createCriteria(Schedule.class)
                .add(Restrictions.eq("testCaseSuiteId", testCaseId))
                .addOrder(Order.desc("id"))
                .list();
        return scheduleStatuses;
    }

    public List<Schedule> findTestSuiteScheduleStatus(Long testCaseId){
        List<Schedule> scheduleStatuses = sessionFactory.getCurrentSession().createCriteria(Schedule.class)
                .add(Restrictions.eq("testCaseSuiteId", testCaseId))
                .add(Restrictions.eq("runType", RunType.TESTSUITE))
                .addOrder(Order.desc("id"))
                .list();
        return scheduleStatuses;
    }

    public List<Schedule> findSchedule(Long scheduleId){
        return (List<Schedule>)sessionFactory.getCurrentSession()
                .createQuery("select s from Schedule s where s.scheduleId = :id or s.parentScheduleId = :id order by s.id desc")
                .setParameter("id", scheduleId)
                .list();
        /*return (List<Schedule>) sessionFactory.getCurrentSession().createCriteria(Schedule.class)
                .add(Restrictions.eq("scheduleId", scheduleId))
                .list();*/
    }

    public Schedule findScheduleByTestCaseId(Long testCaseId, Long scheduleId){
        return (Schedule) sessionFactory.getCurrentSession().createCriteria(Schedule.class)
                .add(Restrictions.eq("scheduleId", scheduleId))
                .add(Restrictions.eq("testCaseSuiteId", testCaseId))
                .uniqueResult();
    }

    public List<TestCase> findTestCasesByProject(Long projectId) {
        return sessionFactory.getCurrentSession()
                .createCriteria(TestCase.class)
                .createAlias("project", "p")
                .add(Restrictions.eq("p.id", projectId))
                .addOrder(Order.asc("name"))
                .list();
    }

    /**
     * This returns TestCase by creating new session.
     * This is created to get testcase commands in TestExecutor.
     * @param id id of testcase
     * @return TestCase
     */
    public TestCase findTestCaseCommands(Long id){

        Session session = sessionFactory.openSession();

        TestCase testCase = (TestCase)session.get(TestCase.class, id);
        testCase.getCommands().size();

        session.close();

        return testCase;
    }

    /**
     * quick fix implementation, need to change
     *
     * @return list of testcase which given testcase can include
     */
    public List<TestCase> findIncludableTestCase(Long projectId, Long testCaseId, PaginatedRequest paginatedRequest){
        String searchTerm = paginatedRequest.searchTerm != null ? paginatedRequest.searchTerm : "";
        return sessionFactory.getCurrentSession()
                .createCriteria(TestCase.class)
                .add(Restrictions.like("name", "%" + searchTerm + "%"))
                .createAlias("project", "p")
                .add(Restrictions.eq("p.id", projectId))
                .addOrder(Order.asc("name"))
                .list();
    }

}
