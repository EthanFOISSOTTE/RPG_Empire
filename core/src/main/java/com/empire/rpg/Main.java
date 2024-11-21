package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.component.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.empire.rpg.entity.PNJ;
import com.empire.rpg.quest.Quest;
import com.empire.rpg.quest.QuestPlayer;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.ui.PlayerUI;
import com.empire.rpg.entity.mob.MobFactory;
import com.empire.rpg.component.pathfinding.Pathfinding;
import com.empire.rpg.CollisionHandler;
import com.empire.rpg.entity.mob.Mob;

import com.empire.rpg.entity.player.Inventory.Inventory;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private CollisionHandler collisionHandler;
    private PlayerCharacter player;
    private PositionComponent positionComponent;
    private SoundManager soundManager;
    private DebugRenderer debugRenderer;
    private PlayerUI playerUI;
    private Texture F_Key_Texture;
    private Texture questBoardTexture;
    private boolean debugMode = false;
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;
    private static final Vector2 squarePosition = new Vector2(52 * 48, 45 * 49);  // Position du tableau de quête dans le monde
    private boolean showInteractionFrame = false;  // Booléen pour savoir si le cadre d'interaction est affiché
    private boolean showQuestPlayer = false;
    private boolean showDialogueFrame = false;
    private Inventory inventaire; // Instance de l'inventaire
    private ShapeRenderer shapeRenderer; // Déclaration de ShapeRenderer
    private BitmapFont font;
    private Quest quest;  // Objet Quest qui gère les quêtes dans le jeu
    private QuestPlayer questPlayer;
    private DialogueManager dialogue; // Objet qui gère les dialogues avec les PNJ
    private PNJ pnj_radagast;
    private PNJ pnj_duc;
    private static final float INTERACTION_DISTANCE = 70;  // Distance d'interaction avec un objet
    private static final float DISPLAY_DISTANCE = 500;
    private static final float SQUARE_SIZE = 64;
    private static final float INTERACTION_DISTANCE_PNJ = 70;
    private Pathfinding pathfinding;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer(); // Initialisation de ShapeRenderer
        font = new BitmapFont();

        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        Map<Class<? extends Component>, Component> Radagast = Map.of(
            PositionComponent.class, new PositionComponent(49 * 48 + 24, 44 * 48 + 24),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            TextureComponent.class, new TextureComponent(new Texture("PNJ/Radagast.png"), 48, 48, 0, 0, 2.0f)
        );
        pnj_radagast = new PNJ("Radagast", Radagast, UUID.randomUUID());
        pnj_radagast.setName("Radagast");

        Map<Class<? extends Component>, Component> Duc_Michel = Map.of(
            PositionComponent.class, new PositionComponent(177* 48 + 24, 68 * 48 + 24),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            TextureComponent.class, new TextureComponent(new Texture("PNJ/Duc_Michel.png"), 48, 48, 0, 0, 2.0f)
        );
        pnj_duc = new PNJ("Duc_Michel", Duc_Michel, UUID.randomUUID());
        pnj_duc.setName("Duc_Michel");

        // Initialisation des objets liés aux quêtes
        quest = new Quest(camera, batch);  // Création de l'objet Quest pour gérer les quêtes
        questPlayer = new QuestPlayer(camera, batch, quest.getQuestList());  // Création de l'objet QuestPlayer pour gérer l'affichage des quêtes
        dialogue = new DialogueManager(camera, batch); // Initialisation du gestionnaire de dialogues
        // Chargement de la texture de l'icône du tableau de quête
        questBoardTexture = new Texture(Gdx.files.internal("exclamation.png"));  // Chemin vers l'image du tableau de quêtes
        F_Key_Texture = new Texture(Gdx.files.internal("Images/F_Key.png"));  // Chemin vers l'image de la touche F

        // Création d'une map de composants avec PositionComponent et HealthComponent
        // Création du Map avec les composants nécessaires
        Map<Class<? extends Component>, Component> components = new HashMap<>();
        components.put(HealthComponent.class, new HealthComponent(90, 100));
        components.put(PositionComponent.class, new PositionComponent(180 * 48 + 24, 55 * 48 + 24)); // Spawn au port de la ville

        this.player = new PlayerCharacter(2.0f, UUID.randomUUID(), "Hero", components);
        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Initialiser l'UI du joueur
        playerUI = new PlayerUI(player);

        // Initialiser le pathfinding
        pathfinding = new Pathfinding(collisionManager);
        // Définit le pathfinding global pour tous les mobs
        MobFactory.setPathfinding(pathfinding);
        // Créer des mobs avec des comportements spécifiques
        MobFactory.createMob("goblin", new Vector2(4700, 4900), collisionManager);
        MobFactory.createMob("ogre", new Vector2(4800, 4900), collisionManager);
        MobFactory.createMob("orc", new Vector2(4900, 4900), collisionManager);
        MobFactory.createMob("rabbit", new Vector2(4700, 5000), collisionManager);
        MobFactory.createMob("rabbit-horned", new Vector2(4800, 5000), collisionManager);
        // Créer un gestionnaire de collisions
        collisionHandler = new CollisionHandler(collisionManager);

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        inventaire = new Inventory(camera, batch);  // Initialisation de l'inventaire
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rendre les couches inférieures (en-dessous du joueur)
        mapManager.renderLowerLayers(camera);

        if (canMove()) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            player.update(deltaTime, collisionManager);
        }
        // Mettre à jour les mobs
        for (Mob mob : Mob.allMobs) {
            mob.update(Gdx.graphics.getDeltaTime(), player, camera);
        }
        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Mob mob : Mob.allMobs) {
            // Appliquer le facteur de zoom lors du rendu
            batch.draw(
                mob.getCurrentTexture(),
                mob.getPosition().x + mob.getOffsetX(),
                mob.getPosition().y + mob.getOffsetY(),
                mob.getCurrentTexture().getRegionWidth() * mob.getScale(),
                mob.getCurrentTexture().getRegionHeight() * mob.getScale()
            );
        }
        pnj_radagast.render(batch);
        pnj_duc.render(batch);
        player.render(batch);
        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapManager.renderUpperLayers(camera);

        dialogue.render(batch, player.getPlayerPosition());

        // Activer/Désactiver le mode de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (debugMode) {
            debugRenderer.renderDebugBounds(camera, player, collisionManager);
        }


        // Mettre à jour l'inventaire pour gérer les entrées
        if (!showInteractionFrame) {
            inventaire.update();  // Appel de update() pour gérer la navigation dans l'inventaire
            inventaire.render(new Vector2(player.getX(),player.getY()));
        }

        // Affichage de l'icône de quête si le joueur est proche du tableau
        if (isPlayerNearSquare(player.getPlayerPosition(), squarePosition, DISPLAY_DISTANCE)) {
            batch.begin();
            batch.draw(questBoardTexture, squarePosition.x + 19, squarePosition.y + SQUARE_SIZE);  // Affiche l'icône du tableau de quête
            batch.end();
        }

// Region
        if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), pnj_radagast.getPosition(), INTERACTION_DISTANCE)) {
            batch.begin();

            // Dessinez la texture de la touche "F"
            batch.draw(F_Key_Texture, pnj_radagast.getPosition().x + 27, pnj_radagast.getPosition().y + 85, 24, 24);

            // Dessinez le texte "interagir" à côté de l'image
            font.draw(batch, " interagir", pnj_radagast.getPosition().x + 55, pnj_radagast.getPosition().y + 105);

            batch.end();

            // Détecter l'appui sur la touche "F"
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                dialogue.startDialogue("Radagast");
            }
        }


        if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), pnj_duc.getPosition(), INTERACTION_DISTANCE)) {
            batch.begin();

            // Dessinez la texture de la touche "F"
            batch.draw(F_Key_Texture, pnj_duc.getPosition().x + 52, pnj_duc.getPosition().y + 78, 24, 24);

            // Dessinez le texte "interagir" à côté de l'image
//            font.draw(batch, " INTERAGIR", pnj_duc.getPosition().x + 50, pnj_duc.getPosition().y + 90);

            batch.end();

            // Détecter l'appui sur la touche "F"
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                dialogue.startDialogue("Duc Michel");
            }
        }

// Afficher le cadre de dialogue si activé
        if (dialogue.isShowDialogueFrame()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                dialogue.nextDialogue(); // Avance au dialogue suivant
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                dialogue.closeDialogue(); // Ferme le cadre
            }
            dialogue.render(batch, player.getPlayerPosition());
        }

        // Cacher le cadre d'interaction avec la touche ESCAPE
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showDialogueFrame = false;  // Désactive le cadre d'interaction
            dialogue.setShowDialogueFrame(false);  // Désactive le cadre d'interaction dans Quest
        }
        //End Region

        // Bascule d'affichage du cadre d'interaction avec la touche F
        if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), squarePosition, INTERACTION_DISTANCE) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            showInteractionFrame = !showInteractionFrame;  // Inverse l'état d'affichage du cadre d'interaction
            quest.setShowInteractionFrame(showInteractionFrame);  // Applique l'état à l'objet Quest
        }


        // Afficher/masquer le cadre de quête avec la touche G
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            showQuestPlayer = !showQuestPlayer;  // Inverse l'état d'affichage du cadre de quête
            questPlayer.setShowQuestPlayerFrame(showQuestPlayer);  // Applique l'état à l'objet QuestPlayer
        }

        // Cacher le cadre d'interaction avec la touche ESCAPE
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showInteractionFrame = false;  // Désactive le cadre d'interaction
            quest.setShowInteractionFrame(false);  // Désactive le cadre d'interaction dans Quest
        }

        // Si le cadre de quête doit être affiché, appelle le rendu de QuestPlayer
        if (showQuestPlayer) {
            questPlayer.render();  // Affiche le cadre de quête
        }

        // Si le cadre d'interaction doit être affiché, appelle le rendu de Quest
        if (showInteractionFrame) {
            quest.render(batch, player.getPlayerPosition());  // Affiche l'interaction avec le tableau de quête
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
    }

    // Méthode pour déterminer si le joueur peut se déplacer
    private boolean canMove() {
        return !showQuestPlayer && !showInteractionFrame && !showDialogueFrame;  // Le joueur ne peut se déplacer que si les cadres de quête et d'interaction sont fermés
    }

    // Méthode pour vérifier si le joueur est proche du tableau d'interaction (50 unités)
    private boolean isPlayerWithinInteractionDistance(Vector2 playerPosition, Vector2 squarePosition, float distanceThreshold) {
        return playerPosition.dst(squarePosition) < distanceThreshold;  // Vérifie si le joueur est à une distance inférieure au seuil
    }

    // Méthode pour vérifier si le joueur est proche du tableau de quête (500 unités)
    private boolean isPlayerNearSquare(Vector2 playerPosition, Vector2 squarePosition, float distanceThreshold) {
        return playerPosition.dst(squarePosition) < distanceThreshold;  // Vérifie si le joueur est à une distance inférieure au seuil
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
        if (soundManager != null) {
            soundManager.dispose();
        }
        debugRenderer.dispose();
        playerUI.dispose();
        F_Key_Texture.dispose();
        shapeRenderer.dispose(); // Libère ShapeRenderer
        font.dispose();
        inventaire.dispose();  // Libère les ressources utilisées dans inventaire
        quest.dispose();
        questBoardTexture.dispose();
        pathfinding.dispose();
        debugRenderer.dispose();
    }
}
