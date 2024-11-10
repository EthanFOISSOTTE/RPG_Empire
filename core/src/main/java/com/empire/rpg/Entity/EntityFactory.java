package com.empire.rpg.Entity;

import java.util.UUID;

public abstract class EntityFactory {

    // Méthode abstraite pour créer une entité sans composants spécifiques
    public abstract void createEntity(UUID id);

}
