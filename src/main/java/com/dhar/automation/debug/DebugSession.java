package com.dhar.automation.debug;

import com.dhar.automation.RunConfig;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author Dharmendra Chouhan
 */
public class DebugSession {
    private String uuid;

    private Date lastAccessTime = new Date();
    private WebDriver webDriver;
    private RunConfig runConfig;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public RunConfig getRunConfig() {
        return runConfig;
    }

    public void setRunConfig(RunConfig runConfig) {
        this.runConfig = runConfig;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
