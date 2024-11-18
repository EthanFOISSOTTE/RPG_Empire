package com.empire.rpg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PNJ {
    private Vector2 position;
    private Texture pnjTexture;
    private TextureRegion currentFrame;
    private float size = 2.0f;
    private Rectangle collisionRectangle; // Rectangle de collision du PNJ
    private final CollisionManager collisionManager;

    public PNJ(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.position = new Vector2(49 * 50 + 24, 45 * 41 + 24);
        loadAnimations();
    }

    // Charger les animations de stand et définir la frame initiale
    private void loadAnimations() {
        pnjTexture = new Texture(Gdx.files.internal("PNJ/radagast.png"));
        TextureRegion[][] tmp = TextureRegion.split(pnjTexture, 64, 64);
        currentFrame = tmp[0][0];
    }

    // Rendre le PNJ avec la frame actuelle
    public void render(SpriteBatch batch) {
        float width = 64 * size;
        float height = 64 * size;
        batch.draw(currentFrame, position.x - width / 2, position.y - height / 2, width, height);
    }

    // Libérer la mémoire de la texture
    public void dispose() {
        if (pnjTexture != null) {
            pnjTexture.dispose();
        }
    }
}
