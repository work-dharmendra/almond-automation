package com.dhar.automation.exception;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dharmendra.Singh
 */
public class AutomationBaseRuntimeException extends RuntimeException implements Serializable{

    private AutomationErrorType errorType;
    private String defaultMessage;
    private Map<String, String> params = new HashMap<>();

    public AutomationBaseRuntimeException(AutomationErrorType errorType){
        this.errorType = errorType;
        this.defaultMessage = errorType.getMessage();
    }

    public AutomationBaseRuntimeException(AutomationErrorType errorType, String defaultMessage){
        this.errorType = errorType;
        this.defaultMessage = defaultMessage;
    }

    public AutomationBaseRuntimeException(AutomationErrorType errorType, Map<String, String> params){
        this.errorType = errorType;
        this.defaultMessage = new StrSubstitutor(params).replace(errorType.getMessage());
        this.params = params;
    }

    public AutomationBaseRuntimeException(AutomationErrorType errorType, String key, String value) {
        Map<String, String> params =  new HashMap<>();
        params.put(key, value);
        this.errorType = errorType;
        this.defaultMessage = new StrSubstitutor(params).replace(errorType.getMessage());
        this.params = params;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
