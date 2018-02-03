package com.dhar.automation.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Dharmendra.Singh
 */
public class CircularQueue<T> extends AbstractQueue<T> {

    private BlockingQueue<T> queue = new ArrayBlockingQueue<>(1000);

    @Override
    public void put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T get() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in getting value from queue", e);
        }
    }
}
