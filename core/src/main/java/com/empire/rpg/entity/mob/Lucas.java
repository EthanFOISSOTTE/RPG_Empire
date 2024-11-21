package com.empire.rpg.entity.mob;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Camera;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.entity.mob.attacks.MobAttack;
import com.empire.rpg.entity.player.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe représentant un Gobelin avec des animations et une attaque.
 */
public class Lucas extends Mob {

    // Animations pour idle
    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;
    // Animations pour déplacement
    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    // Animations pour attaque
    private Animation<TextureRegion> attackUp, attackDown, attackLeft, attackRight;
    // Temps écoulé pour les animations
    private float stateTime;
    // Direction actuelle du Gobelin
    private String currentDirection = "DOWN"; // Direction par défaut
    // Indicateur de mouvement
    private boolean isMoving;
    // Indicateur d'attaque (hérité de la classe Mob)
    // private boolean isAttacking; // Déjà déclaré dans Mob

    // Textures chargées
    private Texture idleTexture, runTexture, attackTexture;

    public Lucas(Vector2 position, CollisionManager collisionManager) {
        super(
            "Lucas",
            UUID.randomUUID(),
            initializeComponents(position),
            400f,
            new FollowPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de suivi du joueur
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
        this.scale = 2.0f;      // Zoom factor (2x)
        this.offsetX = -30f;    // Décalage X
        this.offsetY = -30f;    // Décalage Y

        // Définir l'attaque du Goblin
        MobAttack basicAttack = new MobAttack(
            "goblin_basic_attack",
            95f, // Dégâts infligés
            1f,  // Cooldown en secondes
            0.5f, // Durée de l'attaque
            80f,  // Portée de l'attaque
            80f,  // Largeur de la hitbox
            80f   // Hauteur de la hitbox
        );
        availableAttacks.add(basicAttack);
    }

    private static Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> initializeComponents(Vector2 position) {
        Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components = new HashMap<>();
        components.put(PositionComponent.class, new PositionComponent(position.x, position.y));
        components.put(HealthComponent.class, new HealthComponent(200, 200));
        components.put(CollisionComponent.class, new CollisionComponent(true, new Rectangle(position.x, position.y, 32, 48)));
        return components;
    }

    @Override
    protected void initializeTextures() {
        // Charger et diviser les textures d'idle
        idleTexture = new Texture("mobs/lucas/demon1.png");
        TextureRegion[][] idleSplit = TextureRegion.split(idleTexture, 64, 64);

        idleUp = new Animation<>(0.1f, idleSplit[1][0]);
        idleDown = new Animation<>(0.1f, idleSplit[0][0]);
        idleLeft = new Animation<>(0.1f, idleSplit[2][0]);
        idleRight = new Animation<>(0.1f, idleSplit[3][0]);

        // Charger et diviser les textures de déplacement
        runTexture = new Texture("mobs/lucas/demon1.png");
        TextureRegion[][] runSplit = TextureRegion.split(runTexture, 64, 64);

        runUp = new Animation<>(0.05f,
            runSplit[5][0], runSplit[5][1], runSplit[5][6],
            runSplit[5][3], runSplit[5][4], runSplit[5][7]);
        runDown = new Animation<>(0.05f,
            runSplit[4][0], runSplit[4][1], runSplit[4][6],
            runSplit[4][3], runSplit[4][4], runSplit[4][7]);
        runLeft = new Animation<>(0.05f,
            runSplit[7][0], runSplit[7][1], runSplit[7][6],
            runSplit[7][3], runSplit[7][4], runSplit[7][7]);
        runRight = new Animation<>(0.05f,
            runSplit[6][0], runSplit[6][1], runSplit[6][6],
            runSplit[6][3], runSplit[6][4], runSplit[6][7]);

        // Charger et diviser les textures d'attaque
        attackTexture = new Texture("mobs/lucas/demon1.png");
        TextureRegion[][] attackSplit = TextureRegion.split(attackTexture, 64, 64);

        // Créer les animations d'attaque pour chaque direction
        attackUp = new Animation<>(0.05f, attackSplit[5][6]);
        attackDown = new Animation<>(0.05f, attackSplit[4][6]);
        attackLeft = new Animation<>(0.05f, attackSplit[7][6]);
        attackRight = new Animation<>(0.05f, attackSplit[6][6]);

        // Initialiser la texture courante
        currentTexture = idleDown.getKeyFrame(0, true);
    }

    @Override
    public void update(float deltaTime, PlayerCharacter player, Camera camera) {
        super.update(deltaTime, player, camera); // Appel de la mise à jour de base

        // Mettre à jour le temps d'état pour les animations
        stateTime += deltaTime;

        // Déterminer si le mob est en mouvement
        isMoving = !currentPath.isEmpty() && targetPosition != null;

        // L'indicateur d'attaque est déjà mis à jour dans la classe Mob
        // isAttacking = this.isAttacking;

        // Déterminer la direction basée sur le mouvement
        if (isMoving && targetPosition != null) {
            float deltaX = targetPosition.x - getPositionVector().x;
            float deltaY = targetPosition.y - getPositionVector().y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                currentDirection = (deltaX > 0) ? "RIGHT" : "LEFT";
            } else {
                currentDirection = (deltaY > 0) ? "UP" : "DOWN";
            }
        }

        // Sélectionner l'animation appropriée
        Animation<TextureRegion> currentAnimation;
        if (isAttacking) {
            switch (currentDirection) {
                case "UP":
                    currentAnimation = attackUp;
                    break;
                case "DOWN":
                    currentAnimation = attackDown;
                    break;
                case "LEFT":
                    currentAnimation = attackLeft;
                    break;
                case "RIGHT":
                    currentAnimation = attackRight;
                    break;
                default:
                    currentAnimation = attackDown;
            }
        } else if (isMoving) {
            switch (currentDirection) {
                case "UP":
                    currentAnimation = runUp;
                    break;
                case "DOWN":
                    currentAnimation = runDown;
                    break;
                case "LEFT":
                    currentAnimation = runLeft;
                    break;
                case "RIGHT":
                    currentAnimation = runRight;
                    break;
                default:
                    currentAnimation = idleDown;
            }
        } else {
            switch (currentDirection) {
                case "UP":
                    currentAnimation = idleUp;
                    break;
                case "DOWN":
                    currentAnimation = idleDown;
                    break;
                case "LEFT":
                    currentAnimation = idleLeft;
                    break;
                case "RIGHT":
                    currentAnimation = idleRight;
                    break;
                default:
                    currentAnimation = idleDown;
            }
        }

        // Obtenir la frame actuelle de l'animation
        currentTexture = currentAnimation.getKeyFrame(stateTime, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (idleTexture != null) {
            idleTexture.dispose();
        }
        if (runTexture != null) {
            runTexture.dispose();
        }
        if (attackTexture != null) {
            attackTexture.dispose();
        }
    }

    @Override
    protected Map<String, Object> getDeathInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("Item-Name", "Peau de Lucas");
        info.put("Item-Description", "Peau de Lucas, un matériau aussi rare que lugubre.");
        info.put("Item-Quantity", 1);
        info.put("Item-Type", "divers");
        return info;
    }
}
