package com.dhar.automation.command;

import com.dhar.automation.domain.Command;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * This class returns appropriate class which can invoke exact selenium command.
 * TODO this class require lot of changes, this is just for poc
 * @author Dharmendra.Singh
 */
@Component
public class CommandFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(CommandFactory.class);

    private static Map<String, Class<? extends CommandExecutor>> commandMap = new HashMap<>();

    private static final List<String> commandNameList = new ArrayList<>();

    @PostConstruct
    public void init(){
        Reflections reflections = new Reflections("com.dhar.automation");
        Set<Class<?>> commandTypes = reflections.getTypesAnnotatedWith(CommandType.class);

        for(Class<?> clazz : commandTypes){
            String name = clazz.getAnnotation(CommandType.class).name();
            CommandProviderType commandType = clazz.getAnnotation(CommandType.class).provider();
            if(commandMap.get(name) == null){
                LOGGER.info("registering command name = {}, provider = {} ", name, commandType);
                commandMap.put(name, (Class<? extends CommandExecutor>) clazz);
            } else {
                LOGGER.info("More than 1 class found for same command name = {}, provider = {} ", name, commandType);
                throw new RuntimeException("More than 1 class found for same command = " + name );
            }
        }

        commandNameList.addAll(commandMap.keySet());
        Collections.sort(commandNameList);
        LOGGER.info("total number of available commands = {}", commandMap.size());
    }

    public CommandExecutor getCommandExecutor(WebDriver webDriver, Command command){
        CommandExecutor commandExecutor;

        try {
            commandExecutor = commandMap.get(command.getName()).getDeclaredConstructor(WebDriver.class).newInstance(webDriver);
        } catch (Exception e) {
            LOGGER.error("No implementation found for command = {}, error = {}", command.getName(), e);
            throw new RuntimeException("No implementation found for command = " + command.getName());
        }

        return commandExecutor;
    }

    public List<String> getAllCommandName(){
        return new ArrayList<>(commandNameList);
    }
}
