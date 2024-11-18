package com.empire.rpg.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.CollisionManager;

/**
 * Fabrique pour créer des instances de mobs avec des comportements définis.
 */
public class MobFactory {
    private static Pathfinding pathfinding;

    /**
     * Définit l'instance de pathfinding pour les mobs.
     * @param pathfindingInstance Instance de pathfinding
     */
    public static void setPathfinding(Pathfinding pathfindingInstance) {
        pathfinding = pathfindingInstance;
    }

    /**
     * Accède à l'instance de pathfinding.
     * @return Instance de pathfinding
     */
    public static Pathfinding getPathfinding() {
        return pathfinding;
    }

    /**
     * Crée un mob de type spécifique à une position donnée.
     * @param type Type de mob (ex: "gobelinvert")
     * @param position Position initiale du mob
     * @return Instance de Mob
     */
    public static Mob createMob(String type, Vector2 position, CollisionManager collisionManager) {
        switch (type.toLowerCase()) {
            case "gobelinvert":
                return new GobelinVert(position, collisionManager);
            case "gobelinrouge":
                return new GobelinRouge(position, collisionManager);
            case "chatgris":
                return new ChatGris(position, collisionManager);
            case "lapinblanc":
                return new LapinBlanc(position, collisionManager);
            default:
                throw new IllegalArgumentException("Type de mob inconnu: " + type);
        }
    }
}
