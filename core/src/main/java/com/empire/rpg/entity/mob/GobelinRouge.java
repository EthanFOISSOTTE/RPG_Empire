package com.empire.rpg.entity.mob;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.CollisionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Classe représentant un Gobelin Rouge avec des composants.
 */
public class GobelinRouge extends Mob {

    public GobelinRouge(Vector2 position, CollisionManager collisionManager) {
        super(
            "GobelinRouge",
            UUID.randomUUID(),
            initializeComponents(position),
            300f,
            new FollowPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de suivi du joueur
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
    }

    private static Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> initializeComponents(Vector2 position) {
        Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components = new HashMap<>();
        components.put(PositionComponent.class, new PositionComponent(position.x, position.y));
        components.put(HealthComponent.class, new HealthComponent(60, 60));
        components.put(CollisionComponent.class, new CollisionComponent(true, new Rectangle(position.x - 16, position.y - 16, 32, 32)));
        return components;
    }

    @Override
    protected void initializeTextures() {
        texture = new Texture("mobs/gobelin_rouge.png");
        TextureRegion[][] split = TextureRegion.split(texture, 64, 64);
        face = split[0][0];
        dos = split[0][1];
        droite = split[1][0];
        gauche = split[1][1];
        currentTexture = face;
    }
}
