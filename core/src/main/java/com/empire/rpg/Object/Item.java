package com.empire.rpg.Object;

import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.StateComponent;

public class Item extends Object{
    private String itemType;
    private int itemValue;

    public Item(String name, PositionComponent position, StateComponent state, String itemType, int itemValue) {
        super(name, position, state);
        this.itemType = itemType;
        this.itemValue = itemValue;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemValue() {
        return itemValue;
    }

    public void setItemValue(int itemValue) {
        this.itemValue = itemValue;
    }

    public void collect(){
        System.out.println("Collecting " + this.getName());
    }

    public void use(){
        System.out.println("Using " + this.getName());
    }

    public void drop(){
        System.out.println("Dropping " + this.getName());
    }

    public void equip(){
        System.out.println("Equipping " + this.getName());
    }

    public void unequip(){
        System.out.println("Unequipping " + this.getName());
    }


}
