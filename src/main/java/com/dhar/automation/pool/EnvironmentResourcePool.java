package com.dhar.automation.pool;

import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.EnvironmentResource;
import com.dhar.automation.queue.ArrayQueue;
import com.dhar.automation.queue.CircularQueue;
import com.dhar.automation.queue.Queue;
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
public class EnvironmentResourcePool {
    private static Logger LOG = LoggerFactory.getLogger(EnvironmentResourcePool.class);

    @Resource
    ApplicationContext applicationContext;
    private Map<Environment, Queue<EnvironmentResource>> queueMap = new HashMap<>();


    public Queue<EnvironmentResource> create(Environment environment){
        LOG.debug("creating queue for env : id = {}", environment.getId());
        Queue<EnvironmentResource> queue = queueMap.get(environment);
        if(queue == null){
            LOG.debug("queue NOT exists for environment : id = {}", environment.getId());
            queue = applicationContext.getBean(CircularQueue.class);
            queueMap.put(environment, queue);
            LOG.debug("queue created for env : id = {}", environment.getId());
        } else {
            LOG.debug("queue already exists for env : id = {}", environment.getId());
        }
        return queue;
    }

    public void destroy(Environment environment){
        queueMap.remove(environment);
    }

    public boolean addResourceToEnvironment(Environment environment, EnvironmentResource resource){
        getEnvironmentResourceQueue(environment).put(resource);
        return true;
    }

    public Queue<EnvironmentResource> getEnvironmentResourceQueue(Environment environment){
        Queue<EnvironmentResource> queue = queueMap.get(environment);
        if(queue == null){
            queue = create(environment);
        }
        return queue;
    }


}
