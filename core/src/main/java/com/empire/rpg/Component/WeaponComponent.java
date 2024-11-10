package com.empire.rpg.Component;

public class WeaponComponent implements Component {
    private String name;
    private int damage;

    public WeaponComponent(String name, int attackPoints) {
        this.name = name;
        this.damage = attackPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
