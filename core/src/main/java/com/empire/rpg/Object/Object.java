package com.empire.rpg.Object;

import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.StateComponent;
import com.empire.rpg.Entity.Player;

public abstract class Object {
    private String name;
    private PositionComponent position;
    private StateComponent state;

    public Object(String name, PositionComponent position, StateComponent state) {
        this.name = name;
        this.position = position;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PositionComponent getPosition() {
        return position;
    }

    public void setPosition(PositionComponent position) {
        this.position = position;
    }

    public StateComponent getState() {
        return state;
    }

    public void setState(StateComponent state) {
        this.state = state;
    }

    public void interact(Player player){
        System.out.println("Interacting with " + this.name);
    }

}

