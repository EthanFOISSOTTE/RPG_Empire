package com.empire.rpg.entity;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite EntityManager qui étend EntityFactory.
 * Gère les entités dans le jeu.
 */
public abstract class EntityManager extends EntityFactory {
    protected String name;
    protected Map<UUID, Entity> entities = new HashMap<>(); // Gestion des entités par UUID

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
     * Ajoute une nouvelle entité et l'enregistre dans le gestionnaire d'entités.
     *
     * @param entity L'entité à ajouter.
     * @return L'entité ajoutée.
     */
    public Entity addEntity(Entity entity) {
        entities.put(entity.getId(), entity); // Ajout de l'entité dans le map
        return entity;
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

    /**
     * Retourne une entité par son UUID.
     *
     * @param id L'UUID de l'entité.
     * @return L'entité correspondante ou null si elle n'existe pas.
     */
    public Entity getEntityById(UUID id) {
        return entities.get(id);
    }

    public abstract Entity addEntity(); // Méthode abstraite à implémenter
}
