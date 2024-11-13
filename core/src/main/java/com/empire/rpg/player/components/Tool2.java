package com.empire.rpg.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tool2 {
    private TextureRegion currentFrame;

    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    public void render(Batch batch, float x, float y, float scale) {
        if (currentFrame != null) {
            float width = currentFrame.getRegionWidth() * scale;
            float height = currentFrame.getRegionHeight() * scale;
            float adjustedX = x - (width / 2);
            float adjustedY = y - (height / 2);
            batch.draw(currentFrame, adjustedX, adjustedY, width, height);
        }
    }

    public void dispose() {
        // Si vous avez des ressources à libérer
    }
}
