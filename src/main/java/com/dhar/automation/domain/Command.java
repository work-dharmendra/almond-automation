package com.dhar.automation.domain;

import com.google.gson.GsonBuilder;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by dharmendra.singh on 4/20/2015.
 */
@Embeddable
public class Command implements Serializable{

    private transient int index;
    private String name;
    private String element;
    private String value;
    private String params;

    public Command() {
    }

    public Command(String name, String element, String value) {
        this.name = name;
        this.element = element;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toJson(){
        return new GsonBuilder().create().toJson(this);
    }

}
