package com.empire.rpg.screen;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PauseScreen {
    private boolean visible;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Graphics g;

    public PauseScreen() {
        this.visible = false;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void toggleVisibility() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(Graphics g) {
        if (!visible) return;
        // Dessiner le menu pause
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "Menu Pause", 350, 200);
        font.draw(batch, "1. Reprendre", 350, 250);
        font.draw(batch, "2. Quitter", 350, 300);
        batch.end();
    }
}
