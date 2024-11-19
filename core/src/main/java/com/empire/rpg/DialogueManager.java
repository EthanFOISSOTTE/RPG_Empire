package com.empire.rpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DialogueManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean showDialogueFrame = false;
    private ShapeRenderer shapeRenderer;

    public DialogueManager(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(SpriteBatch batch, Vector2 playerPosition){
        // handleInput(); // Permet de passer au dialogue suivant

        if(showDialogueFrame){
            drawDialogueFrame(playerPosition);
        }
    }

    private void drawDialogueFrame(Vector2 playerPosition){
        float frameWidth = 450;
        float frameHeight = 100;
        float frameX = playerPosition.x - frameWidth / 2;
        float frameY = playerPosition.y - 200;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();
    }

    public void setShowDialogueFrame(boolean showDialogue) {
        this.showDialogueFrame = showDialogue;
    }
}
