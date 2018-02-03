package com.dhar.automation.interceptor;

import com.dhar.automation.Configuration;
import com.dhar.automation.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This interceptor watch every request and check if database is configured or not.
 * If it it not configured then it redirect to page where user needs to fill
 * database details.
 * It store private variable to save database configuration state.
 * Ideally access to this variable should be synchronized but will do it later.
 *
 * @author Dharmendra Chouhan
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    public static boolean isDatabaseConfigured = false;

    @Resource
    Configuration configuration;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!Configuration.isDatabaseConfigure()){
            //check if Constants.

            LOGGER.info("database is not configured, checking for request uri");
            if(request.getRequestURI().contains("database")){
                LOGGER.info("database is not configured, but request is for database, continue this request.");
                return super.preHandle(request, response, handler);
            } else {
                if (!isDatabaseFileValid()) {
                    LOGGER.info("redirecting to database configuration isDatabaseConfigured = {}, uri = {}"
                            , Configuration.isDatabaseConfigure(), request.getRequestURI());
                    response.sendRedirect("database.service");
                    return false;
                } else {
                    LOGGER.info("Database file (application.properties) is valid");
                    Configuration.setDatabaseConfigure(true);
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    private boolean isDatabaseFileValid() throws IOException {
        Properties properties = new Properties();
        boolean result = false;
        try {
            properties.load(new InputStreamReader(RequestInterceptor.class.getResourceAsStream(Constants.DATABASE_FILE)));
            if(StringUtils.isNotEmpty(properties.getProperty("jdbc.url"))  && StringUtils.isNotEmpty(properties.getProperty("jdbc.username"))){
                result = true;
            }
        } catch (IOException e) {
            throw e;
        }
        return result;
    }
}
