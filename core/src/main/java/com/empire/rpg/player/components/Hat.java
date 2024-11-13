package com.empire.rpg.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.player.animations.spritesheet.HatSpriteSheet;

public class Hat {
    private HatSpriteSheet spriteSheet;
    private TextureRegion currentFrame;

    public Hat() {
        spriteSheet = new HatSpriteSheet();
    }

    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    public void render(Batch batch, float x, float y, float scale) {
        if (currentFrame != null) {
            float width = currentFrame.getRegionWidth() * scale;
            float height = currentFrame.getRegionHeight() * scale;

            // Ajuster x et y pour le point d'ancrage
            float adjustedX = x - (width / 2);
            float adjustedY = y - (height / 2);

            batch.draw(currentFrame, adjustedX, adjustedY, width, height);
        }
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public HatSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
