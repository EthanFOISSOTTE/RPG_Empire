package com.empire.rpg.player.states;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;

public class RunningState extends State {

    // Constructeur qui initialise l'état de course avec une référence au joueur
    public RunningState(Player player) {
        super(player);
    }

    // Méthode appelée lorsqu'on entre dans l'état de course
    @Override
    public void enter() {
        player.updateAnimationState(); // Met à jour l'animation pour la course
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (!player.isMoving()) {
            // Si le joueur ne bouge plus, on le remet à l'état de repos
            player.changeState(new StandingState(player));
        } else if (!player.isRunning()) {
            // Si le joueur ralentit, on passe à l'état de marche
            player.changeState(new WalkingState(player));
        } else {
            // Sinon, le joueur continue à se déplacer et on met à jour l'animation
            player.move(deltaTime, collisionManager);
            player.updateAnimationState();
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de course
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
