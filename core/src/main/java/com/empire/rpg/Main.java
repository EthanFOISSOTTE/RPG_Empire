package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.WeaponComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.component.Component;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.screen.*;
import com.empire.rpg.ui.PlayerUI;

import com.empire.rpg.entity.player.Inventory.Inventory;
import com.empire.rpg.shop.Shop;
import com.empire.rpg.shop.Vente;





public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private PlayerCharacter player;
    private SoundManager soundManager;
    private DebugRenderer debugRenderer;
    private PlayerUI playerUI;
    private Screen currentScreen;
    private boolean animationFinished = false;
    private boolean debugMode = false;
    private PauseScreen pauseScreen;

    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;
    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    //inventaire
    private Inventory inventaire; // Instance de l'inventaire
    private boolean showInteractionFrame = false;
    private ShapeRenderer shapeRenderer; // Déclaration de ShapeRenderer
    private BitmapFont font;

    //PPT
    private InteractionImageManager interactionImageManager;

    //Shop
    private Shop shop; // Instance du shop
    private boolean showShopFrame = false;

    //Shop
    private Vente vente; // Instance du shop
    private boolean showVenteFrame = false;

    @Override
    public void create() {
        // Charger l'écran d'introduction avec un Runnable pour afficher le menu principal
        IntroScreen introScreen = new IntroScreen(() -> setScreen(new MainMenuScreen()));
        setScreen(introScreen);


        // Charger les ressources
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        pauseScreen = new PauseScreen();
        shapeRenderer = new ShapeRenderer(); // Initialisation de ShapeRenderer
        font = new BitmapFont();


        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        // Création d'une map de composants avec PositionComponent et HealthComponent
        Map<Class<? extends Component>, Component> components = Map.of(
            HealthComponent.class, new HealthComponent(90, 100),
            PositionComponent.class, new PositionComponent(1306f,1831f)
        );

        // Création et initialisation de l'instance de PlayerCharacter
        player = new PlayerCharacter(2.0f, UUID.randomUUID(), "Hero", components);

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Initialiser l'UI du joueur
        playerUI = new PlayerUI(player);

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        inventaire = new Inventory(camera, batch);  // Initialisation de l'inventaire

        shop = new Shop(camera, batch);  // Initialisation du shop

        vente = new Vente(camera, batch);  // Initialisation de vente

        // Initialiser l'InteractionImageManager
        interactionImageManager = new InteractionImageManager(
            8424, 2530, 48, 48, // Coordonnées et taille du carré
            // Chemins des images
            new String[]{
                "PPT/image1.png",
                "PPT/image2.png",
                "PPT/image3.png",
                "PPT/image4.png",
                "PPT/image5.png",
                "PPT/image6.png",
                "PPT/image7.png"
            }
        );

    }

    public void setScreen(Screen screen) {
        currentScreen = screen;
        if (!(currentScreen instanceof IntroScreen)) {
            return;
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        public void render() {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gestion de la touche Echap
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseScreen.toggleVisibility(); // Basculer la visibilité du menu Pause
        }

        // Si le menu Pause est visible, ne pas rendre le reste du jeu
        if (pauseScreen.isVisible()) {
            pauseScreen.render(null); // Passer `null` ou une instance de `Graphics` si nécessaire
            return;
        }

        // Rendu normal du jeu
        mapManager.renderLowerLayers(camera);
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime, collisionManager);
            // Rendre les couches inférieures (en-dessous du joueur)
            mapManager.renderLowerLayers(camera);

            // Mettre à jour le joueur
            float deltaTime = Gdx.graphics.getDeltaTime();
            player.update(deltaTime, collisionManager);

            // Démarrer le batch pour dessiner le joueur
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            player.render(batch);
            batch.end();



            // Rendre les couches supérieures (au-dessus du joueur)
            mapManager.renderUpperLayers(camera);


            // Mettre à jour l'inventaire pour gérer les entrées
            if (!showInteractionFrame) {
                inventaire.update();  // Appel de update() pour gérer la navigation dans l'inventaire
                inventaire.render(new Vector2(player.getX(),player.getY()));
            }

            if (!showShopFrame){
                shop.update();  // Appel de update() pour gérer la navigation dans le shop
                shop.render(new Vector2(player.getX(),player.getY()));
            }

            if (!showShopFrame){
                vente.update();  // Appel de update() pour gérer la navigation dans le vente
                vente.render(new Vector2(player.getX(),player.getY()));
            }


            if (Inventory.getShowInteractionFrame()) {
                inventaire.render(new Vector2(player.getX(), player.getY()));  // Rendu uniquement si l'inventaire est actif
            }


            // Rendre l'UI du joueur (après le rendu des éléments du jeu)
            playerUI.render(batch);

            // Activer/Désactiver le mode de débogage
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
                debugMode = !debugMode;
            }
            if (debugMode) {
                debugRenderer.renderDebugBounds(camera, player, collisionManager);
            }

            // Mettre à jour la caméra pour suivre le joueur
            camera.position.set(player.getX(), player.getY(), 0);
            camera.update();

            // Mise à jour et rendu de l'interaction avec les images
            interactionImageManager.update(new Vector2(player.getX(), player.getY()));
            interactionImageManager.render(batch);

        }
        playerUI.render(batch);

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        if (currentScreen != null) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void resize ( int width, int height){
        viewport.update(width, height);
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void dispose () {
        player.dispose();
        if (soundManager != null) {
            soundManager.dispose();
        }
        debugRenderer.dispose();
        playerUI.dispose();

        if (currentScreen != null) {
            currentScreen.dispose();
        }

        shapeRenderer.dispose(); // Libère ShapeRenderer
        font.dispose();
        inventaire.dispose();  // Libère les ressources utilisées dans inventaire


        shop.dispose();  // Libère les ressources utilisées dans shop
        vente.dispose();  // Libère les ressources utilisées dans vente

        // Autres libérations de ressources...
        interactionImageManager.dispose();

    }
}


