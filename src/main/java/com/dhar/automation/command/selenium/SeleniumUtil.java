package com.dhar.automation.command.selenium;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra Chouhan
 */
@Component
public class SeleniumUtil {

    private static Logger LOG = LoggerFactory.getLogger(SeleniumUtil.class);

    public String getScreenshot(WebDriver driver, Object webElement){
        String screenshot = null;

       try {

           try {
               if (webElement != null) {
                   if (driver instanceof JavascriptExecutor) {
                       ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", webElement);
                   }
               }
               screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);

               //remove border after taking screenshot
               if (webElement != null) {
                   if (driver instanceof JavascriptExecutor) {
                       ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='0px'", webElement);
                   }
               }
           } catch (StaleElementReferenceException e) {
               //element is no longer present in ui, take screenshot without referencing element
               if (driver instanceof JavascriptExecutor) {
                   ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'");
                   screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
               }
           }
       } catch (Exception e) {
           LOG.error("exception in taking screenshot", e);
        }

        return screenshot;
    }
}
