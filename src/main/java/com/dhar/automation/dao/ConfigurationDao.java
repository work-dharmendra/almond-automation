package com.dhar.automation.dao;

import com.dhar.automation.domain.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dharmendra.Singh on 4/17/2015.
 */
@Repository
public class ConfigurationDao {
    @Autowired
    SessionFactory sessionFactory;

    public Map<String,String> getConfigurations(){
        Session session = sessionFactory.openSession();
        List<Config> listConfig = session.createCriteria(Config.class).list();

        Map<String, String> result = new HashMap<>();
        if(listConfig != null){
            for(Config config : listConfig){
                result.put(config.getName(), config.getValue());
            }
        }

        return result;
    }

    public void addConfig(String key, String value){
        Config config = new Config();
        config.setName(key);
        config.setValue(value);
        org.hibernate.Session session = sessionFactory.getCurrentSession();
        session.save(config);
    }

    public void updateConfig(String name, String value){
        Session session = sessionFactory.getCurrentSession();
        List<Config> configList = session.createSQLQuery("select value from config where name=:name")
                .setParameter("name", name)
                .list();

        if(configList != null && configList.size() > 0){
            session.createSQLQuery("update config set value=:value where name=:name")
                    .setParameter("name", name)
                    .setParameter("value", value)
                    .executeUpdate();
        } else {
            addConfig(name, value);
        }


    }
}
