package com.dhar.automation;

import com.dhar.automation.common.Constants;
import com.dhar.automation.common.Util;
import com.dhar.automation.database.LiquibaseMigration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * NOT COMPLETE
 * @author Dharmendra Chouhan
 */
//@Component
public class AutomationRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(AutomationRegistryPostProcessor.class);

    @Resource
    LiquibaseMigration liquibaseMigration;

    @Resource
    Configuration configuration;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        LOGGER.info("creating and registering new beans");
        try {
            Map<String, String> applicationProperties = getApplicationProperties();
            //liquibaseMigration.update(applicationProperties);
            LOGGER.info("liquibase migration is done, creating connection poll");
            //create connection pool
            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(applicationProperties.get(Constants.JDBC_DRIVER_KEY));
            dataSource.setJdbcUrl(applicationProperties.get(Constants.JDBC_URL_KEY));
            dataSource.setUser(applicationProperties.get(Constants.JDBC_USERNAME_KEY));
            dataSource.setPassword(applicationProperties.get(Constants.JDBC_PASSWORD_KEY));
            dataSource.setInitialPoolSize(Integer.parseInt(Util.getOptionalString(applicationProperties.get("pool.size"), "100")));
            //TODO need to set more properties for connection pool
            LOGGER.info("connection pool  is created, register it with spring");
                /*AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(ComboPooledDataSource.class);
                beanDefinition.setAutowireCandidate(true);
                registry.registerBeanDefinition("dataSource", beanDefinition);
                factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);*/
            GenericBeanDefinition beanDefinitionDataSource = new GenericBeanDefinition();
            //applicationContext.getAutowireCapableBeanFactory().autowireBean(dataSource);
            LOGGER.info("connection pool is registered with spring, creating session factory");
            //create sessionfactory
            LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource);
            Properties hibernateProperties = new Properties();
            hibernateProperties.setProperty(Constants.HIBERNATE_SHOW_SQL_KEY, Util.getOptionalString(applicationProperties.get(Constants.HIBERNATE_SHOW_SQL_KEY), "false"));
            hibernateProperties.setProperty(Constants.HIBERNATE_NEW_GENERATOR_MAPPINGS,
                    Util.getOptionalString(applicationProperties.get(Constants.HIBERNATE_NEW_GENERATOR_MAPPINGS), "false"));
            hibernateProperties.setProperty(Constants.HIBERNATE_DIALECT, Util.getOptionalString(applicationProperties.get(Constants.HIBERNATE_DIALECT), "org.hibernate.dialect.MySQLDialect"));
            sessionFactoryBean.setPackagesToScan(Constants.PACKAGES_SCAN_VALUE);
            sessionFactoryBean.setHibernateProperties(hibernateProperties);
            sessionFactoryBean.afterPropertiesSet();
            LOGGER.info("session factory is created, register it with spring");
            //applicationContext.getAutowireCapableBeanFactory().autowireBean(sessionFactoryBean);
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(SessionFactory.class);
            //beanDefinition.set
            beanDefinition.setAutowireCandidate(true);
            registry.registerBeanDefinition("sessionFactory", beanDefinition);
            //factory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            //create transaction manager
            HibernateTransactionManager transactionManager = new HibernateTransactionManager();
            transactionManager.setSessionFactory(sessionFactoryBean.getObject());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //applicationContext.getAutowireCapableBeanFactory().autowireBean(transactionManager);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    /**
     * It return value of application.properties in map.
     *
     * @return properties in application.properties
     */
    public Map<String, String> getApplicationProperties() {
        Properties properties = new Properties();
        Map<String, String> applicationProperties = new HashMap<>();
        try {
            properties.load(new InputStreamReader(this.getClass().getResourceAsStream(Constants.DATABASE_FILE)));
            for (String key : properties.stringPropertyNames()) {
                applicationProperties.put(key, properties.getProperty(key));
            }

        } catch (IOException e) {
            LOGGER.error("Error in reading application.properties");
        }

        return applicationProperties;
    }

    public boolean isDatabaseDetailsValid() {
        boolean result = false;
        Map<String, String> configurations = new HashMap<>();

        if (StringUtils.isNotEmpty(configurations.get(Constants.JDBC_URL_KEY)) && StringUtils.isNotEmpty(Constants.JDBC_USERNAME_KEY)) {
            result = true;
        }

        return result;
    }
}
