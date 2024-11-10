package com.empire.rpg.Entity;

import java.util.UUID;

/**
 * Classe abstraite EntityManager qui étend EntityFactory.
 * Gère les entités dans le jeu.
 */

public abstract class EntityManager extends EntityFactory {
    protected String name;

    /**
     * Constructeur pour injecter une instance de EntityFactory.
     *
     * @param name Le nom de l'EntityManager.
     */
    public EntityManager(String name) {
        this.name = name;
    }

    /**
     * Méthode abstraite pour créer une entité.
     *
     * @param id L'identifiant unique de l'entité.
     */
    public abstract void createEntity(UUID id);

    /**
     * Ajoute une nouvelle entité en générant un UUID aléatoire.
     *
     * @return L'entité ajoutée.
     */
    public Entity addEntity() {
        createEntity(UUID.randomUUID()); // Création de l'entité via EntityFactory
        return null;
    }

    /**
     * Obtient le nom de l'EntityManager.
     *
     * @return Le nom de l'EntityManager.
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom de l'EntityManager.
     *
     * @param name Le nouveau nom de l'EntityManager.
     */
    public void setName(String name) {
        this.name = name;
    }
}
