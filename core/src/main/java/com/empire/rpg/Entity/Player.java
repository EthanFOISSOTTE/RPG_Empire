package com.empire.rpg.Entity;

import com.empire.rpg.Component.HealthComponent;
import com.empire.rpg.Component.WeaponComponent;

public class Player extends Entity {
    private String name;
    private HealthComponent health;
    private WeaponComponent weapon;
    private int attackPower;
    //private PositionComponent position;


    public Player(String name, HealthComponent health, WeaponComponent weapon, int attackPower) {
        this.name = name;
        this.health = health;
        this.weapon = weapon;
        this.attackPower = attackPower;
    }

    public HealthComponent getHealth() {
        return health;
    }

    public void setHealth(HealthComponent health) {
        this.health = health;
    }

    public WeaponComponent getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponComponent weapon) {
        this.weapon = weapon;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void attack(MOB mob) {
        mob.getComponent(HealthComponent.class).setCurrentHealthPoints(mob.getComponent(HealthComponent.class).getCurrentHealthPoints() - this.attackPower);
    }
}
