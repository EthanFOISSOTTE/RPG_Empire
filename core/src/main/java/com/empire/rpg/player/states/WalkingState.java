package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public class WalkingState extends State {

    // Constructeur qui initialise l'état de marche avec une référence au joueur
    public WalkingState(Player player) {
        super(player);
    }

    // Méthode appelée lorsqu'on entre dans l'état de marche
    @Override
    public void enter() {
        player.updateAnimationState(); // Met à jour l'animation pour la marche
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (!player.isMoving()) {
            // Si le joueur s'arrête, passer à l'état de repos
            player.changeState(new StandingState(player));
        } else if (player.isRunning()) {
            // Si le joueur accélère, passer à l'état de course
            player.changeState(new RunningState(player));
        } else {
            // Sinon, continuer à bouger et mettre à jour l'animation
            player.move(deltaTime, collisionManager);
            player.updateAnimationState();
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de marche
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
