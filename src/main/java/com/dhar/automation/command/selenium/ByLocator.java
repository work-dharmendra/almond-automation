package com.dhar.automation.command.selenium;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * @author Dharmendra.Singh
 */
@Component
public class ByLocator {

    public static By findBy(String value){
        String [] command = value.split("=");
        String selectorType = command[0];
        String elementPath = command[1];
        By by = null;

        if("id".equals(selectorType.toLowerCase())){
            by = By.id(elementPath);
        }
        if("name".equals(selectorType.toLowerCase())){
            by = By.name(elementPath);
        }
        if("class".equals(selectorType.toLowerCase())){
            by = By.className(elementPath);
        }
        if("xpath".equals(selectorType.toLowerCase())){
            by = By.xpath(elementPath);
        }
        return by;
    }

}
