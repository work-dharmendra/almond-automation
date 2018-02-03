package com.dhar.automation.command.selenium;

import com.google.common.collect.Lists;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsDriver;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dharmendra Chouhan
 */
public class ByJavaScript extends By implements Serializable {
    private final String script;
    private WebDriver webDriver;
    public ByJavaScript(String script, WebDriver driver) {
        this.script = script;
        this.webDriver = driver;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        JavascriptExecutor js = getJavascriptExecutorFromSearchContext(context);

        // call the JS, inspect and validate response
        System.out.println("return " + script);
        Object response = ((JavascriptExecutor)webDriver).executeScript("return " + script);
        System.out.println(response);
        List<WebElement> elements = getElementListFromJsResponse(response);

        // filter out the elements that aren't descendants of the context node
        if (context instanceof WebElement) {
            filterOutElementsWithoutCommonAncestor(elements, (WebElement)context);
        }

        return elements;
    }

    private static JavascriptExecutor getJavascriptExecutorFromSearchContext(SearchContext context) {
        if (context instanceof JavascriptExecutor) {
            // context is most likely the whole WebDriver
            return (JavascriptExecutor)context;
        }
        if (context instanceof WrapsDriver) {
            // context is most likely some WebElement
            WebDriver driver = ((WrapsDriver)context).getWrappedDriver();
            //checkState(driver instanceof JavascriptExecutor, "This WebDriver doesn't support JavaScript.");
            return (JavascriptExecutor)driver;
        }
        throw new IllegalStateException("We can't invoke JavaScript from this context.");
    }

    @SuppressWarnings("unchecked")  // cast thoroughly checked
    private static List<WebElement> getElementListFromJsResponse(Object response) {
        if (response == null) {
            // no element found
            return Lists.newArrayList();
        }
        if (response instanceof WebElement) {
            // a single element found
            return Lists.newArrayList((WebElement)response);
        }
        if (response instanceof List) {
            // found multiple things, check whether every one of them is a WebElement
            //checkArgument(Iterables.all((List<?>)response, Predicates.instanceOf(WebElement.class)),"The JavaScript query returned something that isn't a WebElement.");
            return (List<WebElement>)response;  // cast is checked as far as we can tell
        }
        throw new IllegalArgumentException("The JavaScript query returned something that isn't a WebElement.");
    }

    private static void filterOutElementsWithoutCommonAncestor(List<WebElement> elements, WebElement ancestor) {
        for (Iterator<WebElement> iter = elements.iterator(); iter.hasNext(); ) {
            WebElement elem = iter.next();
            System.out.println(elem);
            // iterate over ancestors
            while (!elem.equals(ancestor) && !elem.getTagName().equals("html")) {
                elem = elem.findElement(By.xpath("./.."));
            }

            if (!elem.equals(ancestor)) {
                iter.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "By.javaScript: \"" + script + "\"";
    }

}