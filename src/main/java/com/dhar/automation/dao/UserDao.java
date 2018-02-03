package com.dhar.automation.dao;

import com.dhar.automation.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Repository
public class UserDao {

    @Autowired
    SessionFactory sessionFactory;

    public List<User> findAllUsers() {

        Session session = sessionFactory.openSession();

        List<User> users = session.createCriteria(User.class).list();

        session.close();

        return users;
    }

    public User findUser(Long id) {
        return (User) sessionFactory.openSession().get(User.class, id);
    }

    public User createUser(User user) {
        Long id = (Long) sessionFactory.openSession().save(user);
        user.setId(id);

        return user;
    }

    public void updateUser(User user) {
        Session session = sessionFactory.openSession();
        session.update(user);
        session.flush();
    }
}
