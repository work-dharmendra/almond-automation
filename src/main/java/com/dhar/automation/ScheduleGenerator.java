package com.dhar.automation;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

/**
 * It generate scheduled id for testcase and testsuite
 * @author Dharmendra Chouhan
 */
@Component
public class ScheduleGenerator {

    AtomicLong scheduleGenerator;
    public ScheduleGenerator(){
        Calendar currentCalendar = Calendar.getInstance();
        long currentTime = currentCalendar.getTimeInMillis();
        Calendar baseCalendar = Calendar.getInstance();
        baseCalendar.set(2015, 1, 1, 0, 0, 0);
        long baseTime = baseCalendar.getTimeInMillis();
        scheduleGenerator = new AtomicLong((currentTime - baseTime)/1000);
    }

    public long getNextScheduledId(){
        return scheduleGenerator.incrementAndGet();
    }

}
