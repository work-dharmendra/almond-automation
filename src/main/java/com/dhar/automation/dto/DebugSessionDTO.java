package com.dhar.automation.dto;

import com.dhar.automation.RunConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dharmendra Chouhan
 */
public class DebugSessionDTO {

    public String uuid;

    public List<CommandDTO> commands = new ArrayList<>();

    public List<String> steps = new ArrayList<>();

    public RunConfig config = new RunConfig();
}
