package com.empire.rpg.player.components;

import com.badlogic.gdx.math.Rectangle;

public class CollisionComponent {
    private Rectangle boundingBox;
    private float offsetX;
    private float offsetY;

    public CollisionComponent(float x, float y, float width, float height, float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        boundingBox = new Rectangle(x + offsetX, y + offsetY, width, height);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setPosition(float x, float y) {
        boundingBox.setPosition(x + offsetX, y + offsetY);
    }

    public void setSize(float width, float height) {
        boundingBox.setSize(width, height);
    }

    public void setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    // Getters pour offsetX et offsetY
    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }
}
