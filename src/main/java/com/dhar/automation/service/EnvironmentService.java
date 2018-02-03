package com.dhar.automation.service;

import com.dhar.automation.common.*;
import com.dhar.automation.dao.EnvironmentDao;
import com.dhar.automation.dao.ProjectDao;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.Project;
import com.dhar.automation.dto.EnvironmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */
@Transactional(propagation= Propagation.REQUIRED, readOnly=false)
@Service
public class EnvironmentService {
    private static Logger LOG = LoggerFactory.getLogger(EnvironmentService.class);


    @Autowired
    EnvironmentDao environmentDao;


    public EnvironmentDTO createEnvironment(EnvironmentDTO dto){
        Environment environment = MapEntityDto.buildEnvironment(dto);
        environment = environmentDao.create(environment);
        HibernateDetachUtility.deProxy(environment);
        return MapEntityDto.buildEnvironmentDTO(environment);
    }

    public void updateEnvironment(EnvironmentDTO dto){
        environmentDao.update(MapEntityDto.buildEnvironment(dto));
    }

    public EnvironmentDTO findEnvironment(Long id){
        //HibernateUtil util = new HibernateUtil();
        Environment environment = environmentDao.findEnvironment(id);
        HibernateDetachUtility.deProxy(environment);
        return MapEntityDto.buildEnvironmentDTO(environment);
    }

    public PaginatedList<EnvironmentDTO> findAllEnvironment(){
        List<Environment> environmentList = environmentDao.findAllEnvironments();

        List<EnvironmentDTO> environmentDTOs = new ArrayList<>();
        if(environmentList != null && environmentList.size() > 0){

            for(Environment environment : environmentList){
                environmentDTOs.add(MapEntityDto.buildEnvironmentDTO(environment));
            }
        }

        return new PaginatedList<>(1, environmentDTOs.size(), environmentDTOs, environmentDTOs.size());
    }


}