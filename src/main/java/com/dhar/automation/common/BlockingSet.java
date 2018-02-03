package com.dhar.automation.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class create blocking queue implementation so that queue contains only unique elements.
 * This class is not fully tested.
 */
public class BlockingSet<E> extends ArrayBlockingQueue<E> {

    private final Lock lock = new ReentrantLock();
    public BlockingSet(int capacity) {
        super(capacity);
    }

    @Override
    public void put(E e) throws InterruptedException {
        try {
            lock.lock();
            if(!this.contains(e)){
                super.put(e);
            }

        } finally {
            lock.unlock();
        }
    }
}
