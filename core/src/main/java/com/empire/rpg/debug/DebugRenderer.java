package com.empire.rpg.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.states.AttackingState;
import com.empire.rpg.entity.player.attacks.Attack;
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

    public void renderDebugBounds(Camera camera, PlayerCharacter player, CollisionManager collisionManager) {
        // Gestion des entrées pour ajouter/soustraire des points de vie
        handleHealthModification(player);

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

        // Dessiner le nom du joueur
        String playerName = player.getName();
        glyphLayout.setText(font, "Name: " + playerName);
        float nameTextWidth = glyphLayout.width;
        float nameTextHeight = glyphLayout.height;
        font.draw(spriteBatch, "Name: " + playerName, playerTextX - nameTextWidth / 2, playerTextY + 15 + nameTextHeight);

        // Dessiner la santé du joueur
        int currentHealth = player.getHealth();
        int maxHealth = player.getMaxHealth();
        String healthText = "Health: " + currentHealth + "/" + maxHealth;
        glyphLayout.setText(font, healthText);
        float healthTextWidth = glyphLayout.width;
        float healthTextHeight = glyphLayout.height;
        font.draw(spriteBatch, healthText, playerTextX - healthTextWidth / 2, playerTextY + 30 + healthTextHeight);

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

    /**
     * Gère l'ajout et la soustraction de points de vie via les touches "P" et "O".
     *
     * @param player Le joueur dont les points de vie sont modifiés.
     */
    private void handleHealthModification(PlayerCharacter player) {
        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
        if (health == null) {
            System.out.println("HealthComponent missing");
            return;
        }

        // Ajouter 5 HP lorsque la touche "P" est pressée
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            int healedHealth = health.heal(5);
            System.out.println("Health increased to " + healedHealth + "/" + health.getMaxHealthPoints());
        }

        // Soustraire 5 HP lorsque la touche "O" est pressée
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            int damagedHealth = health.takeDamage(5);
            System.out.println("Health decreased to " + damagedHealth + "/" + health.getMaxHealthPoints());
        }
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
