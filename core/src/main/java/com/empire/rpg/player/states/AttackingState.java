package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;
import com.empire.rpg.player.animations.AnimationState;
import com.empire.rpg.player.animations.CustomAnimation;

public class AttackingState extends State {
    private Attack attack;
    private Tool tool;
    private float elapsedTime;

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
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        elapsedTime += deltaTime;
        if (elapsedTime >= attack.getDuration()) {
            // L'attaque est terminée
            player.changeState(new StandingState(player));
            // Si le combo est terminé, le réinitialiser
            if (player.getComboStep() >= player.getCurrentTool1().getAvailableAttacks().size()) {
                player.resetCombo();
            }
        }
        // Gestion des collisions ou autres mises à jour spécifiques à l'attaque
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
        player.getAnimationController().setCustomAnimation(null);
    }
}
