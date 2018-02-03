package com.dhar.automation.poc;

import com.dhar.automation.queue.ArrayQueue;
import com.dhar.automation.queue.CircularQueue;
import com.dhar.automation.queue.Queue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Dharmendra.Singh
 */
public class QueueFactoryTest {

    @Test
    public void testFactoryConfigurationBean() throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(QueueFactory.class);

        Queue<String> arrayQueue = ctx.getBean(ArrayQueue.class);

        /*System.out.println(arrayQueue.put(new String()));

        Queue<String> circularQueue = ctx.getBean(CircularQueue.class);

        System.out.println(circularQueue.put(new String()));*/
    }
}
