package com.dhar.automation.command;

import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;

/**
 * Wait till condition is met or timeout
 * @author Dharmendra Chouhan
 */
public class WaitUntil {

    private int timeout = 5*60*1000; //5 minute
    private int waitTime = 200;//200 milli seconds

    private ICondition condition;

    public WaitUntil(ICondition condition){
        this.condition = condition;
    }

    public void until(){
        long stopTime = System.currentTimeMillis() + timeout;

        boolean isWaitOver = false;

        while(!isWaitOver){

            boolean isDone = this.condition.done();
            if(isDone){
                isWaitOver = true;
            }
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            if(System.currentTimeMillis() > stopTime){
                throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_TIMEOUT);
            }
        }
    }

}
