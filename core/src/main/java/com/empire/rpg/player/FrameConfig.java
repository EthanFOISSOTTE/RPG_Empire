package com.empire.rpg.player;

// Classe FrameConfig
public class FrameConfig {
    public int id;
    public boolean flip;
    public float duration;

    // Constructeur
    public FrameConfig(int id, boolean flip, float duration) {
        this.id = id;
        this.flip = flip;
        this.duration = duration;
    }
}
