package com.empire.rpg.Entity;

import java.util.UUID;

public abstract class EntityManager extends EntityFactory {
    protected String name;

    // Constructeur pour injecter une instance de EntityFactory
    public EntityManager(String name) {
        this.name = name;
    }


    public Entity addEntity() {
        createEntity(UUID.randomUUID()); // Création de l'entité via EntityFactory
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
