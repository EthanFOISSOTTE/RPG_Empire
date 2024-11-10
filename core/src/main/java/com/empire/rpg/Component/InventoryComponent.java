package com.empire.rpg.Component;

public class InventoryComponent implements Component {
    private int gold;
    private int capacity;
    private int currentCapacity;

    public InventoryComponent(int gold, int capacity) {
        this.gold = gold;
        this.capacity = capacity;
        this.currentCapacity = 0;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public void removeGold(int amount) {
        gold -= amount;
    }

    public void addItem(int weight) {
        currentCapacity += weight;
    }

    public void removeItem(int weight) {
        currentCapacity -= weight;
    }

    public boolean isFull() {
        return currentCapacity >= capacity;
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
