package com.dhar.automation.dao;

import com.dhar.automation.Status;
import com.dhar.automation.common.PaginatedList;
import com.dhar.automation.domain.Schedule;
import com.dhar.automation.dto.PaginationDTO;
import com.dhar.automation.dto.ScheduleDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dhar.automation.domain.Status.*;

/**
 * @author Dharmendra.Singh
 */
@Repository
public class ScheduleDao {
    private static Logger LOG = LoggerFactory.getLogger(ScheduleDao.class);

    @Autowired
    SessionFactory sessionFactory;

    public PaginatedList<Schedule> findScheduleByEnvironment(Long envId, PaginationDTO paginationDTO) {
        Session session = sessionFactory.getCurrentSession();
        List<Schedule> scheduleList = session
                .getNamedQuery("findScheduleByEnvironment")
                .setLong("eid", envId)
                .setFirstResult(paginationDTO.start)
                .setMaxResults(paginationDTO.pageSize)
                .list();

        long count = (long) session
                .getNamedQuery("countScheduleByEnvironment")
                .setLong("eid", envId)
                .uniqueResult();

        PaginatedList<Schedule> schedulePaginatedList = new PaginatedList<>(paginationDTO.start, paginationDTO.pageSize, scheduleList, count);
        return schedulePaginatedList;
    }


    public PaginatedList<Schedule> findScheduleByTestCaseAndEnvironment(Long testCaseId, Long environmentId, PaginationDTO paginationDTO) {
        Session session = sessionFactory.getCurrentSession();
        List<Schedule> scheduleList = session
                .getNamedQuery("findScheduleByTestCaseAndEnvironment")
                .setLong("eid", environmentId)
                .setLong("testCaseId", testCaseId)
                .setFirstResult(paginationDTO.start)
                .setMaxResults(paginationDTO.pageSize)
                .list();

        long count = (long) session
                .getNamedQuery("countScheduleByTestCaseAndEnvironment")
                .setLong("eid", environmentId)
                .setLong("testCaseId", testCaseId)
                .uniqueResult();

        PaginatedList<Schedule> schedulePaginatedList = new PaginatedList<>(paginationDTO.start, paginationDTO.pageSize, scheduleList, count);
        return schedulePaginatedList;
    }

    public PaginatedList<Schedule> findSchedule(Long scheduleId) {
        Session session = sessionFactory.getCurrentSession();

        /*List<> = session.createQuery("select el.status, count(*) from ExecutionList el where el.schedule.id = :id " +
                " and el.testSuite is null " +
                " group by el.status")
                .setParameter("id", scheduleId)
                .list();*/

        List<Schedule> scheduleList = session
                                .getNamedQuery("findScheduleFullDetail")
                                .setLong("id", scheduleId)
                                .list();

        PaginatedList<Schedule> paginatedList = new PaginatedList<>();
        paginatedList.list = scheduleList;


        return paginatedList;
    }

    public ScheduleDTO findScheduleStats(Long scheduleId){
        Session session = sessionFactory.getCurrentSession();
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        List<Object[]> list = session.createQuery("select el.status, count(*) from ExecutionList el where el.schedule.id = :id " +
                " and el.testSuite is null " +
                " group by el.status")
                .setParameter("id", scheduleId)
                .list();

        int totalTestCase = 0;
        if(list != null && list.size() > 0){
            for(Object [] objects : list){
                com.dhar.automation.domain.Status status = (com.dhar.automation.domain.Status) objects[0];
                int count = ((Long) objects[1]).intValue();
                totalTestCase = totalTestCase + count;
                switch (status){
                    case FAIL:
                        scheduleDTO.totalFailTestCase = count;
                        break;
                    case NOTSTARTED:
                        scheduleDTO.totalNotStartedTestCase = count;
                        break;
                    case INPROGRESS:
                        scheduleDTO.totalInProgressCase = count;
                        break;
                    case PASS:
                        scheduleDTO.totalPassTestCase = count;
                        break;
                }
            }
        }

        scheduleDTO.totalTestCase = totalTestCase;


        return scheduleDTO;
    }
}
