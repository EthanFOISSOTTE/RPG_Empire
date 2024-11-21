package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.component.Component;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.map.MapManager;
import com.empire.rpg.map.MobManager;
import com.empire.rpg.map.ZoneManager;
import com.empire.rpg.ui.PlayerUI;
import com.empire.rpg.ui.MobUI;
import com.empire.rpg.ui.ZoneUI;
import com.empire.rpg.entity.mob.MobFactory;
import com.empire.rpg.component.pathfinding.Pathfinding;
import com.empire.rpg.entity.mob.Mob;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private CollisionHandler collisionHandler;
    private PlayerCharacter player;
    private PlayerUI playerUI;
    private SoundManager soundManager;
    private Pathfinding pathfinding;
    private MobManager mobManager;
    private MobUI mobUI;
    private ZoneManager zoneManager;
    private ZoneUI zoneUI;
    private OrthographicCamera uiCamera;

    private DebugRenderer debugRenderer;
    private boolean debugMode = false;

    // Taille de l'écran de jeu (16:9 | 480p)
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Initialiser la caméra de l'UI
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        // Initialiser collisionHandler
        collisionHandler = new CollisionHandler(collisionManager);

        // Initialiser le pathfinding
        pathfinding = new Pathfinding(collisionManager);
        // Définit le pathfinding global pour tous les mobs
        MobFactory.setPathfinding(pathfinding);

        // Initialiser le MobManager
        mobManager = new MobManager(collisionManager);
        // Initialiser le MobUI
        mobUI = new MobUI();

        // Initialiser le ZoneManager et charger les zones
        zoneManager = new ZoneManager();
        zoneManager.loadZonesFromMap(mapManager.getTiledMap());

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Appeler initializeGame() pour initialiser le joueur et l'UI
        initializeGame();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rendre les couches inférieures (en-dessous du joueur)
        mapManager.renderLowerLayers(camera);

        // Mettre à jour le joueur
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime, collisionManager);

        // Vérifier si le joueur est mort
        if (player.isDead()) {
            resetGame();
            return; // Arrêter le rendu pour cette frame
        }

        // Mettre à jour les mobs
        for (Mob mob : Mob.allMobs) {
            mob.update(deltaTime, player, camera);
        }
        collisionHandler.handleCollisions(player, Mob.allMobs);

        // Démarrer le batch pour dessiner les mobs, le joueur et les barres de vie
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Mob mob : Mob.allMobs) {
            // Rendu du mob
            batch.draw(
                mob.getCurrentTexture(),
                mob.getPosition().x + mob.getOffsetX(),
                mob.getPosition().y + mob.getOffsetY(),
                mob.getCurrentTexture().getRegionWidth() * mob.getScale(),
                mob.getCurrentTexture().getRegionHeight() * mob.getScale()
            );
        }
        // Rendu du joueur
        player.render(batch);
        for (Mob mob : Mob.allMobs) {
            // Rendu de la barre de vie du mob
            mobUI.render(batch, mob);
        }
        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapManager.renderUpperLayers(camera);

        // Activer/Désactiver le mode de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (debugMode) {
            debugRenderer.renderDebugBounds(camera, player, collisionManager);
        }

        // Mettre à jour la ZoneUI
        zoneUI.update();

        // Rendre l'UI du joueur et de la zone
        batch.setProjectionMatrix(uiCamera.combined); // Utiliser la caméra UI
        batch.begin();
        playerUI.render(batch);
        zoneUI.render(batch);
        batch.end();

        // Mettre à jour la caméra pour suivre le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiCamera.setToOrtho(false, width, height); // Mettre à jour la caméra UI
    }

    private void resetGame() {
        System.out.println("Redémarrage du jeu...");

        // Dispose des ressources existantes
        if (player != null) {
            player.dispose();
            player = null;
        }
        if (playerUI != null) {
            playerUI.dispose();
            playerUI = null;
        }
        if (zoneUI != null) {
            zoneUI.dispose();
            zoneUI = null;
        }
        // Disposer les mobs
        for (Mob mob : Mob.allMobs) {
            mob.dispose();
        }
        Mob.allMobs.clear();

        // Appeler initializeGame() pour recréer le joueur et l'UI
        initializeGame();
    }

    private void initializeGame() {
        // Création d'une map de composants avec PositionComponent et HealthComponent
        Map<Class<? extends Component>, Component> components = new HashMap<>();
        components.put(HealthComponent.class, new HealthComponent(90, 100));
        components.put(PositionComponent.class, new PositionComponent(4800f, 4800f));

        // Recharger les mobs depuis la carte
        mobManager.loadMobsFromMap(mapManager.getTiledMap());

        // Création et initialisation de l'instance de PlayerCharacter
        player = new PlayerCharacter(2.0f, UUID.randomUUID(), "Hero", components);

        // Initialiser l'UI du joueur
        playerUI = new PlayerUI(player);

        // Initialiser le ZoneUI
        zoneUI = new ZoneUI(player, zoneManager);

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        if (player != null) {
            player.dispose();
        }
        if (playerUI != null) {
            playerUI.dispose();
        }
        if (zoneUI != null) {
            zoneUI.dispose();
        }
        if (pathfinding != null) {
            pathfinding.dispose();
        }
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }
        if (mobUI != null) {
            mobUI.dispose();
        }
        if (soundManager != null) {
            soundManager.dispose();
        }
        for (Mob mob : Mob.allMobs) {
            mob.dispose();
        }
        Mob.allMobs.clear();
    }
}
