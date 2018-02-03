package com.dhar.automation.service;

import com.dhar.automation.common.HibernateDetachUtility;
import com.dhar.automation.dao.ExecutionListDao;
import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.domain.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class ExecutionListService {
    private static Logger LOG = LoggerFactory.getLogger(ExecutionListService.class);

    @Autowired
    ExecutionListDao executionListDao;

    public void updateStatus(Long executionListId, Status status, String errorMessage, Date whenModified, Date whenCompleted){
        executionListDao.updateStatus(executionListId, status, errorMessage, whenModified, whenCompleted);
    }

    public ExecutionList getExecutionList(Long executionId){
        return executionListDao.findById(executionId);
    }

    public ExecutionList create(ExecutionList executionList){
        ExecutionList savedExecutionList = executionListDao.createExecutionList(executionList);

        HibernateDetachUtility.deProxy(savedExecutionList);

        return savedExecutionList;
    }

    public List<ExecutionList> findExecutionListForSchedule(Long scheduleId){
        List<ExecutionList> executionLists = executionListDao.findExecutionListForSchedule(scheduleId);

        /*if(executionLists != null && executionLists.size() > 0){
            for(ExecutionList executionList : executionLists){
                HibernateDetachUtility.deProxy(executionList);
            }
        }*/
        return executionLists == null ? new ArrayList<ExecutionList>() : executionLists;
    }

    public List<ExecutionList> findExecutionListForParent(Long scheduleId, Long parentId){
        List<ExecutionList> executionLists = executionListDao.findExecutionListForParent(scheduleId, parentId);

        return executionLists == null ? new ArrayList<ExecutionList>() : executionLists;
    }

    public ExecutionList findExecutionListForScheduleAndCommand(Long scheduleId, int commandIndex) {
        return executionListDao.findExecutionListForScheduleAndCommand(scheduleId, commandIndex);
    }

    public void updateParams(Long executionListId, Map<String, String> params) {
        executionListDao.updateParams(executionListId, params);
    }
}
