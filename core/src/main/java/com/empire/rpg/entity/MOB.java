package com.empire.rpg.entity;

import com.empire.rpg.component.*;

import java.util.Map;
import java.util.UUID;

/**
 * La classe MOB représente une entité mobile dans le jeu RPG.
 * Elle étend la classe Entity et fournit des implémentations
 * pour ajouter et supprimer des entités.
 */

public class MOB extends Entity {

    /**
     * Crée une nouvelle instance de MOB.
     *
     * @param name       Le nom du MOB
     * @param components Les composants associés au MOB
     * @param id         L'identifiant unique du MOB
     */
    public MOB(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Ajouter une entité.
     *
     * @return l'entity ajoutée
     */
    @Override
    public Entity addEntity() {
        return null;
    }

    /**
     * Supprimer une entité par son nom.
     *
     * @param name le nom de l'entité à supprimer
     * @return l'entité supprimée
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }

}
