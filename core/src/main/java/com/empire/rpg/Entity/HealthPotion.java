package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.HealingComponent;
import com.empire.rpg.Component.PositionComponent;

import java.util.Map;
import java.util.UUID;

/**
 * Represente une potion de vie dans le jeu.
 * Cette entité a un composant de position, un composant de collision et un composant de soin.
 */

public class HealthPotion extends Entity {

    /**
     * Constructeur de potion de vie avec une position, collision et le supplément de vie.
     *
     * @param x coordonnée x de la potion de vie
     * @param y coordonnée y de la potion de vie
     * @param healingPoints la recharge de points de vie de la potion
     */
    public HealthPotion(int x, int y, int healingPoints) {
        super("HealthPotion",
            Map.of(
                PositionComponent.class, new PositionComponent(x, y),
                CollisionComponent.class, new CollisionComponent(true),
                HealingComponent.class, new HealingComponent(healingPoints)
            ),
            UUID.randomUUID());
    }

    /**
     * Ajoute une entité potion dans le jeu.
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
