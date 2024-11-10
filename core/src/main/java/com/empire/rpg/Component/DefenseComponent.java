package com.empire.rpg.Component;

public class DefenseComponent implements Component {
    private int armorPoints;
    private int damageReduction;

    public DefenseComponent(int armorPoints, int damageReduction) {
        this.armorPoints = armorPoints;
        this.damageReduction = damageReduction;
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    public void setDamageReduction(int damageReduction) {
        this.damageReduction = damageReduction;
    }

    public void takeDamage(int damage){
        this.armorPoints -= damage;
    }

    public void increaseArmorPoints(int armorPoints){
        this.armorPoints += armorPoints;
    }
}
