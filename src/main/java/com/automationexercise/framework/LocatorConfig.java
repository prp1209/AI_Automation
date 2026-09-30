package com.automationexercise.framework;

public class LocatorConfig {
    private LocatorType type;
    private String value;

    public LocatorConfig() {
    }

    public LocatorConfig(LocatorType type, String value) {
        this.type = type;
        this.value = value;
    }

    public LocatorType getType() {
        return type;
    }

    public void setType(LocatorType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
