package com.dhar.automation.database;

import java.util.Map;

/**
 * @author Dharmendra Chouhan
 */
public interface Migration {

    void update(Map<String, String> params);
}
