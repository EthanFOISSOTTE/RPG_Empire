package com.empire.rpg.Entity;

import com.empire.rpg.Component.HealthComponent;

public class MOB extends Entity{
    private String name;
    private HealthComponent health;
    private int attackPower;
    // private int distance;
    //private int speed;
    //private int level;
    //private PositionComponent position;

    public MOB(String name, HealthComponent health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
//        this.distance = distance;
//        this.speed = speed;
//        this.level = level;
    }

    public HealthComponent getHealth() {
        return health;
    }

    public void setHealth(HealthComponent health) {
        this.health = health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

//    public int getDistance() {
//        return distance;
//    }
//
//    public void setDistance(int distance) {
//        this.distance = distance;
//    }
//
//    public int getSpeed() {
//        return speed;
//    }
//
//    public void setSpeed(int speed) {
//        this.speed = speed;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void attack(Player target){
        target.getHealth().setCurrentHealthPoints(target.getHealth().getCurrentHealthPoints() - this.attackPower);
    }


}
