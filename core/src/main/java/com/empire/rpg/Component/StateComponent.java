package com.empire.rpg.Component;

import java.util.List;

public class StateComponent implements Component {
    private boolean currentState;
    private String nameState;

    public StateComponent(boolean currentState, String nameState) {
        this.currentState = currentState;
        this.nameState = nameState;
    }

    public boolean isCurrentState() {
        return currentState;
    }

    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
    }

    public String getNameState() {
        return nameState;
    }

    public void setNameState(String nameState) {
        this.nameState = nameState;
    }

    public void toggleState(){
        this.currentState = !this.currentState;
    }

}
