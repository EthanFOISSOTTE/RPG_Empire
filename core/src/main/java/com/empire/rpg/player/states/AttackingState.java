package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;
import com.empire.rpg.player.animations.AnimationState;

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
        // Déterminer l'animation en fonction de l'outil utilisé (tool)
        String direction = player.getFacingDirection();
        AnimationState animationState;

        if (tool == player.getCurrentTool1()) {
            // Utiliser l'animation d'attaque pour tool1 (par exemple, ONE_SLASH1)
            animationState = attack.getAnimationStates().get(direction);
        } else if (tool == player.getCurrentTool2()) {
            // Utiliser l'animation d'attaque pour tool2 (par exemple, DEFEND1)
            animationState = attack.getAnimationStates().get(direction);
        } else {
            animationState = AnimationState.STANDING_DOWN; // État par défaut si l'outil n'est pas défini
        }

        if (animationState != null) {
            player.getAnimationController().setAnimationState(animationState);
        }
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        elapsedTime += deltaTime;
        if (elapsedTime >= attack.getDuration()) {
            // L'attaque est terminée, revenir à l'état de repos
            player.changeState(new StandingState(player));
        }

        // Gestion des collisions ou autres mises à jour spécifiques à l'attaque
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
    }
}
