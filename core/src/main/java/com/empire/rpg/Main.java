package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.Map;
import java.util.UUID;

import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.entity.Entity;
import com.empire.rpg.entity.EntityManager;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.screen.*;
import com.empire.rpg.ui.PlayerUI;
import com.empire.rpg.utils.SaveData;
import com.empire.rpg.utils.SaveManager;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private PlayerCharacter player;
    private DebugRenderer debugRenderer;
    private PlayerUI playerUI;
    private Screen currentScreen;
    private PauseScreen pauseScreen;
    private EntityManager entityManager;
    private String playerName = "Hero";
    private boolean debugMode = false;
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;
    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void create() {
        // Initialisation
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        debugRenderer = new DebugRenderer();
        // Création du joueur
        UUID playerId = UUID.randomUUID();
        player = new PlayerCharacter(
            2.0f,
            playerId,
            playerName,
            Map.of(
                HealthComponent.class, new HealthComponent(90, 100),
                PositionComponent.class, new PositionComponent(4800f, 4800f)
            )
        );
        entityManager = new EntityManager(player.getName()) {
            @Override
            public void createEntity(UUID id) {
                new Entity(entityManager.getName(), Map.of(
                    HealthComponent.class, new HealthComponent(100, 100),
                    PositionComponent.class, new PositionComponent(0, 0)
                ), id) {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public Entity addEntity() {
                        return null;
                    }

                    @Override
                    public Entity removeEntity(String name) {
                        return null;
                    }
                };
            }

            @Override
            public Entity addEntity() {
                return null;
            }
        };




        // Ajouter le joueur à l'EntityManager
        entityManager.addEntity(playerId, player);
        entityManager.setName(playerId, playerName);

        // Initialisation des autres composants
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());
        playerUI = new PlayerUI(player);
        pauseScreen = new PauseScreen(player,entityManager.getName());

        // Charger l'écran principal
        setScreen(new IntroScreen(() -> setScreen(new MainMenuScreen())));
    }


    public void setScreen(Screen screen) {
        currentScreen = screen;
    }

    public void updatePlayerName(String playerName) {
        if (entityManager != null && player != null) {
            entityManager.setName(player.getId(), playerName);
            player.setName(playerName);
        }
    }

    private void loadGame() {
        SaveData data = SaveManager.loadGame();
        if (data != null) {
            player.setName(data.playerName);
            player.setPosition(data.positionX, data.positionY);

            HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
            if (health != null) {
                health.setCurrentHealthPoints(data.currentHealth);
            } else {
                System.err.println("Erreur : HealthComponent introuvable !");
            }

            pauseScreen.setPlayer(player); // Mise à jour du joueur dans PauseScreen
            System.out.println("Jeu chargé : Nom=" + data.playerName + ", Position=(" + data.positionX + ", " + data.positionY + ")");
        } else {
            System.out.println("Aucune sauvegarde trouvée.");
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gestion de la pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseScreen.toggleVisibility();
        }

        // Rendu du menu pause
        if (pauseScreen.isVisible()) {
            pauseScreen.render(null);
            return;
        }

        // Rendu normal du jeu
        mapManager.renderLowerLayers(camera);
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime, collisionManager);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        mapManager.renderUpperLayers(camera);
        playerUI.render(batch);

        if (debugMode) {
            debugRenderer.renderDebugBounds(camera, player, collisionManager);
        }

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        if (currentScreen != null) {
            currentScreen.render(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        player.dispose();
        debugRenderer.dispose();
        playerUI.dispose();

        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
