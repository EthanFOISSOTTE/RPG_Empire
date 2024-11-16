package com.empire.rpg.entity.player.states;

import com.empire.rpg.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.equipment.Tool;
import com.empire.rpg.entity.player.attacks.Attack;
import com.empire.rpg.entity.player.animations.AnimationState;
import com.empire.rpg.entity.player.animations.CustomAnimation;
import com.badlogic.gdx.math.Polygon;

// État du joueur lorsqu'il effectue une attaque
public class AttackingState extends State {
    private Attack attack; // Attaque en cours
    private Tool tool; // Outil utilisé pour l'attaque
    private float elapsedTime; // Temps écoulé depuis le début de l'attaque
    private Polygon attackHitbox; // Hitbox de l'attaque

    // Constructeur de l'état d'attaque
    public AttackingState(PlayerCharacter PlayerCharacter, Attack attack, Tool tool) {
        super(PlayerCharacter);
        this.attack = attack;
        this.tool = tool;
        this.elapsedTime = 0f;
    }

    @Override
    public void enter() {
        // Déterminer la direction du joueur pour sélectionner l'animation appropriée
        String direction = PlayerCharacter.getFacingDirection();
        AnimationState animationState = attack.getAnimationStates().get(direction);

        if (animationState != null) {
            // Récupérer les clés de spritesheet pour les outils équipés
            String tool1SpritesheetKey = PlayerCharacter.getCurrentTool1() != null ? PlayerCharacter.getCurrentTool1().getSpritesheetKey() : null;
            String tool2SpritesheetKey = PlayerCharacter.getCurrentTool2() != null ? PlayerCharacter.getCurrentTool2().getSpritesheetKey() : null;

            // Créer l'animation d'attaque personnalisée
            CustomAnimation attackAnimation = PlayerCharacter.getAnimationController().createAttackAnimation(
                attack.getCategoryKey(), tool1SpritesheetKey, tool2SpritesheetKey, animationState);
            PlayerCharacter.getAnimationController().setCustomAnimation(attackAnimation);
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
            if (!PlayerCharacter.getAttackQueue().isEmpty()) {
                // Démarrer l'attaque suivante dans la file d'attente
                PlayerCharacter.startNextAttack();
            } else {
                // Retourner à l'état debout et réinitialiser le combo
                PlayerCharacter.changeState(new StandingState(PlayerCharacter));
                PlayerCharacter.resetCombo();
            }
        }
        // Ici, vous pouvez ajouter la gestion des collisions avec la hitbox de l'attaque
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
        PlayerCharacter.getAnimationController().setCustomAnimation(null);
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
        float playerCenterX = PlayerCharacter.getCollisionBounds().x + PlayerCharacter.getCollisionBounds().width / 2;
        float playerCenterY = PlayerCharacter.getCollisionBounds().y + PlayerCharacter.getCollisionBounds().height / 2;

        // Déplacer la hitbox au centre du joueur
        attackHitbox.setPosition(playerCenterX, playerCenterY);

        // Tourner et déplacer la hitbox en fonction de la direction
        String direction = PlayerCharacter.getFacingDirection();
        float rotation = 0f;
        float offsetX = 0f;
        float offsetY = 0f;

        switch (direction) {
            case "UP":
                rotation = 0f;
                offsetY = PlayerCharacter.getCollisionBounds().height / 2;
                break;
            case "DOWN":
                rotation = 180f;
                offsetY = -PlayerCharacter.getCollisionBounds().height / 2;
                break;
            case "LEFT":
                rotation = 90f;
                offsetX = -PlayerCharacter.getCollisionBounds().width / 2;
                break;
            case "RIGHT":
                rotation = -90f;
                offsetX = PlayerCharacter.getCollisionBounds().width / 2;
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
