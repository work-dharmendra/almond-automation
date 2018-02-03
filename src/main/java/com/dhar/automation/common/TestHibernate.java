package com.dhar.automation.common;

import com.dhar.automation.domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra.Singh
 */
public class TestHibernate {

    private SessionFactory sessionFactory;
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
        config.addAnnotatedClass(Schedule.class);
        config.addAnnotatedClass(ExecutionList.class);
        config.addAnnotatedClass(TestCaseExecution.class);
        config.setProperty(org.hibernate.cfg.Environment.DRIVER, "com.mysql.jdbc.Driver");
        config.setProperty(org.hibernate.cfg.Environment.URL, "jdbc:mysql://localhost:3306/automation");
        config.setProperty(org.hibernate.cfg.Environment.USER, "automation");
        config.setProperty(org.hibernate.cfg.Environment.PASS, "automation");
        config.setProperty("show_sql", "true");
        config.configure();

        TestHibernate hibernate = new TestHibernate();
        hibernate.sessionFactory = config.buildSessionFactory();
        //hibernate.createEnv();
        hibernate.saveProject();
        //hibernate.createUser();
        hibernate.createTestcase();
        hibernate.createTestsuite();
        hibernate.updateTestsuite();
        //hibernate.updateTestcase();
    }


    public void createTestcase(){
        TestCase testCase = new TestCase();
        //testCase.setId(1l);
        testCase.setName("new name");
        testCase.setProject(new Project(1l));

        Command cmd1 = new Command();
        cmd1.setName("name1");
        cmd1.setElement("el1");
        Command cmd2 = new Command();
        cmd2.setName("name2");
        cmd2.setElement("el2");
        List<Command> commands = new ArrayList<>();
        commands.add(cmd1);
        commands.add(cmd2);
        testCase.setCommands(commands);

        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.saveOrUpdate(testCase);

        session.flush();
        tx.commit();
    }

    public void createTestsuite(){
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        TestSuite suite = new TestSuite();
        suite.setName("name1");
        suite.getTestCases().add(new TestCase(1l));
        //suite.getTestSuites().add(new TestSuite(1l));
        session.saveOrUpdate(suite);
        session.flush();
        tx.commit();

        tx = session.beginTransaction();
        suite = new TestSuite();
        suite.setName("name2");
        suite.getTestCases().add(new TestCase(1l));
        suite.getTestSuites().add(new TestSuite(1l));
        session.saveOrUpdate(suite);
        session.flush();
        tx.commit();

        tx = session.beginTransaction();
        suite = new TestSuite();
        suite.setName("name3");
        suite.getTestCases().add(new TestCase(1l));
        suite.getTestSuites().add(new TestSuite(1l));
        suite.getTestSuites().add(new TestSuite(2l));
        session.saveOrUpdate(suite);
        session.flush();
        tx.commit();
    }

    public void updateTestsuite(){
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        TestSuite suite = (TestSuite) session.load(TestSuite.class, 3l);
        suite.getTestSuites().remove(new TestSuite(1l));
        session.saveOrUpdate(suite);

        session.flush();
        tx.commit();
    }

    public void updateTestcase(){
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        TestCase testCase = (TestCase) session.get(TestCase.class, 1l);
        //testCase.setId(1l);
        //testCase.setName("name");
        //testCase.setProject(new Project(1l));
        /*for(CommandType cmd : testCase.getCommands()){
            testCase.getCommands().remove(cmd);
        }*/
        Command cmd1 = new Command();
        cmd1.setName("name1");
        cmd1.setElement("el1");
        Command cmd2 = new Command();
        cmd2.setName("name2");
        cmd2.setElement("el2");
        List<Command> commands = new ArrayList<>();
        commands.add(cmd1);
        commands.add(cmd2);
        //testCase.getCommands().remove(1);
        //testCase.getCommands().add(cmd1);
        testCase.setCommands(commands);


        session.saveOrUpdate(testCase);

        session.flush();
        tx.commit();
    }

    public void createUser(){
        Environment env = new Environment();
        env.setId(1l);
        env.setProject(new Project(1l));
        User user = new User();
        user.setId(1l);
        user.setUsername("username");
        user.setPassword("pass");
        user.setUserType("admin");
        user.setEnvironment(env);
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.saveOrUpdate(user);

        session.flush();
        tx.commit();
    }

    public void saveProject(){

        Project project = new Project();
        project.setName("project1");
        project.setDescription("test");
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.saveOrUpdate(project);

        session.flush();
        tx.commit();

    }

    public void createEnv(){
        Environment env = new Environment();
        env.setId(1l);
        env.setDescription("desc new");
        env.setName("new name");
        env.setProject(new Project(1l));
        org.hibernate.Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.update(env);

        session.flush();
        tx.commit();


    }
}
