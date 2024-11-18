package com.empire.rpg.loader;

import java.util.List;

public class TiledMap {
    private List<TiledLayer> layers;
    private String index;

    public TiledMap(List<TiledLayer> layers, String index) {
        this.layers = layers;
        this.index = index;
    }

    public List<TiledLayer> getLayers() {
        return layers;
    }

    public String getIndex(String index) {
        return index;
    }
    public void setLayers(List<TiledLayer> layers) {
        this.layers = layers;
    }
}
