package com.empire.rpg.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.empire.rpg.component.Component;
import com.empire.rpg.entity.Entity;

import java.util.Map;
import java.util.UUID;

/**
 * Classe abstraite représentant un joueur dans le jeu.
 */
public abstract class Player extends Entity {
    /**
     * Constructeur du joueur.
     *
     * @param name       Le nom du joueur.
     * @param components La map des composants du joueur.
     * @param id         L'identifiant unique du joueur.
     */
    public Player(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Méthode abstraite pour ajouter une entité.
     *
     * @return L'entité ajoutée.
     */
    @Override
    public abstract Entity addEntity();

    /**
     * Méthode abstraite pour supprimer une entité par son nom.
     *
     * @param name Le nom de l'entité à supprimer.
     * @return L'entité supprimée.
     */
    @Override
    public abstract Entity removeEntity(String name);

    /**
     * Méthode abstraite pour rendre le joueur.
     *
     * @param batch Le batch pour le rendu.
     */
    public abstract void render(Batch batch);
}
