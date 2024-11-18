package com.empire.rpg.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Screen;
import com.empire.rpg.GameEngine;

import java.io.IOException;

public class Intro implements Screen {
    private final Array<Texture> frames;
    private final SpriteBatch batch;
    private final GameEngine game;
    private float elapsedTime = 0;
    private boolean animationFinished = false;
    private Music introMusic;

    public Intro(GameEngine game) throws IOException {
        this.batch = new SpriteBatch();
        this.frames = new Array<>();
        this.game = game;
    }

    @Override
    public void show() {
        // Chargement des frames d'animation
        int nombreDeFrames = 152; // Ajuste selon ton animation
        for (int i = 1; i <= nombreDeFrames; i++) {
            String filePath = "assets/Anim/RPG_Empire_launching_animation/frame_" + String.format("%04d", i) + ".png";
            FileHandle fileHandle = Gdx.files.internal(filePath);
            if (fileHandle.exists() && !fileHandle.isDirectory()) {
                frames.add(new Texture(fileHandle));
            } else {
                System.err.println("Frame manquante : " + filePath);
            }
        }

        if (frames.size == 0) {
            throw new RuntimeException("Aucune frame d'animation n'a été chargée !");
        }

        // Charger et jouer la musique d'intro
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Audio/introScreen.mp3"));
        introMusic.setLooping(false);
        introMusic.play();
    }

    @Override
    public void render(float delta) {
        if (!animationFinished) {
            elapsedTime += delta;
            // Pour 30 FPS
            float frameDuration = 1 / 30f;
            int currentFrame = (int) (elapsedTime / frameDuration);

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Afficher la frame courante
            if (currentFrame < frames.size) {
                Texture currentTexture = frames.get(currentFrame);
                float x = (Gdx.graphics.getWidth() - currentTexture.getWidth()) / 2f;
                float y = (Gdx.graphics.getHeight() - currentTexture.getHeight()) / 2f;

                batch.begin();
                batch.draw(currentTexture, x, y);
                batch.end();
            } else {
                animationFinished = true;
                if (introMusic != null) {
                    introMusic.stop();
                }
                // Passer à l'écran du menu principal
                game.setScreen(new MainMenuScreen());
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture frame : frames) {
            frame.dispose();
        }
        if (introMusic != null) {
            introMusic.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Optionnel : Ajouter des ajustements si nécessaire
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {
        if (introMusic != null) {
            introMusic.stop();
        }
    }
}
