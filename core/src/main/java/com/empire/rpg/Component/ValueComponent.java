package com.empire.rpg.Component;

public class ValueComponent implements Component {
    private final int value;

    public ValueComponent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

