package com.empire.rpg.entity.mob;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.entity.mob.MobFactory;
import com.empire.rpg.CollisionManager;

/**
 * Classe représentant un Gobelin Vert avec une stratégie de suivi du joueur.
 */
public class GobelinVert extends Mob {

    public GobelinVert(Vector2 position, CollisionManager collisionManager) {
        super(
            position,
            300f,
            new FollowPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de suive du joueur
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
    }

    @Override
    protected void initializeTextures() {
        // Charge la texture spécifique du Gobelin Vert et divise les directions
        texture = new Texture("mobs/gobelin_vert.png");
        TextureRegion[][] split = TextureRegion.split(texture, 64, 64);
        face = split[0][0];
        dos = split[0][1];
        droite = split[1][0];
        gauche = split[1][1];
        currentTexture = face;
    }
}
