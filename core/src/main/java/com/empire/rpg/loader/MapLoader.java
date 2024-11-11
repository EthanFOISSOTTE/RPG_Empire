package com.empire.rpg.loader;

import com.empire.rpg.entity.*;
import com.empire.rpg.component.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import java.util.Map;
import java.util.UUID;

/**
 * Classe MapLoader pour charger et gérer les entités et objets d'une carte Tiled.
 */

public class MapLoader {

    private TiledMap tiledMap;

    /**
     * Constructeur pour charger la carte depuis un fichier.
     *
     * @param mapPath Chemin du fichier de la carte.
     */
    public MapLoader(String mapPath) {
        this.tiledMap = new TmxMapLoader().load(mapPath);
    }

    /**
     * Charge les entités et objets depuis les couches de la carte.
     *
     * @param entityManager Gestionnaire d'entités pour ajouter les entités chargées.
     */
    public void loadEntities(EntityManager entityManager) {
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer.getName().equals("Entities")) {
                loadEntitiesFromLayer(layer, entityManager);
            } else if (layer.getName().equals("Objects")) {
                loadObjectsFromLayer(layer, entityManager);
            }
        }
    }

    /**
     * Charge les entités depuis une couche spécifique.
     *
     * @param layer Calque de la carte contenant les entités.
     * @param entityManager Gestionnaire d'entités pour ajouter les entités chargées.
     */
    private void loadEntitiesFromLayer(MapLayer layer, EntityManager entityManager) {
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                UUID id = UUID.randomUUID();

                Entity entity = null;

                if (object.getName().equals("MOB")) {
                    entity = createMOB(rect, id);
                } else if (object.getName().equals("PNJ")) {
                    entity = createPNJ(rect, id);
                }

                if (entity != null) {
                    entityManager.addEntity(entity);
                }
            }
        }
    }

    /**
     * Charge les objets depuis une couche spécifique.
     *
     * @param layer Calque de la carte contenant les objets.
     * @param entityManager Gestionnaire d'entités pour ajouter les objets chargés.
     */
    private void loadObjectsFromLayer(MapLayer layer, EntityManager entityManager) {
        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                UUID id = UUID.randomUUID();

                Entity entity = null;

                if (object.getName().equals("Sign")) {
                    entity = createSign(rect, id);
                }

                if (entity != null) {
                    entityManager.addEntity(entity);
                }
            }
        }
    }

    /**
     * Crée une entité MOB avec les composants appropriés.
     *
     * @param rect Rectangle définissant la position de l'entité.
     * @param id Identifiant unique de l'entité.
     * @return Une nouvelle entité MOB.
     */
    private Entity createMOB(Rectangle rect, UUID id) {
        Map<Class<? extends Component>, Component> components = Map.of(
            PositionComponent.class, new PositionComponent(rect.x, rect.y),
            HealthComponent.class, new HealthComponent(50, 50),
            CollisionComponent.class, new CollisionComponent(true),
            MovementComponent.class, new MovementComponent(1.0f, "south")
        );
        return new MOB("Orc", components, id);
    }

    /**
     * Crée une entité PNJ avec les composants appropriés.
     *
     * @param rect Rectangle définissant la position de l'entité.
     * @param id Identifiant unique de l'entité.
     * @return Une nouvelle entité PNJ.
     */
    private Entity createPNJ(Rectangle rect, UUID id) {
        Map<Class<? extends Component>, Component> components = Map.of(
            PositionComponent.class, new PositionComponent(rect.x, rect.y),
            HealthComponent.class, new HealthComponent(100, 100),
            CollisionComponent.class, new CollisionComponent(false)
        );
        return new PNJ("Villageois", components, id, "Bonjour, aventurier !", 0);
    }

    /**
     * Crée une entité Sign avec les composants appropriés.
     *
     * @param rect Rectangle définissant la position de l'entité.
     * @param id Identifiant unique de l'entité.
     * @return Une nouvelle entité Sign.
     */
    private Entity createSign(Rectangle rect, UUID id) {
        Map<Class<? extends Component>, Component> components = Map.of(
            PositionComponent.class, new PositionComponent(rect.x, rect.y),
            StateComponent.class, new StateComponent(false, "fermé"),
            CollisionComponent.class, new CollisionComponent(false)
        );
        return new Sign("Signpost", components, id);
    }
}
