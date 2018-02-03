package com.dhar.automation.queue;

/**
 * @author Dharmendra.Singh
 */
public interface Queue<T> {

    public void put(T t);

    public T get();
}
