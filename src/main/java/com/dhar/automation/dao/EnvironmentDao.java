package com.dhar.automation.dao;

import com.dhar.automation.domain.Environment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dharmendra.Singh
 */
@Repository
public class EnvironmentDao{

    @Autowired
    SessionFactory sessionFactory;

    public Environment findEnvironment(Long id){
        return (Environment) sessionFactory.openSession().get(Environment.class, id);
    }

    public Environment create(Environment environment){
        Session session = sessionFactory.getCurrentSession();

        Long id = (Long) session.save(environment);
        environment.setId(id);

        return environment;
    }

    public void update(Environment environment){
        Session session = sessionFactory.getCurrentSession();

        session.update(environment);

        session.flush();
    }

    public List<Environment> findAllEnvironments(){
        return sessionFactory.getCurrentSession()
                .createCriteria(com.dhar.automation.domain.Environment.class)
                .createAlias("project", "p")
                .addOrder(Order.asc("p.name"))
                .list();
    }

}
