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
 * Classe représentant un Lapin Blanc avec des composants.
 */
public class LapinBlanc extends Mob {

    public LapinBlanc(Vector2 position, CollisionManager collisionManager) {
        super(
            "LapinBlanc",
            UUID.randomUUID(),
            initializeComponents(position),
            250f,
            new FleePathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de fuite du joueur
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
    }

    private static Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> initializeComponents(Vector2 position) {
        Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components = new HashMap<>();
        components.put(PositionComponent.class, new PositionComponent(position.x, position.y));
        components.put(HealthComponent.class, new HealthComponent(40, 40));
        components.put(CollisionComponent.class, new CollisionComponent(true, new Rectangle(position.x - 16, position.y - 16, 32, 32)));
        return components;
    }

    @Override
    protected void initializeTextures() {
        texture = new Texture("mobs/lapin_blanc.png");
        TextureRegion[][] split = TextureRegion.split(texture, 64, 64);
        face = split[0][0];
        dos = split[0][1];
        droite = split[1][0];
        gauche = split[1][1];
        currentTexture = face;
    }

    @Override
    protected Map<String, Object> getDeathInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("Item-Name", "Peau de Lapin Blanc");
        info.put("Item-Description", "Peau de lapin blanc, un ingrédient bon marché.");
        info.put("Item-Quantity", 1);
        info.put("Item-Type", "divers");
        return info;
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
