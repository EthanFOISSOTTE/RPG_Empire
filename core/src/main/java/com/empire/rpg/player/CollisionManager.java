package com.empire.rpg.player;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionManager {
    private final Array<Rectangle> collisionRectangles = new Array<>();
    private final Array<Ellipse> collisionEllipses = new Array<>();
    private final Array<Polygon> collisionPolygons = new Array<>();

    public CollisionManager(TiledMap tiledMap) {
        loadCollisionObjects(tiledMap);
    }

    // Méthode pour charger les objets de collision de la carte
    private void loadCollisionObjects(TiledMap tiledMap) {
        MapLayer collisionLayer = tiledMap.getLayers().get("collision");
        if (collisionLayer != null) {
            MapObjects objects = collisionLayer.getObjects();

            for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                collisionRectangles.add(rectangleObject.getRectangle());
            }
            for (EllipseMapObject ellipseObject : objects.getByType(EllipseMapObject.class)) {
                collisionEllipses.add(ellipseObject.getEllipse());
            }
            for (PolygonMapObject polygonObject : objects.getByType(PolygonMapObject.class)) {
                collisionPolygons.add(polygonObject.getPolygon());
            }
        }
    }

    // Méthode pour ajouter un rectangle de collision supplémentaire
    public void addCollisionRectangle(Rectangle rectangle) {
        collisionRectangles.add(rectangle);
    }

    // Méthode pour vérifier les collisions avec un rectangle
    public boolean isColliding(Rectangle playerRect) {
        // Vérifier les collisions avec les rectangles
        for (Rectangle rect : collisionRectangles) {
            if (playerRect.overlaps(rect)) {
                return true;
            }
        }

        // Vérifier les collisions avec les ellipses (approximation par cercle)
        for (Ellipse ellipse : collisionEllipses) {
            float ellipseCenterX = ellipse.x + ellipse.width / 2;
            float ellipseCenterY = ellipse.y + ellipse.height / 2;
            float radius = Math.min(ellipse.width, ellipse.height) / 2;

            if (overlapsCircleRectangle(ellipseCenterX, ellipseCenterY, radius, playerRect)) {
                return true;
            }
        }

        // Vérifier les collisions avec les polygones
        for (Polygon polygon : collisionPolygons) {
            if (Intersector.overlapConvexPolygons(new Polygon(new float[]{
                playerRect.x, playerRect.y,
                playerRect.x + playerRect.width, playerRect.y,
                playerRect.x + playerRect.width, playerRect.y + playerRect.height,
                playerRect.x, playerRect.y + playerRect.height
            }), polygon)) {
                return true;
            }
        }

        return false;
    }

    // Vérification de la collision entre un cercle et un rectangle
    private boolean overlapsCircleRectangle(float circleX, float circleY, float radius, Rectangle rectangle) {
        float closestX = Math.max(rectangle.x, Math.min(circleX, rectangle.x + rectangle.width));
        float closestY = Math.max(rectangle.y, Math.min(circleY, rectangle.y + rectangle.height));

        float distanceX = circleX - closestX;
        float distanceY = circleY - closestY;

        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }
}
