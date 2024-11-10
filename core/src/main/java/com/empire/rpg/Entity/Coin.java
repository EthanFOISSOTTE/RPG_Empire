package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.ValueComponent;

import java.util.Map;
import java.util.UUID;

/**
 * Represente une pièce d'or dans le jeu.
 * Une pièce d'or a une valeur, une position, et peut entrer en collision avec le player.
 */

public class Coin extends Entity {

    /**
     * Constructeur de la classe Coin.
     *
     * @param x     Coordonnée x de la pièce d'or
     * @param y     Coordonnée y de la pièce d'or
     * @param value Valeur de la pièce d'or
     */
    public Coin(int x, int y, int value) {
        super("Coin",
            Map.of(
                PositionComponent.class, new PositionComponent(x, y),
                CollisionComponent.class, new CollisionComponent(true),
                ValueComponent.class, new ValueComponent(value)
            ),
            UUID.randomUUID());
    }

    /**
     * Ajoute une entité dans le jeu.
     *
     * @return l'entité ajoutée
     */
    @Override
    public Entity addEntity() {
        return null;
    }

    /**
     * Supprime une entité par son nom.
     *
     * @param name le nom de l'entité à supprimer
     * @return l'entité supprimée ou null si non trouvée
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }
}
