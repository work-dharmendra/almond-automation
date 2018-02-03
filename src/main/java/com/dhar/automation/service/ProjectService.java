package com.dhar.automation.service;

import com.dhar.automation.common.HibernateUtil;
import com.dhar.automation.common.MapEntityDto;
import com.dhar.automation.dao.ProjectDao;
import com.dhar.automation.domain.Project;
import com.dhar.automation.dto.ProjectDTO;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;
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
public class ProjectService {
    private static Logger LOG = LoggerFactory.getLogger(ProjectService.class);


    @Autowired
    ProjectDao projectDao;


    public ProjectDTO createProject(ProjectDTO dto){
        HibernateUtil util = new HibernateUtil();
        Project project = MapEntityDto.buildProject(dto);
        project.setId(null);
        project = projectDao.create(project);
        project = util.deProxy(project);
        return MapEntityDto.buildProjectDTO(project);
    }

    public void updateProject(ProjectDTO dto){
        Project project = MapEntityDto.buildProject(dto);

        projectDao.update(project);

    }

    public ProjectDTO findProject(Long id){
        HibernateUtil util = new HibernateUtil();
        Project project = projectDao.findProject(id);
        if(project == null){
            throw new AutomationBaseRuntimeException(AutomationErrorType.ID_NOT_FOUND, "Project doesn't exist", "DD");
        }
        project = util.deProxy(project);

        ProjectDTO projectDTO = MapEntityDto.buildProjectDTO(project);

        return projectDTO;
    }

    public List<ProjectDTO> findAllProjects() {
        List<Project> projects = projectDao.findAllProject();
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        if(projects != null && projects.size() > 0){
            for(Project project : projects){
                projectDTOs.add(MapEntityDto.buildProjectDTO(project));
            }
        }

        return projectDTOs;
    }
}
