package com.empire.rpg.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.component.pathfinding.Pathfinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Factory pour créer des mobs en tant qu'entités avec des composants.
 */
public class MobFactory {
    private static Pathfinding pathfinding;

    public static void setPathfinding(Pathfinding pathfindingInstance) {
        pathfinding = pathfindingInstance;
    }

    public static Pathfinding getPathfinding() {
        return pathfinding;
    }

    public static void createMob(String type, Vector2 position, CollisionManager collisionManager) {
        Mob mob = null;
        switch (type.toLowerCase()) {
            case "gobelinrouge":
                mob = new GobelinRouge(position, collisionManager);
                break;
            case "gobelinvert":
                mob = new GobelinVert(position, collisionManager);
                break;
            case "chatgris":
                mob = new ChatGris(position, collisionManager);
                break;
            case "lapinblanc":
                mob = new LapinBlanc(position, collisionManager);
                break;
            default:
                System.out.println("Type de mob inconnu: " + type);
        }

        if (mob != null) {
            // Mob ajouté à la liste des mobs dans la classe Mob
            // Pas besoin d'action supplémentaire car Mob ajoute automatiquement à allMobs
        }
    }
}
