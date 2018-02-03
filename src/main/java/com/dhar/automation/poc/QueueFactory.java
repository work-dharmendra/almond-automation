package com.dhar.automation.poc;

import com.dhar.automation.Execution;
import com.dhar.automation.queue.ArrayQueue;
import com.dhar.automation.queue.CircularQueue;
import com.dhar.automation.queue.Queue;
import com.dhar.automation.queue.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @author Dharmendra.Singh
 */
@Configuration
public class QueueFactory<T> {

    @Bean
    public Queue<T> createArrayQueue(){
        return new ArrayQueue<T>();
    }

    @Bean
    public Queue<T> createCircularQueue(){
        return new CircularQueue<T>();
    }
}
