package com.empire.rpg.Component;

public class WeaponComponent implements Component {
    private String name;
    private int attackPoints;

    public WeaponComponent(String name, int attackPoints) {
        this.name = name;
        this.attackPoints = attackPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }
}
