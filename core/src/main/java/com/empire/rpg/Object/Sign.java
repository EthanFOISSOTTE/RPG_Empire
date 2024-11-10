package com.empire.rpg.Object;

import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.StateComponent;

public class Sign extends Object{
    private String message;
    private boolean isVisible;

    public Sign(String name, PositionComponent position, StateComponent state, String message, boolean isVisible) {
        super(name, position, state);
        this.message = message;
        this.isVisible = isVisible;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void read(){
        System.out.println(this.message);
    }

    public void hide(){
        this.isVisible = false;
    }

    public void display(){
        this.isVisible = true;
    }
}
