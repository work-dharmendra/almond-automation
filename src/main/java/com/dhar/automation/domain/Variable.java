package com.dhar.automation.domain;

/**
 * @author Dharmendra.Singh
 */
public class Variable {
    private String key;
    private String value;
    private VariableType type;

    public Variable(String key, String value, VariableType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }
}
