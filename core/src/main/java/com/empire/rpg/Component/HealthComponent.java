package com.empire.rpg.Component;

public class HealthComponent implements Component {
    private int currentHealthPoints;
    private int maxHealthPoints;

    public HealthComponent(int currentHealthPoints, int maxHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
    }

    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public void setCurrentHealthPoints(int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }
    public int takeDamage(int damage){
        return this.currentHealthPoints - damage;
    }
}

