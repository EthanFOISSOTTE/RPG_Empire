package com.empire.rpg.introScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.empire.rpg.GameEngine;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.entity.Player;
import com.empire.rpg.loader.MapLoader;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Player player;
    private MapLoader mapLoader;
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    public MainMenuScreen(GameEngine game) throws IOException {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        mapLoader = new MapLoader("rpg-map-ecs.json", camera);
        player = new Player("player1", Map.of(
            PositionComponent, new PositionComponent(0, 0),
            CollisionComponent,new CollisionComponent(true)
        ), UUID.randomUUID());

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mise à jour du joueur
        player.update(Gdx.graphics.getDeltaTime());

        // La caméra suit la position du joueur
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        // Rendre les couches inférieures (sous le joueur)
        mapLoader.renderLowerLayers(camera);

        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch); // Rendre le joueur sans vérification de collision
        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapLoader.renderUpperLayers(camera);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
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
        mapLoader.dispose();
        player.dispose();
    }

}
