package com.dhar.automation.dao;

import com.dhar.automation.domain.Project;
import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dharmendra.Singh
 */
@Repository
public class ProjectDao {

    @Autowired
    SessionFactory sessionFactory;

    public Project findProject(Long id){
        return (Project) sessionFactory.getCurrentSession().get(Project.class, id);
    }

    public Project create(Project project){
        Session session = sessionFactory.getCurrentSession();

        Long id = (Long) session.save(project);
        project.setId(id);

        return project;
    }

    public void update(Project project){
        sessionFactory.getCurrentSession().update(project);
    }

    public List<Project> findAllProject() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Project.class)
                .setFetchMode("environments", FetchMode.SELECT)
                .list();

    }
}
