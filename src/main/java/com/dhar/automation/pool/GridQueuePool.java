package com.dhar.automation.pool;

import com.dhar.automation.Execution;
import com.dhar.automation.domain.Environment;
import com.dhar.automation.domain.ExecutionList;
import com.dhar.automation.queue.ArrayQueue;
import com.dhar.automation.queue.Queue;
import com.dhar.automation.service.ExecutionListService;
import com.dhar.automation.testrunner.TestRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.DesiredCapabilities.chrome;

/**
 * @author Dharmendra.Singh
 */
@Component
public class GridQueuePool {
    private static Logger LOG = LoggerFactory.getLogger(GridQueuePool.class);

    @Resource
    ApplicationContext applicationContext;

    @Resource
    TestRunner  testRunner;

    @Resource
    ExecutionListService executionListService;

    private Map<String, Queue<Execution>> queueMap = new HashMap<>();

    public Queue<Execution> createGridQueue(String gridUrl){
        LOG.debug("creating queue for grid : gridUrl = {}", gridUrl);
        Queue<Execution> queue = queueMap.get(gridUrl);
        if(queue == null){
            LOG.debug("queue NOT exists for grid : gridUrl = {}", gridUrl);
            queue = applicationContext.getBean(ArrayQueue.class);
            queueMap.put(gridUrl, queue);
            LOG.debug("queue created for gridUrl = {}", gridUrl);
            processEnvironmentQueue(gridUrl, queue);
        } else {
            LOG.debug("queue already exists for grid", gridUrl);
        }

        return queue;
    }

    public Queue getGridQueue(String gridUrl){
        Queue<Execution> queue = queueMap.get(gridUrl);
        if(queue == null){
            queue = createGridQueue(gridUrl);
        }
        return queue;
    }

    private void processEnvironmentQueue(final String gridUrl, final Queue<Execution> queue){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    LOG.debug("waiting for next testcase to execute");
                    Execution execution = queue.get();

                    ExecutionList executionList = executionListService.getExecutionList(execution.getExecutionListId());

                    if(executionList == null){
                        LOG.info("execution list not found for {}", execution.getExecutionListId());
                        break;
                    }

                    String gridUrl = executionList.getSchedule().getGridUrl();

                    WebDriver webDriver = null;

                    Date startDate = new Date();

                    int timeOut = 60;//in minutes

                    while(webDriver == null){
                        Date currentDate = new Date();

                        if(TimeUnit.MILLISECONDS.toMinutes(currentDate.getTime() - startDate.getTime()) > timeOut){
                            LOG.error("unable to get web driver even after {} mins", timeOut);
                            break;
                        }

                        webDriver = getWebDriver(gridUrl);

                        if(webDriver == null){
                            try {
                                Thread.sleep(30000l);
                            } catch (InterruptedException e) {

                            }
                        }
                    }

                    execution.setWebDriver(webDriver);
                    //LOG.debug("get new testcase to execute , scheduleId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    //temporary commenting code for environment resource
                    /*LOG.debug("waiting for environmentresource for scheduledId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    EnvironmentResource resource = environmentResourcePool.getEnvironmentResourceQueue(environment).get();
                    LOG.debug("get environmentresource for scheduleId = {}, testCaseId = {}", execution.getScheduledId(), execution.getTestCase().getId());
                    execution.getRunConfig().setEnvironmentResource(resource);*/
                    testRunner.execute(execution);
                }
            }
        }).start();
    }

    private WebDriver getWebDriver(String gridUrl) {

        WebDriver driver = null;

        try {
            driver = new RemoteWebDriver(new URL(gridUrl), chrome());
            //driver = new RemoteWebDriver(new URL(execution.getRunConfig().getGridUrl()), firefox());
            driver.manage().window().maximize();
        } catch (Exception e) {
            LOG.error("Exception in creating webdriver", e);
            //throw new RuntimeException("error.selenium.grid.down");
        }
        return driver;
    }
}
