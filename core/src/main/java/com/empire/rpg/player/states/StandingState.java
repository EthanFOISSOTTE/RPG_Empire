package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public class StandingState extends State {

    // Constructeur qui initialise l'état de repos avec une référence au joueur
    public StandingState(Player player) {
        super(player);
    }

    // Méthode appelée lorsqu'on entre dans l'état de repos
    @Override
    public void enter() {
        player.updateAnimationState(); // Met à jour l'animation pour le repos
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (player.isMoving()) {
            // Si le joueur commence à bouger
            if (player.isRunning()) {
                // Passer à l'état de course
                player.changeState(new RunningState(player));
            } else {
                // Passer à l'état de marche
                player.changeState(new WalkingState(player));
            }
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de repos
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
