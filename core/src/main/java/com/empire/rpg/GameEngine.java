package com.empire.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.empire.rpg.loader.MapLoader;
import com.empire.rpg.loader.TiledMap;
import com.empire.rpg.screen.Intro;
import com.empire.rpg.screen.MainMenuScreen;
import com.empire.rpg.utils.Camera;


import java.io.IOException;

public class GameEngine extends Game {
    public static final String TITLE = "RPG Empire";
    public static final String VERSION = "0.1";
    public static final String MAP_PATH = "assets/rpg-map.tmx";
    private static final float INTRO_DURATION = 5f; // Durée de l'écran d'introduction

    private Camera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float elapsedTime = 0;
    private Batch batch;
    private BitmapFont font;
    private MainMenuScreen mainMenuScreen;
    private GameEngine game;

    public static void main(String[] args) {
        new LwjglApplication(new GameEngine(), TITLE, 1300, 800);
    }

    @Override
    public void create() {
        // Configurer le titre
        Gdx.graphics.setTitle(TITLE + " v" + VERSION);

        // Lancer l'écran d'introduction
        try {
            setScreen(new Intro(this));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    MapLoader mapLoader = new MapLoader();

    @Override
    public void render() {
        super.render();
        if (getScreen() instanceof Intro) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            if (elapsedTime >= INTRO_DURATION) {
                setScreen(mainMenuScreen);
            }

        }else if (getScreen() instanceof MainMenuScreen) {
            mainMenuScreen.render(Gdx.graphics.getDeltaTime());

        }
    }
}
