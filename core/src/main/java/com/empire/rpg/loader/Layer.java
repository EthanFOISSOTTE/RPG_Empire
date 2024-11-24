package com.empire.rpg.loader;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Layer {
    @JsonProperty("data")
    private List<Integer> data;

    @JsonProperty("height")
    private int height;

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("opacity")
    private float opacity;

    @JsonProperty("type")
    private String type;

    @JsonProperty("visible")
    private boolean visible;

    @JsonProperty("width")
    private int width;

    @JsonProperty("x")
    private int x;

    @JsonProperty("y")
    private int y;

    // Getters et setters
    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

