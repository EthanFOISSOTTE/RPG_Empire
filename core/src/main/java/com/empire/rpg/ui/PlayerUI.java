package com.empire.rpg.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Classe responsable de l'affichage de l'interface utilisateur du joueur,
 * notamment la barre de santé.
 */
public class PlayerUI {
    // Textures pour les éléments UI
    private Texture playerStatusTexture;
    private Texture playerHealthBarTexture;

    // Variables de position et de taille pour player_status.png
    private final float statusX;
    private final float statusY;
    private final float statusWidth;
    private final float statusHeight;

    // Variables de position et de taille pour player_health_bar.png
    private final float healthBarX;
    private final float healthBarY;
    private final float healthBarFullWidth;
    private final float healthBarHeight;

    // Référence au joueur
    private final PlayerCharacter player;

    // Caméra dédiée pour l'UI
    private OrthographicCamera uiCamera;

    /**
     * Constructeur pour initialiser l'UI du joueur.
     *
     * @param playerInstance Instance du joueur pour accéder à sa santé.
     */
    public PlayerUI(PlayerCharacter playerInstance) {
        this.player = playerInstance;

        // Initialisation des variables de position et de taille (hardcodées)
        // Vous pouvez ajuster ces valeurs selon vos besoins

        // Position et taille du cadre de la barre de santé (player_status.png)
        this.statusX = 0f;          // Position X en pixels depuis la gauche de l'écran
        this.statusY = 580f;         // Position Y en pixels depuis le bas de l'écran
        this.statusWidth = 83f * 4;     // Largeur en pixels
        this.statusHeight = 35f * 4;     // Hauteur en pixels

        // Position et taille de la barre de santé (player_health_bar.png)
        this.healthBarX = 132f;       // Position X en pixels depuis la gauche de l'écran
        this.healthBarY = 652f;       // Position Y en pixels depuis le bas de l'écran
        this.healthBarFullWidth = 45f * 4; // Largeur maximale en pixels
        this.healthBarHeight = 5f * 4;  // Hauteur en pixels

        // Charger les textures
        this.playerStatusTexture = new Texture(Gdx.files.internal("UI/player_status.png"));
        this.playerHealthBarTexture = new Texture(Gdx.files.internal("UI/player_health_bar.png"));

        // Initialiser la caméra de l'UI
        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Méthode pour dessiner l'UI du joueur.
     *
     * @param batch SpriteBatch utilisé pour le rendu.
     */
    public void render(SpriteBatch batch) {
        // Mettre à jour la caméra de l'UI
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);

        // Commencer le SpriteBatch pour l'UI
        batch.begin();

        // Dessiner le cadre de la barre de santé
        batch.draw(playerStatusTexture, statusX, statusY, statusWidth, statusHeight);

        // Obtenir la santé actuelle et maximale du joueur
        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
        if (health == null) {
            System.out.println("HealthComponent missing");
            batch.end();
            return;
        }
        int currentHealth = health.getCurrentHealthPoints();
        int maxHealth = health.getMaxHealthPoints();

        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;

        // Calculer la largeur de la barre de santé en fonction de la santé actuelle
        float currentHealthBarWidth = healthBarFullWidth * healthPercentage;

        // Dessiner la barre de santé
        // La barre disparaît de droite à gauche en ajustant la largeur
        batch.draw(playerHealthBarTexture, healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);

        // Terminer le SpriteBatch pour l'UI
        batch.end();
    }

    /**
     * Méthode pour libérer les ressources utilisées par l'UI.
     */
    public void dispose() {
        if (playerStatusTexture != null) {
            playerStatusTexture.dispose();
        }
        if (playerHealthBarTexture != null) {
            playerHealthBarTexture.dispose();
        }
    }
}
