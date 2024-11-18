package com.empire.rpg.introScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Screen;
import com.empire.rpg.GameEngine;

import java.io.IOException;

public class Intro implements Screen {
    private Array<Texture> frames;
    private SpriteBatch batch;
    private float elapsedTime = 0;
    private float frameDuration = 1 / 30f; // Pour 30 FPS
    private Music introMusic;
    private boolean animationFinished = false;
    private GameEngine game;
    private MainMenuScreen mainMenuScreen;

    public Intro(GameEngine game) throws IOException {
        this.batch = new SpriteBatch();
        this.frames = new Array<>();
        this.game = game;
        this.mainMenuScreen = new MainMenuScreen(game);
    }

    @Override
    public void show() {
        // Chargement des frames d’animation
        int nombreDeFrames = 152; // Ajuste selon ton animation
        for (int i = 1; i <= nombreDeFrames; i++) {
            String filePath = "Anim/RPG_Empire_launching_animation/frame_" + String.format("%04d", i) + ".png";
            frames.add(new Texture(Gdx.files.internal(filePath)));
        }

        // Charger et jouer la musique d'intro
        introMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/introScreen.mp3"));
        introMusic.play();
    }

    @Override
    public void render(float delta) {
        if (!animationFinished) {
            elapsedTime += delta;
            int currentFrame = (int) (elapsedTime / frameDuration) % frames.size;

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Centrer l'image
            Texture currentTexture = frames.get(currentFrame);
            float x = (Gdx.graphics.getWidth() - currentTexture.getWidth()) / 2f;
            float y = (Gdx.graphics.getHeight() - currentTexture.getHeight()) / 2f;

            batch.begin();
            batch.draw(currentTexture, x, y);
            batch.end();

            // Vérifier si l’animation est terminée
            if (elapsedTime >= frameDuration * frames.size) {
                animationFinished = true;
                introMusic.stop(); // Arrêter la musique si elle est terminée
            }
        } else {
            // Afficher un message pour indiquer d'appuyer sur Enter
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            // Reprendre la derniere frame de l'animation
            batch.draw(frames.get(frames.size - 1), (Gdx.graphics.getWidth() - frames.get(frames.size - 1).getWidth()) / 2f, (Gdx.graphics.getHeight() - frames.get(frames.size - 1).getHeight()) / 2f);
            // Dessiner un texte ici pour indiquer "Appuyez sur Enter"
            BitmapFont font = new BitmapFont();
            font.getData().setScale(2);
            font.setColor(Color.WHITE);
            font.draw(batch, "Appuyez sur Enter", Gdx.graphics.getWidth() / 2f - 150, Gdx.graphics.getHeight() / 2.25f);


            batch.end();

            // Vérifier l'appui sur "Enter" pour démarrer le jeu
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.setScreen(mainMenuScreen);
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
        // Redimensionner les frames d'animation
        for (Texture frame : frames) {
            frame.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
