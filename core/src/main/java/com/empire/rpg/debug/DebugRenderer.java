package com.empire.rpg.debug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.Player;
import com.badlogic.gdx.utils.Array;

public class DebugRenderer {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch spriteBatch; // Nécessaire pour dessiner le texte
    private static final float DIRECTION_LINE_LENGTH = 50f;

    public DebugRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont(); // Initialisation du BitmapFont pour afficher le texte
        this.spriteBatch = new SpriteBatch(); // Initialisation du SpriteBatch pour dessiner le texte
        this.font.setColor(Color.WHITE); // Couleur blanche pour le texte
    }

    public void renderDebugBounds(Camera camera, Player player, CollisionManager collisionManager) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Dessiner la boîte de collision du joueur
        Rectangle playerBounds = player.getCollisionBounds();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);

        // Dessiner la ligne de direction du joueur
        float playerCenterX = playerBounds.x + playerBounds.width / 2;
        float playerCenterY = playerBounds.y + playerBounds.height / 2;

        shapeRenderer.setColor(Color.YELLOW);
        Vector2 playerDirection = player.getDirection();
        shapeRenderer.line(playerCenterX, playerCenterY,
            playerCenterX + playerDirection.x * DIRECTION_LINE_LENGTH,
            playerCenterY + playerDirection.y * DIRECTION_LINE_LENGTH);

        // Dessiner les objets de collision de la carte
        shapeRenderer.setColor(Color.RED);
        Array<Rectangle> collisionRectangles = collisionManager.getCollisionRectangles();
        for (Rectangle rect : collisionRectangles) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        Array<Ellipse> collisionEllipses = collisionManager.getCollisionEllipses();
        for (Ellipse ellipse : collisionEllipses) {
            shapeRenderer.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }
        Array<Polygon> collisionPolygons = collisionManager.getCollisionPolygons();
        for (Polygon polygon : collisionPolygons) {
            shapeRenderer.polygon(polygon.getTransformedVertices());
        }

        shapeRenderer.end();

        // Dessiner les coordonnées X et Y du joueur
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        float playerTextX = playerBounds.x + playerBounds.width / 2;
        float playerTextY = playerBounds.y + playerBounds.height + 15; // Position légèrement au-dessus du joueur
        font.draw(spriteBatch, "X: " + (int) playerBounds.x + " Y: " + (int) playerBounds.y, playerTextX, playerTextY);
        spriteBatch.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        spriteBatch.dispose();
    }
}
