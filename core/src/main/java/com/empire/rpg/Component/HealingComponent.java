package com.empire.rpg.Component;

public class HealingComponent implements Component {
    private final int healingPoints;

    public HealingComponent(int healingPoints) {
        this.healingPoints = healingPoints;
    }

    public int getHealingPoints() {
        return healingPoints;
    }
}

