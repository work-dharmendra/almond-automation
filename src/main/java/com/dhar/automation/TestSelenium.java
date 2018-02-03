package com.dhar.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by dharmendra.singh on 4/15/2015.
 */
public class TestSelenium {

    public static void main(String []args) throws MalformedURLException {
        System.out.println(new Date().toString());

        for(int i = 0; i < 2; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getId());
                    System.out.println(new Date().toString());
                    WebDriver driver = null;
                    try {
                        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    driver.get("http://10.80.20.229:8080/login.htm");
                    WebElement username = driver.findElement(By.id("userName"));
                    username.sendKeys("dharmendrach@synechron.com");
                    driver.findElement(By.id("password")).sendKeys("welcome");
                    driver.findElement(By.id("submit")).click();
                    System.out.println(new Date().toString());
                }
            }).start();
        }

        //driver.quit();
        /*System.out.println(new Date().toString());

        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
        driver.get("http://10.80.20.229:8080/login.htm");
        driver.findElement(By.id("userName")).sendKeys("dharmendrach@synechron.com");
        driver.findElement(By.id("password")).sendKeys("welcome");
        driver.findElement(By.id("submit")).click();*/

    }


}
