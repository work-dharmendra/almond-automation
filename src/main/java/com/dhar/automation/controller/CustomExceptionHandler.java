package com.dhar.automation.controller;

import com.dhar.automation.exception.AutomationBaseRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Dharmendra.Singh
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( { AutomationBaseRuntimeException.class } )
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        AutomationBaseRuntimeException ire = (AutomationBaseRuntimeException) e;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, ire, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

}