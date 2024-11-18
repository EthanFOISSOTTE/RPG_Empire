package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.*;

/**
 * Interface pour les stratégies de pathfinding utilisées par les mobs.
 */
public interface PathfindingStrategy {
    /**
     * Calcule le chemin vers une cible donnée en fonction de la stratégie.
     * @param mob Mob qui utilise cette stratégie
     * @param target Position cible
     * @param deltaTime Temps écoulé depuis la dernière mise à jour
     */
    void calculatePath(Mob mob, Vector2 target, float deltaTime);
}
