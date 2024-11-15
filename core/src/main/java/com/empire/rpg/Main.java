package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.empire.rpg.player.Player;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Player player;
    private MapManager mapManager;
    private CollisionManager collisionManager;

    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());
        player = new Player(collisionManager);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mise à jour du joueur
        player.update(Gdx.graphics.getDeltaTime());

        // La caméra suit la position du joueur
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        // Rendre les couches inférieures (sous le joueur)
        mapManager.renderLowerLayers(camera);

        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch); // Rendre le joueur sans vérification de collision
        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapManager.renderUpperLayers(camera);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        player.dispose();
    }
}
