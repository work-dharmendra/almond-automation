package com.dhar.automation.common;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Dharmendra.Singh on 4/16/2015.
 */
public class TestBlockingSet {
    public static BlockingQueue<String> queue = new BlockingSet<String>(1000);
    public static void main(String [] args){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    queue.put("1");
                    //queue.put("2");
                    queue.put("1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    try {
                        System.out.println(queue.size());
                        System.out.println(new Date().toString() + " = " + queue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
