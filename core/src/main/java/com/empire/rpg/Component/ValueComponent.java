package com.empire.rpg.Component;

public class ValueComponent implements Component {
    private final int value;

    public ValueComponent(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {

    }

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}

