package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import java.util.Map;
import java.util.UUID;

/**
 * La classe Player représente une entité joueur dans le jeu RPG.
 * Elle étend la classe Entity et inclut des fonctionnalités supplémentaires
 * spécifiques aux joueurs.
 */

public class Player extends Entity  {

    /**
     * Ajoute une entité.
     *
     * @return L'entité ajoutée.
     */

    @Override
    public Entity addEntity() {
        return null;
    }

    /**
     * Supprime une entité joueur par son nom.
     *
     * @param name Le nom de l'entité joueur à supprimer.
     * @return L'entité supprimée, actuellement renvoie null.
     */

    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    /**
     * Constructeur de l'entité joueur.
     *
     * @param name le nom du joueur.
     * @param components une map de composants associés au joueur.
     * @param id l'identifiant unique du joueur.
     */

    public Player(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

}
