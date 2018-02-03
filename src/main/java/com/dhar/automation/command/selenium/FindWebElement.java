package com.dhar.automation.command.selenium;

import com.dhar.automation.exception.AutomationBaseRuntimeException;
import com.dhar.automation.exception.AutomationErrorType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Helper class to find element on page based on different selector
 *
 * @author Dharmendra.Singh
 */
@Component
public class FindWebElement {

    private static Logger LOGGER = LoggerFactory.getLogger(FindWebElement.class);

    public static WebElement getWebElement(WebDriver webDriver, String value) {

        //user can provide multiple separator separated by semi colon only for id, name, class
        String[] selectors = value.split(";");
        WebElement webElement = null;

        try {
            for (String selector : selectors) {

                String[] command = selector.split("=");
                String selectorType = command[0];
                String elementPath = command[1];

                if ("id".equals(selectorType.toLowerCase())) {
                    if (webElement == null) {
                        webElement = webDriver.findElement(By.id(elementPath));
                    } else {
                        webElement = webElement.findElement(By.id(elementPath));

                    }
                }
                if ("name".equals(selectorType.toLowerCase())) {
                    if (webElement == null) {
                        webElement = webDriver.findElement(By.name(elementPath));
                    } else {
                        webElement = webElement.findElement(By.name(elementPath));
                    }
                }
                if ("class".equals(selectorType.toLowerCase())) {
                    if (webElement == null) {
                        webElement = webDriver.findElement(By.className(elementPath));
                    } else {
                        webElement = webElement.findElement(By.className(elementPath));
                    }
                }
                if ("css".equals(selectorType.toLowerCase())) {
                    //TODO temporary fix
                    elementPath = selector.substring(4, selector.length());
                    if (webElement == null) {
                        webElement = webDriver.findElement(By.cssSelector(elementPath));
                    } else {
                        webElement = webElement.findElement(By.cssSelector(elementPath));
                    }
                }
                if ("xpath".equals(selectorType.toLowerCase())) {
                    //TODO temporary fix
                    elementPath = selector.substring(6, selector.length());
                    if (webElement == null) {
                        webElement = webDriver.findElement(By.xpath(elementPath));
                    } else {
                        webElement = webElement.findElement(By.xpath(elementPath));
                    }
                }
                if ("script".equals(selectorType.toLowerCase())) {
                    //TODO temporary fix
                    elementPath = selector.substring(7, selector.length());
                    ByJavaScript javaScript = new ByJavaScript(elementPath, webDriver);
                    if (webElement == null) {
                        webElement = webDriver.findElement(javaScript);
                    } else {
                        webElement = webElement.findElement(javaScript);
                    }
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("no such element for " + value, e);
            //throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR_ELEMENT_NOTFOUND, "element", value);
        } catch (Exception e) {
            LOGGER.error("exception in finding element", e);
            throw new AutomationBaseRuntimeException(AutomationErrorType.ERROR);
        }


        return webElement;
    }
}
