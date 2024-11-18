package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;  // Importer Texture pour l'image
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.empire.rpg.component.*;
import com.empire.rpg.player.CollisionManager;
import com.empire.rpg.player.Player;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.quest.Quest;
import com.empire.rpg.quest.QuestPlayer;
import java.util.Map;
import java.util.UUID;
import com.empire.rpg.entity.PNJ;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;  // Objet pour dessiner des textures 2D
    private OrthographicCamera camera;  // Caméra orthographique pour la vue 2D
    private FitViewport viewport;  // Viewport qui ajuste la taille du monde en fonction de la taille de l'écran
    private Player player;  // Objet représentant le joueur
    private MapManager mapManager;  // Gestionnaire de carte
    private CollisionManager collisionManager;  // Gestionnaire de collisions pour le joueur
    private ShapeRenderer shapeRenderer;  // Outil pour dessiner des formes (utilisé pour le débogage)
    private BitmapFont font;  // Police de texte bitmap pour afficher du texte à l'écran

    private Texture questBoardTexture; // Déclaration de la texture pour l'icône du tableau de quête
    private static final float WORLD_WIDTH = 854f;  // Largeur du monde en pixels
    private static final float WORLD_HEIGHT = 480f;  // Hauteur du monde en pixels
    private static final Vector2 squarePosition = new Vector2(52 * 48, 45 * 49);  // Position du tableau de quête dans le monde
    private static final float INTERACTION_DISTANCE = 50;  // Distance d'interaction avec un objet
    private static final float DISPLAY_DISTANCE = 500;  // Distance de visibilité de l'icône du tableau de quête
    private static final float SQUARE_SIZE = 64;  // Taille de l'icône du tableau de quête
    private boolean showInteractionFrame = false;  // Booléen pour savoir si le cadre d'interaction est affiché
    private boolean showQuestPlayer = false;  // Booléen pour savoir si le cadre de quête est affiché
    private Quest quest;  // Objet Quest qui gère les quêtes dans le jeu
    private QuestPlayer questPlayer;  // Objet qui gère l'interface de gestion des quêtes pour le joueur
    private PNJ pnj_radagast;

    @Override
    public void create() {
        // Initialisation des objets essentiels pour le rendu et l'affichage
        batch = new SpriteBatch();  // Initialisation du SpriteBatch pour dessiner les sprites
        camera = new OrthographicCamera();  // Initialisation de la caméra orthographique
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);  // Initialisation du viewport
        shapeRenderer = new ShapeRenderer();  // Initialisation du renderer pour les formes
        font = new BitmapFont();  // Initialisation de la police de texte
        // Initialisation des gestionnaires de carte et des autres objets du jeu
        mapManager = new MapManager("rpg-map.tmx", camera);  // Chargement de la carte avec le gestionnaire de carte
        collisionManager = new CollisionManager(mapManager.getTiledMap());  // Initialisation du gestionnaire de collisions
        player = new Player(collisionManager);  // Initialisation du joueur avec le gestionnaire de collisions


        Map<Class<? extends Component>, Component> ComponentPNJ = Map.of(
            PositionComponent.class, new PositionComponent(49 * 48 + 24, 44 * 48 + 24),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            CollisionComponent.class, new CollisionComponent(true),
            TextureComponent.class, new TextureComponent(new Texture("PNJ/radagast.png"), 48, 48, 0, 0, 2.0f)
        );

        pnj_radagast = new PNJ("Radagast", ComponentPNJ, UUID.randomUUID());


        // Initialisation des objets liés aux quêtes
        quest = new Quest(camera, batch);  // Création de l'objet Quest pour gérer les quêtes
        questPlayer = new QuestPlayer(camera, batch, quest.getQuestList());  // Création de l'objet QuestPlayer pour gérer l'affichage des quêtes

        // Chargement de la texture de l'icône du tableau de quête
        questBoardTexture = new Texture(Gdx.files.internal("exclamation.png"));  // Chemin vers l'image du tableau de quêtes
    }

    @Override
    public void render() {
        // Efface l'écran et applique une couleur de fond noire
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si le joueur peut se déplacer, met à jour sa position
        if (canMove()) {
            player.update(Gdx.graphics.getDeltaTime()); // Mise à jour de la position du joueur
        }

        // Mise à jour de la position de la caméra pour suivre le joueur
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();

        // Rendu des couches inférieures de la carte
        mapManager.renderLowerLayers(camera);

        // Rendu du joueur avec le SpriteBatch
        batch.setProjectionMatrix(camera.combined);  // Définir la matrice de projection de la caméra
        batch.begin();  // Démarre l'affichage des éléments 2D
        pnj_radagast.render(batch);
        player.render(batch);  // Dessine le joueur
        batch.end();  // Fin de l'affichage des éléments 2D

        // Rendu des couches supérieures de la carte
        mapManager.renderUpperLayers(camera);
        // Affichage de l'icône de quête si le joueur est proche du tableau
        if (isPlayerNearSquare(player.getPosition(), squarePosition, DISPLAY_DISTANCE)) {
            batch.begin();
            batch.draw(questBoardTexture, squarePosition.x + 19, squarePosition.y + SQUARE_SIZE);  // Affiche l'icône du tableau de quête
            batch.end();
        }

        // Bascule d'affichage du cadre d'interaction avec la touche F
        if (isPlayerWithinInteractionDistance(player.getPosition(), squarePosition, INTERACTION_DISTANCE)
            && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
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
            quest.render(batch, player.getPosition());  // Affiche l'interaction avec le tableau de quête
        }
    }

    // Méthode pour déterminer si le joueur peut se déplacer
    private boolean canMove() {
        return !showQuestPlayer && !showInteractionFrame;  // Le joueur ne peut se déplacer que si les cadres de quête et d'interaction sont fermés
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
        viewport.update(width, height);  // Met à jour la taille du viewport lors du redimensionnement de la fenêtre
    }

    @Override
    public void dispose() {
        // Libère les ressources utilisées par les objets
        batch.dispose();
        mapManager.dispose();
        player.dispose();
        shapeRenderer.dispose();
        font.dispose();
        quest.dispose();
        questBoardTexture.dispose();  // Libère la texture de l'icône de quête
    }
}
