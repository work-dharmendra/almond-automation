package com.dhar.automation.mock;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dharmendra.Singh
 */
@Configuration
public class CommonMock {

    @Bean
    public ComboPooledDataSource getDataSource(){
        return null;
    }
}
