package com.empire.rpg.Object;

import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.StateComponent;

import java.util.List;

public class Coffre extends Object {

    private boolean isOpen;
    private List<Item> contents;

    public Coffre(String name, PositionComponent position, StateComponent state, boolean isOpen, List<Item> contents) {
        super(name, position, state);
        this.isOpen = isOpen;
        this.contents = contents;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<Item> getContents() {
        return contents;
    }

    public void setContents(List<Item> contents) {
        this.contents = contents;
    }

    public void open(){
        System.out.println("Opening " + this.getName());
    }

}
