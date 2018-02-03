package com.dhar.automation;

import com.dhar.automation.domain.TestSuite;
import com.dhar.automation.dto.CommandDTO;
import com.dhar.automation.dto.TestCaseDTO;
import com.dhar.automation.dto.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.dhar.automation.domain.Command;
import com.dhar.automation.domain.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
public class Main {
    public static void main(String []args){
        TestCase testCase = new TestCase();
        testCase.setId(23l);
        testCase.setName("name");
        testCase.setDescription("desc");
        Command cmd1 = new Command();
        cmd1.setName("cmd_name");
        cmd1.setElement("cmd_el");
        cmd1.setValue("cmd_value");
        cmd1.setParams("params");

        List<Command> list = new ArrayList<>();
        list.add(cmd1);
        cmd1 = new Command();
        cmd1.setName("cmd_name1");
        cmd1.setElement("cmd_el1");
        cmd1.setValue("cmd_value1");
        cmd1.setParams("params1");
        list.add(cmd1);
        testCase.setCommands(list);
        Gson gson = new GsonBuilder().create();

        TestSuite testSuite = new TestSuite();
        testSuite.setId(1l);
        testSuite.setName("name");
        testSuite.setDescription("description");
        List<String> list1 = new ArrayList<>();

        TestCaseDTO testCaseDTO = new TestCaseDTO();
        testCaseDTO.name = "new testcase";
        testCaseDTO.description = "desc";
        testCaseDTO.projectId = 1l;

        CommandDTO commandDTO1 = new CommandDTO();
        commandDTO1.name = "cmdname";
        commandDTO1.value = "cmdvalue";
        commandDTO1.element = "cmdelem";
        commandDTO1.params = "cmdparams";

        List<CommandDTO> commandDTOs = new ArrayList<>();
        commandDTOs.add(commandDTO1);
        testCaseDTO.commands = commandDTOs;

        System.out.println(gson.toJson(testCaseDTO));

        UserDTO userDTO = new UserDTO();
        userDTO.username = "username";
        userDTO.password = "pass";

        System.out.println(gson.toJson(userDTO));

    }
}
