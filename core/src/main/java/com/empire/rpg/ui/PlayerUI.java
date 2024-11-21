package com.empire.rpg.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.entity.player.PlayerCharacter;

import com.empire.rpg.entity.player.equipment.Tool;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsable de l'affichage de l'interface utilisateur du joueur,
 * notamment la barre de santé et l'icône de l'arme équipée.
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

    // Variables de position et de taille pour l'icône de l'arme
    private final float weaponIconX;
    private final float weaponIconY;
    private final float weaponIconWidth;
    private final float weaponIconHeight;

    // Référence au joueur
    private final PlayerCharacter player;

    // Cache pour les textures des icônes d'armes
    private Map<String, Texture> toolIcons;

    /**
     * Constructeur pour initialiser l'UI du joueur.
     *
     * @param playerInstance Instance du joueur pour accéder à sa santé et à ses outils.
     */
    public PlayerUI(PlayerCharacter playerInstance) {
        this.player = playerInstance;

        // Initialisation des variables de position et de taille

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

        // Position et taille de l'icône de l'arme équipée
        this.weaponIconX = 45f;      // Position X en pixels depuis la gauche de l'écran
        this.weaponIconY = 638f;       // Position Y en pixels depuis le bas de l'écran
        this.weaponIconWidth = 16f * 3;   // Largeur en pixels
        this.weaponIconHeight = 16f * 3;  // Hauteur en pixels

        // Charger les textures
        try {
            this.playerStatusTexture = new Texture(Gdx.files.internal("UI/player_status.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de player_status.png: " + e.getMessage());
            this.playerStatusTexture = null;
        }

        try {
            this.playerHealthBarTexture = new Texture(Gdx.files.internal("UI/player_health_bar.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de player_health_bar.png: " + e.getMessage());
            this.playerHealthBarTexture = null;
        }

        // Initialiser le cache des icônes d'armes
        this.toolIcons = new HashMap<>();
    }

    /**
     * Méthode pour dessiner l'UI du joueur.
     *
     * @param batch SpriteBatch utilisé pour le rendu.
     */
    public void render(SpriteBatch batch) {
        // Obtenir la santé actuelle et maximale du joueur
        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
        if (health == null) {
            System.out.println("HealthComponent missing");
            return;
        }
        int currentHealth = health.getCurrentHealthPoints();
        int maxHealth = health.getMaxHealthPoints();

        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;

        // Calculer la largeur de la barre de santé en fonction de la santé actuelle
        float currentHealthBarWidth = healthBarFullWidth * healthPercentage;

        // Dessiner la barre de santé
        if (playerHealthBarTexture != null) {
            batch.draw(playerHealthBarTexture, healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);
        }

        // Dessiner le cadre de la barre de santé
        if (playerStatusTexture != null) {
            batch.draw(playerStatusTexture, statusX, statusY, statusWidth, statusHeight);
        }

        // Déterminer quelle icône d'arme afficher
        String toolIdToDisplay = null;

        // Récupérer les outils équipés
        Tool currentTool1 = player.getCurrentTool1();
        Tool currentTool2 = player.getCurrentTool2();

        if (currentTool1 != null) {
            toolIdToDisplay = currentTool1.getId();
        } else if (currentTool2 != null) {
            toolIdToDisplay = currentTool2.getId();
        }

        // Charger et récupérer la texture de l'icône de l'arme
        if (toolIdToDisplay != null && !toolIdToDisplay.isEmpty()) {
            Texture weaponTexture = toolIcons.get(toolIdToDisplay);
            if (weaponTexture == null) {
                // Charger la texture si elle n'est pas déjà chargée
                String weaponIconPath = "UI/icon/weapon/" + toolIdToDisplay + "-icon.png";
                try {
                    weaponTexture = new Texture(Gdx.files.internal(weaponIconPath));
                    toolIcons.put(toolIdToDisplay, weaponTexture);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de " + weaponIconPath + ": " + e.getMessage());
                    weaponTexture = null;
                }
            }

            // Dessiner l'icône de l'arme si la texture est chargée
            if (weaponTexture != null) {
                batch.draw(weaponTexture, weaponIconX, weaponIconY, weaponIconWidth, weaponIconHeight);
            }
        }
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
        // Disposer toutes les textures d'icônes d'armes
        for (Texture texture : toolIcons.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        toolIcons.clear();
    }
}
