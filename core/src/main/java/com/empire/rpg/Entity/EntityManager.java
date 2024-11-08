package com.empire.rpg.Entity;

public abstract class EntityManager {
    protected EntityFactory entityFactory; // Association avec EntityFactory

    // Constructeur pour injecter une instance de EntityFactory
    public EntityManager(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }


    public void addEntity() {
        Entity entity = entityFactory.createEntity(); // Création de l'entité via EntityFactory
        // Code pour ajouter l'entité au système
    }

    public abstract void manageEntities();
}
