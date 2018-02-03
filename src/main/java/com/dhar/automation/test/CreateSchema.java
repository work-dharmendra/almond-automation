package com.dhar.automation.test;

import com.dhar.automation.domain.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author Dharmendra.Singh
 */
public class CreateSchema {

    public static void main(String []args){
        Configuration config =
                new Configuration();
        config.addAnnotatedClass(Project.class);
        config.addAnnotatedClass(Environment.class);
        config.addAnnotatedClass(User.class);
        config.addAnnotatedClass(TestCase.class);
        config.addAnnotatedClass(Command.class);
        config.addAnnotatedClass(Config.class);
        config.addAnnotatedClass(TestSuite.class);
        config.addAnnotatedClass(EnvironmentResource.class);

        config.addAnnotatedClass(Schedule.class);
        config.addAnnotatedClass(ExecutionList.class);
        config.addAnnotatedClass(TestCaseExecution.class);

        config.setProperty(org.hibernate.cfg.Environment.DRIVER, "com.mysql.jdbc.Driver");
        config.setProperty(org.hibernate.cfg.Environment.URL, "jdbc:mysql://localhost:3306/automation");
        config.setProperty(org.hibernate.cfg.Environment.USER, "automation");
        config.setProperty(org.hibernate.cfg.Environment.PASS, "automation");
        config.setProperty("show_sql", "true");
        config.configure();

        new SchemaExport(config).drop(true, true);
        new SchemaExport(config).create(true, true);
    }
}
