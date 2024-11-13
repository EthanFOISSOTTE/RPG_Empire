package com.empire.rpg.player.animations.spritesheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.player.utils.Constants;

public class SpriteSheet {
    private Texture texture;
    private TextureRegion[][] frames;

    public SpriteSheet() {
        // Constructeur sans paramètres
    }

    public SpriteSheet(String texturePath) {
        texture = new Texture(Gdx.files.internal(texturePath));
        frames = TextureRegion.split(texture, Constants.SPRITE_WIDTH, Constants.SPRITE_HEIGHT);
    }

    public void setTexture(String texturePath) {
        if (texture != null) {
            texture.dispose();
        }
        texture = new Texture(Gdx.files.internal(texturePath));
        frames = TextureRegion.split(texture, Constants.SPRITE_WIDTH, Constants.SPRITE_HEIGHT);
    }

    public TextureRegion getFrame(int row, int col) {
        return frames[row][col];
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
