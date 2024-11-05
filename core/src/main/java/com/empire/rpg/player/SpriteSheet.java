package com.empire.rpg.player;

// Importations
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Classe SpriteSheet
public class SpriteSheet {
    // Méthode pour obtenir une région de texture par ID
    public TextureRegion getTextureRegionById(TextureRegion[][] tmp, int id) {
        int row = id / 16;
        int col = id % 16;
        return tmp[row][col];
    }
}
