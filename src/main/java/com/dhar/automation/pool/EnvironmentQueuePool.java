package com.dhar.automation.pool;

import com.dhar.automation.Execution;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.EnvironmentResource;
import com.dhar.automation.queue.ArrayQueue;
import com.dhar.automation.queue.Queue;
import com.dhar.automation.testrunner.TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
@Component
public class EnvironmentQueuePool {
    private static Logger LOG = LoggerFactory.getLogger(EnvironmentQueuePool.class);

    @Resource
    ApplicationContext applicationContext;

    @Resource
    TestRunner  testRunner;

    @Resource
    EnvironmentResourcePool environmentResourcePool;

    private Map<Environment, Queue<Execution>> queueMap = new HashMap<>();

    public Queue<Execution> createEnvironmentQueue(Environment environment){
        LOG.debug("creating queue for env : envId = {}", environment.getId());
        Queue<Execution> queue = queueMap.get(environment);
        if(queue == null){
            LOG.debug("queue NOT exists for env : envId = {}", environment.getId());
            queue = applicationContext.getBean(ArrayQueue.class);
            queueMap.put(environment, queue);
            LOG.debug("queue created for env : envId = {}", environment.getId());
            processEnvironmentQueue(environment, queue);
        } else {
            LOG.debug("queue already exists for env : envId = {}", environment.getId());
        }

        return queue;
    }

    public Queue getEnvironmentQueue(Environment environment){
        Queue<Execution> queue = queueMap.get(environment);
        if(queue == null){
            queue = createEnvironmentQueue(environment);
        }
        return queue;
    }

    private void processEnvironmentQueue(final Environment environment, final Queue<Execution> queue){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    LOG.debug("waiting for next testcase to execute");
                    Execution execution = queue.get();
                    //LOG.debug("get new testcase to execute , scheduleId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    //temporary commenting code for environment resource
                    /*LOG.debug("waiting for environmentresource for scheduledId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    EnvironmentResource resource = environmentResourcePool.getEnvironmentResourceQueue(environment).get();
                    LOG.debug("get environmentresource for scheduleId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    execution.getRunConfig().setEnvironmentResource(resource);*/
                    testRunner.execute(execution);
                }
            }
        }).start();
    }
}
