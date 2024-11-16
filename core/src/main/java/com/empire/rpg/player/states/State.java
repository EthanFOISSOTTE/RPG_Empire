package com.empire.rpg.player.states;

import com.empire.rpg.CollisionManager;
import com.empire.rpg.player.Player;

// Classe abstraite de base pour les différents états du joueur
public abstract class State {
    protected Player player; // Référence au joueur associé à cet état

    // Constructeur qui initialise l'état avec une référence au joueur
    public State(Player player) {
        this.player = player;
    }

    // Méthode appelée lorsqu'on entre dans un état (doit être implémentée par les sous-classes)
    public abstract void enter();

    // Méthode appelée à chaque mise à jour de la boucle de jeu (doit être implémentée par les sous-classes)
    public abstract void update(float deltaTime, CollisionManager collisionManager);

    // Méthode appelée lorsqu'on quitte un état (doit être implémentée par les sous-classes)
    public abstract void exit();
}
