package com.empire.rpg.introScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.empire.rpg.GameEngine;

public class Intro implements Screen {
    private Array<Texture> frames;
    private SpriteBatch batch;
    private float elapsedTime = 0;
    private float frameDuration = 1 / 30f; // Pour 30 FPS
    private GameEngine game;



    public Intro(GameEngine game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.frames = new Array<>();


    }

    @Override
    public void show() {
        // Chargement des images lorsque l'écran est affiché pour la première fois
        int nombreDeFrames = 150; // Ajuster selon le nombre de frames de l'animation
        for (int i = 1; i <= nombreDeFrames; i++) {
            String filePath = "Anim.RPG_Empire_launching_animation/frame_" + String.format("%04d", i) + ".png";
            frames.add(new Texture(Gdx.files.internal(filePath)));
        }
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        int currentFrame = (int) (elapsedTime / frameDuration) % frames.size;

        batch.begin();
        batch.draw(frames.get(currentFrame), 0, 0);
        batch.end();

        // Passer au menu principal après la vidéo
        if (elapsedTime >= frameDuration * frames.size) {
            game.setScreen(new Intro(game));
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture frame : frames) {
            frame.dispose();
        }
    }
}

