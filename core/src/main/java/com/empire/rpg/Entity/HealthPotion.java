package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.HealingComponent;
import com.empire.rpg.Component.PositionComponent;

import java.util.Map;
import java.util.UUID;

public class HealthPotion extends Entity {
    public HealthPotion(int x, int y, int healingPoints) {
        super("HealthPotion",
            Map.of(
                PositionComponent.class, new PositionComponent(x, y),
                CollisionComponent.class, new CollisionComponent(true),
                HealingComponent.class, new HealingComponent(healingPoints)
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

