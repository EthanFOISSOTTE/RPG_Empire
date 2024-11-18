package com.empire.rpg;

public class Rewards {
    private int xp;
    private int gold;

    // Constructeur sans argument requis pour la désérialisation JSON
    public Rewards() {
    }

    // Constructeur avec arguments pour initialiser xp et gold
    public Rewards(int xp, int gold) {
        this.xp = xp;
        this.gold = gold;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
