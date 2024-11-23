package com.empire.rpg.utils;

import java.io.Serializable;
import java.util.UUID;

public class SaveData implements Serializable {

    public String playerName;
    public float positionX;
    public float positionY;
    public int currentHealth;
    public int maxHealth;
    public UUID id;

    public SaveData(UUID id, String playerName, float positionX, float positionY, int currentHealth, int maxHealth) {
        this.id = id;
        this.playerName = playerName;
        this.positionX = positionX;
        this.positionY = positionY;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
    }


    public String getPlayerName() {
        return playerName;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }




}
