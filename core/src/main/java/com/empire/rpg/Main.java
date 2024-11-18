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
import com.badlogic.gdx.math.Vector2;

import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.WeaponComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.component.Component;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.ui.PlayerUI;
import com.empire.rpg.entity.mob.MobFactory;
import com.empire.rpg.component.pathfinding.Pathfinding;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.CollisionHandler;

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

        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        // Création d'une map de composants avec PositionComponent et HealthComponent
        Map<Class<? extends Component>, Component> components = Map.of(
            HealthComponent.class, new HealthComponent(90, 100),
            PositionComponent.class, new PositionComponent(4800f, 4800f)
        );
        // Création et initialisation de l'instance de PlayerCharacter
        player = new PlayerCharacter(2.0f, UUID.randomUUID(), "Hero", components);
        // Initialiser l'UI du joueur
        playerUI = new PlayerUI(player);
        
        // Initialiser le pathfinding
        pathfinding = new Pathfinding(collisionManager);
        // Définit le pathfinding global pour tous les mobs
        MobFactory.setPathfinding(pathfinding);
        // Créer des mobs avec des comportements spécifiques
        MobFactory.createMob("gobelinrouge", new Vector2(4800, 4900), collisionManager);
        MobFactory.createMob("gobelinvert", new Vector2(4900, 4800), collisionManager);
        MobFactory.createMob("chatgris", new Vector2(5000, 5000), collisionManager);
        MobFactory.createMob("lapinblanc", new Vector2(5100, 5100), collisionManager);
        // Créer un gestionnaire de collisions
        collisionHandler = new CollisionHandler(collisionManager);

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
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

        // Mettre à jour les mobs
        for (Mob mob : Mob.allMobs) {
            mob.update(Gdx.graphics.getDeltaTime(), player, camera);
        }
        collisionHandler.handleCollisions(player, Mob.allMobs);

        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        for (Mob mob : Mob.allMobs) {
            batch.draw(mob.getCurrentTexture(), mob.getPosition().x, mob.getPosition().y);
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

        // Rendre l'UI du joueur (après le rendu des éléments du jeu)
        playerUI.render(batch);

        // Mettre à jour la caméra pour suivre le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
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
        pathfinding.dispose();
        debugRenderer.dispose();
        playerUI.dispose();
        if (soundManager != null) { soundManager.dispose(); }
        for (Mob mob : Mob.allMobs) { mob.dispose(); }
    }
}
