package com.empire.rpg.component;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    private float x;
    private float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Nouvelle méthode getPosition qui retourne un objet Vector2
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {
        // Implémentation vide
    }

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}

