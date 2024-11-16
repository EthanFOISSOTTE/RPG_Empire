package com.empire.rpg.player.states;

import com.empire.rpg.CollisionManager;
import com.empire.rpg.player.Player;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;
import com.empire.rpg.player.animations.AnimationState;
import com.empire.rpg.player.animations.CustomAnimation;
import com.badlogic.gdx.math.Polygon;

// État du joueur lorsqu'il effectue une attaque
public class AttackingState extends State {
    private Attack attack; // Attaque en cours
    private Tool tool; // Outil utilisé pour l'attaque
    private float elapsedTime; // Temps écoulé depuis le début de l'attaque
    private Polygon attackHitbox; // Hitbox de l'attaque

    // Constructeur de l'état d'attaque
    public AttackingState(Player player, Attack attack, Tool tool) {
        super(player);
        this.attack = attack;
        this.tool = tool;
        this.elapsedTime = 0f;
    }

    @Override
    public void enter() {
        // Déterminer la direction du joueur pour sélectionner l'animation appropriée
        String direction = player.getFacingDirection();
        AnimationState animationState = attack.getAnimationStates().get(direction);

        if (animationState != null) {
            // Récupérer les clés de spritesheet pour les outils équipés
            String tool1SpritesheetKey = player.getCurrentTool1() != null ? player.getCurrentTool1().getSpritesheetKey() : null;
            String tool2SpritesheetKey = player.getCurrentTool2() != null ? player.getCurrentTool2().getSpritesheetKey() : null;

            // Créer l'animation d'attaque personnalisée
            CustomAnimation attackAnimation = player.getAnimationController().createAttackAnimation(
                attack.getCategoryKey(), tool1SpritesheetKey, tool2SpritesheetKey, animationState);
            player.getAnimationController().setCustomAnimation(attackAnimation);
        }

        // Jouer le son de l'attaque
        attack.playSound();

        // Calculer la hitbox de l'attaque
        calculateAttackHitbox();
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        elapsedTime += deltaTime;
        if (elapsedTime >= attack.getDuration()) {
            // Si l'attaque est terminée
            if (!player.getAttackQueue().isEmpty()) {
                // Démarrer l'attaque suivante dans la file d'attente
                player.startNextAttack();
            } else {
                // Retourner à l'état debout et réinitialiser le combo
                player.changeState(new StandingState(player));
                player.resetCombo();
            }
        }
        // Ici, vous pouvez ajouter la gestion des collisions avec la hitbox de l'attaque
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
        player.getAnimationController().setCustomAnimation(null);
        attackHitbox = null; // Réinitialiser la hitbox
    }

    // Calculer la hitbox de l'attaque en fonction de la direction du joueur
    private void calculateAttackHitbox() {
        float hitboxWidth = attack.getHitboxWidth();
        float hitboxHeight = attack.getHitboxHeight();

        // Points de la hitbox avant transformation
        float[] vertices = new float[] {
            -hitboxWidth / 2, 0,                  // Point inférieur gauche
            hitboxWidth / 2, 0,                   // Point inférieur droit
            hitboxWidth / 2, hitboxHeight,        // Point supérieur droit
            -hitboxWidth / 2, hitboxHeight        // Point supérieur gauche
        };

        attackHitbox = new Polygon(vertices);

        // Positionner le polygone devant le joueur
        float playerCenterX = player.getCollisionBounds().x + player.getCollisionBounds().width / 2;
        float playerCenterY = player.getCollisionBounds().y + player.getCollisionBounds().height / 2;

        // Déplacer la hitbox au centre du joueur
        attackHitbox.setPosition(playerCenterX, playerCenterY);

        // Tourner et déplacer la hitbox en fonction de la direction
        String direction = player.getFacingDirection();
        float rotation = 0f;
        float offsetX = 0f;
        float offsetY = 0f;

        switch (direction) {
            case "UP":
                rotation = 0f;
                offsetY = player.getCollisionBounds().height / 2;
                break;
            case "DOWN":
                rotation = 180f;
                offsetY = -player.getCollisionBounds().height / 2;
                break;
            case "LEFT":
                rotation = 90f;
                offsetX = -player.getCollisionBounds().width / 2;
                break;
            case "RIGHT":
                rotation = -90f;
                offsetX = player.getCollisionBounds().width / 2;
                break;
        }

        // Appliquer la rotation
        attackHitbox.setRotation(rotation);

        // Déplacer la hitbox devant le joueur
        attackHitbox.translate(offsetX, offsetY);
    }

    // Getter pour la hitbox de l'attaque
    public Polygon getAttackHitbox() {
        return attackHitbox;
    }

    // Getter pour l'attaque en cours
    public Attack getAttack() {
        return attack;
    }
}
