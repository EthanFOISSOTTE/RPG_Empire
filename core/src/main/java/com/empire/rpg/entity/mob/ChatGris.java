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
 * Classe représentant un Chat Gris avec des composants.
 */
public class ChatGris extends Mob {

    public ChatGris(Vector2 position, CollisionManager collisionManager) {
        super(
            "ChatGris",
            UUID.randomUUID(),
            initializeComponents(position),
            200f,
            new RandomPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de déplacement aléatoire
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
    }

    private static Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> initializeComponents(Vector2 position) {
        Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components = new HashMap<>();
        components.put(PositionComponent.class, new PositionComponent(position.x, position.y));
        components.put(HealthComponent.class, new HealthComponent(30, 30));
        // Initialiser le CollisionComponent avec des positions absolues centrées
        components.put(CollisionComponent.class, new CollisionComponent(true, new Rectangle(position.x - 16, position.y - 16, 32, 32)));
        return components;
    }

    @Override
    protected void initializeTextures() {
        texture = new Texture("mobs/chat_gris.png");
        TextureRegion[][] split = TextureRegion.split(texture, 64, 64);
        face = split[0][0];
        dos = split[0][1];
        droite = split[1][0];
        gauche = split[1][1];
        currentTexture = face;
    }
}
