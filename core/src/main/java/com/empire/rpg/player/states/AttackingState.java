package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;
import com.empire.rpg.player.animations.AnimationState;
import com.empire.rpg.player.animations.CustomAnimation;
import com.badlogic.gdx.math.Polygon;

public class AttackingState extends State {
    private Attack attack;
    private Tool tool;
    private float elapsedTime;
    private Polygon attackHitbox;

    public AttackingState(Player player, Attack attack, Tool tool) {
        super(player);
        this.attack = attack;
        this.tool = tool;
        this.elapsedTime = 0f;
    }

    @Override
    public void enter() {
        String direction = player.getFacingDirection();
        AnimationState animationState = attack.getAnimationStates().get(direction);

        if (animationState != null) {
            String tool1SpritesheetKey = player.getCurrentTool1() != null ? player.getCurrentTool1().getSpritesheetKey() : null;
            String tool2SpritesheetKey = player.getCurrentTool2() != null ? player.getCurrentTool2().getSpritesheetKey() : null;

            CustomAnimation attackAnimation = player.getAnimationController().createAttackAnimation(
                attack.getCategoryKey(), tool1SpritesheetKey, tool2SpritesheetKey, animationState);
            player.getAnimationController().setCustomAnimation(attackAnimation);
        }

        // Jouer le son de l'attaque
        attack.playSound();

        // Calculer la position de la zone d'effet
        calculateAttackHitbox();
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        elapsedTime += deltaTime;
        if (elapsedTime >= attack.getDuration()) {
            // L'attaque est terminée
            if (!player.getAttackQueue().isEmpty()) {
                player.startNextAttack();
            } else {
                player.changeState(new StandingState(player));
                player.resetCombo();
            }
        }
        // Gestion des collisions ou autres mises à jour spécifiques à l'attaque
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
        player.getAnimationController().setCustomAnimation(null);
        attackHitbox = null; // Réinitialiser la hitbox
    }

    private void calculateAttackHitbox() {
        float hitboxWidth = attack.getHitboxWidth();
        float hitboxHeight = attack.getHitboxHeight();

        // Points de la hitbox avant transformation
        float[] vertices = new float[] {
            -hitboxWidth / 2, 0,                  // Point inférieur gauche
            hitboxWidth / 2, 0,                  // Point inférieur droit
            hitboxWidth / 2, hitboxHeight,       // Point supérieur droit
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

    public Polygon getAttackHitbox() {
        return attackHitbox;
    }

    // Ajouter le getter pour l'attaque
    public Attack getAttack() {
        return attack;
    }
}
