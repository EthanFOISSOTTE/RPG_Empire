package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public class WalkingState extends State {

    public WalkingState(Player player) {
        super(player);
    }

    @Override
    public void enter() {
        player.updateAnimationState(); // Met à jour l'animation pour la marche
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (!player.isMoving()) {
            player.changeState(new StandingState(player));
        } else if (player.isRunning()) {
            player.changeState(new RunningState(player));
        } else {
            player.move(deltaTime, collisionManager);
            player.updateAnimationState(); // Met à jour l'animation en fonction de la direction
        }
    }

    @Override
    public void exit() {
        // Nettoyage si nécessaire
    }
}
