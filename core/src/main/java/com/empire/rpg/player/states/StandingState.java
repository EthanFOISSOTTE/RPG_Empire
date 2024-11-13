package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public class StandingState extends State {

    public StandingState(Player player) {
        super(player);
    }

    @Override
    public void enter() {
        player.updateAnimationState(); // Met à jour l'animation pour le repos
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (player.isMoving()) {
            if (player.isRunning()) {
                player.changeState(new RunningState(player));
            } else {
                player.changeState(new WalkingState(player));
            }
        }
    }

    @Override
    public void exit() {
        // Nettoyage si nécessaire
    }
}
