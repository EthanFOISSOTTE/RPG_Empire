package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.empire.rpg.player.Inventory.Inventory;
import com.empire.rpg.player.Player;
import com.badlogic.gdx.math.Vector2;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Player player;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private ShapeRenderer shapeRenderer; // Déclaration de ShapeRenderer
    private BitmapFont font;

    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;
    private static final Vector2 squarePosition = new Vector2(52 * 48, 45 * 49);
    private static final float INTERACTION_DISTANCE = 500;

    private boolean showInteractionFrame = false;
    private Inventory inventaire;  // Ajout de l'instance de l'inventaire

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer(); // Initialisation de ShapeRenderer
        font = new BitmapFont();

        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());
        player = new Player(collisionManager);

        inventaire = new Inventory(camera, batch);  // Initialisation de l'inventaire
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!showInteractionFrame) {
            player.update(Gdx.graphics.getDeltaTime());
        }

        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        mapManager.renderLowerLayers(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        mapManager.renderUpperLayers(camera);

            // Gérer l'affichage et la mise à jour de l'inventaire
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                showInteractionFrame = !showInteractionFrame;   //inverse true en false et inversement
                inventaire.setShowInteractionFrame(showInteractionFrame);  // Active le cadre d'inventaire
            }
            // Mettre à jour l'inventaire pour gérer les entrées
            if (showInteractionFrame) {
                inventaire.update();  // Appel de update() pour gérer la navigation dans l'inventaire
                inventaire.render(player.getPosition());
            }

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
        shapeRenderer.dispose(); // Libère ShapeRenderer
        font.dispose();
        inventaire.dispose();  // Libère les ressources utilisées dans Quest
    }
}
