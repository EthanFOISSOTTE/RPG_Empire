package com.empire.rpg.Component;

public class SignComponent implements Component {
    private boolean isOpen;
    private final String message;

    public SignComponent(String message) {
        this.isOpen = false; // Par défaut, la pancarte est fermée
        this.message = message;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void toggle() {
        isOpen = !isOpen;
    }

    public String getMessage() {
        return message;
    }
}

