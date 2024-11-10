package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.ValueComponent;

import java.util.Map;
import java.util.UUID;

public class Coin extends Entity {
    public Coin(int x, int y, int value) {
        super("Coin",
            Map.of(
                PositionComponent.class, new PositionComponent(x, y),
                CollisionComponent.class, new CollisionComponent(true),
                ValueComponent.class, new ValueComponent(value)
            ),
            UUID.randomUUID());
    }

    @Override
    public Entity addEntity() {
        return null;
    }

    @Override
    public Entity removeEntity(String name) {
        return null;
    }
}

