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
import com.empire.rpg.player.states.AttackingState;
import com.empire.rpg.player.attacks.Attack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DebugRenderer {
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch spriteBatch;
    private final GlyphLayout glyphLayout;
    private static final float DIRECTION_LINE_LENGTH = 50f;

    public DebugRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.spriteBatch = new SpriteBatch();
        this.font.setColor(Color.WHITE);
        this.glyphLayout = new GlyphLayout();
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

        // Dessiner la zone d'effet de l'attaque si le joueur est en train d'attaquer
        if (player.getCurrentState() instanceof AttackingState) {
            AttackingState attackingState = (AttackingState) player.getCurrentState();
            Polygon attackHitbox = attackingState.getAttackHitbox();
            if (attackHitbox != null) {
                shapeRenderer.setColor(Color.ORANGE);
                shapeRenderer.polygon(attackHitbox.getTransformedVertices());
            }
        }

        shapeRenderer.end();

        // Dessiner les textes
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Dessiner les coordonnées X et Y du joueur
        float playerTextX = playerBounds.x + playerBounds.width / 2;
        float playerTextY = playerBounds.y + playerBounds.height + 15; // Position légèrement au-dessus du joueur
        font.draw(spriteBatch, "X: " + (int) playerBounds.x + " Y: " + (int) playerBounds.y, playerTextX, playerTextY);

        // Dessiner le texte sur la hitbox de l'attaque
        if (player.getCurrentState() instanceof AttackingState) {
            AttackingState attackingState = (AttackingState) player.getCurrentState();
            Polygon attackHitbox = attackingState.getAttackHitbox();
            if (attackHitbox != null) {
                Attack attack = attackingState.getAttack();
                if (attack != null) {
                    // Calculer le centre du polygone
                    Vector2 centroid = getPolygonCentroid(attackHitbox);

                    // Préparer le texte
                    String attackInfo = "ID: " + attack.getId() + " Damage: " + attack.getDamage();

                    // Mesurer la taille du texte pour le centrer
                    glyphLayout.setText(font, attackInfo);
                    float textWidth = glyphLayout.width;
                    float textHeight = glyphLayout.height;

                    // Dessiner le texte au centre de la hitbox
                    font.draw(spriteBatch, attackInfo, centroid.x - textWidth / 2, centroid.y + textHeight / 2);
                }
            }
        }

        spriteBatch.end();
    }

    private Vector2 getPolygonCentroid(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        float centroidX = 0, centroidY = 0;
        int numVertices = vertices.length / 2;
        for (int i = 0; i < vertices.length; i += 2) {
            centroidX += vertices[i];
            centroidY += vertices[i + 1];
        }
        centroidX /= numVertices;
        centroidY /= numVertices;
        return new Vector2(centroidX, centroidY);
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        spriteBatch.dispose();
    }
}
