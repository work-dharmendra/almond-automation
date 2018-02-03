package com.dhar.automation.dao;

import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.domain.Status;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */
@Repository
public class ExecutionListDao {

    @Autowired
    SessionFactory sessionFactory;

    public ExecutionList createExecutionList(ExecutionList executionList) {
        Long id = (Long) sessionFactory
                .getCurrentSession()
                .save(executionList);

        executionList.setId(id);

        return executionList;
    }

    public void updateStatus(Long executionListId, Status status, String errorMessage, Date whenModified, Date whenCompleted){
        Session session = sessionFactory.getCurrentSession();
        ExecutionList executionList = (ExecutionList) session.get(ExecutionList.class, executionListId);

        executionList.setStatus(status);
        executionList.setWhenModified(whenModified);
        executionList.setWhenComplete(whenCompleted);
        executionList.setErrorMessage(errorMessage);
    }

    public ExecutionList findById(Long executionId) {
        Session session = sessionFactory.getCurrentSession();

        ExecutionList executionList =  (ExecutionList) session.createCriteria(ExecutionList.class)
                /*.createAlias("schedule", "s", JoinType.INNER_JOIN)
                .createAlias("testCase", "t", JoinType.INNER_JOIN)
                .createAlias("testSuite", "testSuite", JoinType.INNER_JOIN)*/
                .createAlias("testSuite", "ts", JoinType.LEFT_OUTER_JOIN)
                .createAlias("testCase", "t", JoinType.LEFT_OUTER_JOIN)
                .setFetchMode("params", FetchMode.JOIN)
                .setFetchMode("t.commands", FetchMode.JOIN)
                .createAlias("testCaseExecutionList", "ttt", JoinType.LEFT_OUTER_JOIN)

                .add(Restrictions.eq("id", executionId))
                .uniqueResult();

        if (executionList.getTestCase() != null && executionList.getTestCase().getCommands() != null) {
            Hibernate.initialize(executionList.getTestCase().getCommands());
        }
        Hibernate.initialize(executionList.getTestSuite());
        Hibernate.initialize(executionList.getTestCaseExecutionList());
        Hibernate.initialize(executionList.getSchedule());
        Hibernate.initialize(executionList.getSchedule().getParams());

        executionList.getTestCaseExecutionList().size();

        return executionList;
    }

    /**
     * This return list of execution list which can be immediately executed.
     * return those execution list with given schedule id and null parent.
     * @param scheduleId
     * @return
     */
    public List<ExecutionList> findExecutionListForSchedule(Long scheduleId){
        return sessionFactory.getCurrentSession().createCriteria(ExecutionList.class)
                .add(Restrictions.eq("schedule.id", scheduleId))
                .add(Restrictions.isNull("parent"))
                .list();
    }

    /**
     * It return list of execution list for given scheduleid and parentid.
     * created initialy to get list of executionlist for testsuite.
     * @param scheduleId
     * @param parentId
     * @return
     */
    public List<ExecutionList> findExecutionListForParent(Long scheduleId, Long parentId){
        return sessionFactory.getCurrentSession().createCriteria(ExecutionList.class)
                .add(Restrictions.eq("schedule.id", scheduleId))
                .add(Restrictions.eq("parent", parentId))
                .list();
    }

    public ExecutionList findExecutionListForScheduleAndCommand(Long scheduleId, Integer commandIndex) {
        return (ExecutionList) sessionFactory.getCurrentSession()
                .createCriteria(ExecutionList.class)
                .createAlias("schedule", "s", JoinType.INNER_JOIN)
                .add(Restrictions.eq("s.id", scheduleId))
                .add(Restrictions.eq("commandIndex", commandIndex))
                .uniqueResult();
    }

    public void updateParams(Long executionListId, Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();

        ExecutionList executionList = (ExecutionList) session.get(ExecutionList.class, executionListId);
        executionList.setParams(params);
    }
}
