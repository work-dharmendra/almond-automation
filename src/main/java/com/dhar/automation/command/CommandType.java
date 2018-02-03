package com.dhar.automation.command;

import java.lang.annotation.*;

/**
 *
 * @author Dharmendra Chouhan
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandType {
    String name();
    CommandProviderType provider() default CommandProviderType.SELENIUM;
}
