package com.empire.rpg.loader;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

public class MapData {

    @JsonProperty("height")
    private int height;

    @JsonProperty("infinite")
    private boolean infinite;

    @JsonProperty("layers")
    private List<Layer> layers;

    @JsonProperty("nextlayerid")
    private int nextlayerid;

    @JsonProperty("nextobjectid")
    private int nextobjectid;

    @JsonProperty("orientation")
    private String orientation;

    @JsonProperty("renderorder")
    private String renderorder;

    @JsonProperty("tiledversion")
    private String tiledversion;

    @JsonProperty("tileheight")
    private int tileheight;

    @JsonProperty("tilesets")
    private List<TiledMapTileSet> tilesets;

    @JsonProperty("tilewidth")
    private int tilewidth;

    @JsonProperty("type")
    private String type;

    @JsonProperty("version")
    private String version;

    @JsonProperty("width")
    private int width;

    // Getters et setters


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
