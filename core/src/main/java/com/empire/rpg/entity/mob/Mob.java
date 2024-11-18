package com.empire.rpg.entity.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;

import java.util.ArrayList;
import java.util.List;

public abstract class Mob {
    // Constantes
    private static final float AGGRO_RADIUS = 300f;
    private static final int TILE_SIZE = 48;
    private static final float STOP_DISTANCE = 20f; // Distance d'arrêt en pixels
    private static final float MAX_BLOCKED_TIME = 0f; // Temps maximum en secondes avant une réorientation
    private static final float RETURN_TO_SPAWN_DISTANCE = 500f; // Distance max avant de revenir au spawn

    // Variables d'instance
    protected Vector2 position;
    protected Vector2 previousPosition;
    private final Vector2 spawnPosition;
    protected float speed;
    private Vector2 targetPosition;
    private List<Vector2> currentPath;

    private boolean isNearPlayer = false;
    private boolean isBlocked = false;
    private float blockedDuration = 0f;
    private Vector2 previousTarget = null;

    // Stratégies de pathfinding
    protected PathfindingStrategy currentStrategy;
    private final PathfindingStrategy passiveStrategy;
    private final PathfindingStrategy aggressiveStrategy;
    private final PathfindingStrategy goToStrategy;

    // Textures
    protected Texture texture;
    protected TextureRegion face, dos, droite, gauche;
    protected TextureRegion currentTexture;

    // Dimensions de la collision
    protected final float marginLeft = 15f;
    protected final float marginRight = 15f;
    protected final float marginTop = 40f;
    protected final float marginBottom = 5f;

    // Gestion des collisions
    protected CollisionManager collisionManager;

    // Liste de tous les mobs
    public static List<Mob> allMobs = new ArrayList<>();

    // Indicateur pour le retour au spawn
    private boolean isReturningToSpawn = false;

    // Constructeur
    public Mob(Vector2 position, float speed, PathfindingStrategy aggressiveStrategy,
               PathfindingStrategy goToStrategy, CollisionManager collisionManager) {
        this.position = position;
        this.previousPosition = new Vector2(position);
        this.spawnPosition = new Vector2(position); // Enregistre le point de spawn initial
        this.speed = speed;
        this.aggressiveStrategy = aggressiveStrategy;
        this.goToStrategy = goToStrategy;
        this.collisionManager = collisionManager;

        this.passiveStrategy = new RandomPathfindingStrategy(MobFactory.getPathfinding());
        this.currentStrategy = passiveStrategy;

        currentPath = new ArrayList<>();
        targetPosition = null;

        allMobs.add(this);
        initializeTextures();
    }

    // Méthode pour initialiser les textures
    protected abstract void initializeTextures();

    // Méthode pour obtenir les bordures de collision du mob
    public Rectangle getCollisionBounds() {
        float collisionWidth = 64 - (marginLeft + marginRight);
        float collisionHeight = 64 - (marginTop + marginBottom);
        float collisionX = position.x + collisionWidth / 2;
        float collisionY = position.y + collisionHeight / 2;
        return new Rectangle(collisionX, collisionY, collisionWidth, collisionHeight);
    }

    // Méthode pour calculer la distance entre les bordures de collision du mob et du joueur
    private float calculateCollisionDistance(PlayerCharacter player) {
        Rectangle mobBounds = this.getCollisionBounds();
        Rectangle playerBounds = player.getCollisionBounds();

        float dx = Math.max(0, Math.abs(mobBounds.x - playerBounds.x) - (mobBounds.width + playerBounds.width) / 2);
        float dy = Math.max(0, Math.abs(mobBounds.y - playerBounds.y) - (mobBounds.height + playerBounds.height) / 2);

        // Retourner la distance entre les bordures de collision
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // Mise à jour de la méthode update pour inclure l'arrêt basé sur la distance de collision
    public void update(float deltaTime, PlayerCharacter player, Camera camera) {
        previousPosition.set(position);

        if (!isVisible(camera)) {
            return;
        }

        // Vérifie la distance au spawn pour activer le retour
        boolean isFarFromSpawn = position.dst(spawnPosition) > RETURN_TO_SPAWN_DISTANCE;

        if (isFarFromSpawn) {
            if (!isReturningToSpawn) {
                isReturningToSpawn = true; // Activer le retour au spawn
                currentStrategy = goToStrategy;
                currentStrategy.calculatePath(this, spawnPosition, deltaTime); // Calculer le chemin vers le spawn
            }
        } else {
            isReturningToSpawn = false; // Désactiver le retour au spawn si proche
            currentStrategy = isPlayerInAggroRange(player) ? aggressiveStrategy : passiveStrategy;
        }

        // Vérifie la distance entre les collisions du mob et du joueur
        float collisionDistance = calculateCollisionDistance(player);
        if (collisionDistance < STOP_DISTANCE) {
            isNearPlayer = true;
            return; // Arrêter le mouvement si proche du joueur en fonction des collisions
        } else {
            isNearPlayer = false;
        }

        if (isReturningToSpawn || !isNearPlayer) {
            // Mettre à jour le chemin ou suivre le joueur
            if (currentPath.isEmpty() || targetPosition == null || (isBlocked && blockedDuration > MAX_BLOCKED_TIME)) {
                currentStrategy.calculatePath(this, isReturningToSpawn ? spawnPosition : player.getPosition(), deltaTime);
                smoothPath();
            }
            moveInStaircasePattern(deltaTime);
            updateTextureDirection();
        }

        handleCollisions(player, deltaTime);
    }

    private void handleCollisions(PlayerCharacter player, float deltaTime) {
        if (collisionManager.isColliding(this.getCollisionBounds(), player.getCollisionBounds())) {
            position.set(previousPosition);
            targetPosition = null;
        }

        boolean blockedByOtherMob = false;
        for (Mob otherMob : allMobs) {
            if (otherMob != this && collisionManager.isColliding(this.getCollisionBounds(), otherMob.getCollisionBounds())) {
                position.set(previousPosition);
                blockedByOtherMob = true;
                resolveMobBlocking(otherMob, deltaTime);
                break;
            }
        }
        setBlocked(blockedByOtherMob);
    }

    // Méthode pour vérifier si le joueur est dans la portée d'aggro
    private boolean isPlayerInAggroRange(PlayerCharacter player) {
        return position.dst(player.getPosition()) <= AGGRO_RADIUS;
    }

    // Méthode pour vérifier si le mob est visible dans la caméra
    private boolean isVisible(Camera camera) {
        float viewportHalfWidth = camera.viewportWidth / 2;
        float viewportHalfHeight = camera.viewportHeight / 2;

        float minX = camera.position.x - viewportHalfWidth;
        float maxX = camera.position.x + viewportHalfWidth;
        float minY = camera.position.y - viewportHalfHeight;
        float maxY = camera.position.y + viewportHalfHeight;

        return position.x >= minX && position.x <= maxX && position.y >= minY && position.y <= maxY;
    }

    // Définir le chemin pour le mob
    public void setPath(List<Vector2> path) {
        this.currentPath = path;
        if (!currentPath.isEmpty()) {
            targetPosition = currentPath.remove(0);
        }
    }

    // Déplacement en mode "staircase"
    private void moveInStaircasePattern(float deltaTime) {
        if (targetPosition == null) {
            return;
        }

        float distanceToTarget = position.dst(targetPosition);

        if (distanceToTarget < speed * deltaTime) {
            position.set(targetPosition);
            targetPosition = !currentPath.isEmpty() ? currentPath.remove(0) : null;

            if (targetPosition == null) {
                currentStrategy.calculatePath(this, null, deltaTime);
            }
        } else {
            float deltaX = targetPosition.x - position.x;
            float deltaY = targetPosition.y - position.y;

            Vector2 newPosition = position.cpy();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                newPosition.x += Math.signum(deltaX) * speed * deltaTime;
                if (Math.abs(newPosition.x - targetPosition.x) < speed * deltaTime) {
                    newPosition.x = targetPosition.x;
                }
            } else {
                newPosition.y += Math.signum(deltaY) * speed * deltaTime;
                if (Math.abs(newPosition.y - targetPosition.y) < speed * deltaTime) {
                    newPosition.y = targetPosition.y;
                }
            }

            position.interpolate(newPosition, 0.5f, Interpolation.smooth);
        }
    }

    // Mise à jour de la direction de la texture du mob
    private void updateTextureDirection() {
        if (targetPosition == null) return;

        float deltaX = targetPosition.x - position.x;
        float deltaY = targetPosition.y - position.y;

        if (Math.abs(deltaX) > 0 && Math.abs(deltaY) > 0) {
            currentTexture = (deltaX > 0) ? droite : gauche;
        } else if (Math.abs(deltaX) > Math.abs(deltaY)) {
            currentTexture = (deltaX > 0) ? droite : gauche;
        } else {
            currentTexture = (deltaY > 0) ? dos : face;
        }
    }

    // Méthode pour ajuster la direction du mob pour regarder le joueur
    private void lookAtPlayer(PlayerCharacter player) {
        float deltaX = player.getX() - position.x;
        float deltaY = player.getY() - position.y;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            currentTexture = (deltaX > 0) ? droite : gauche;
        } else {
            currentTexture = (deltaY > 0) ? face : dos;
        }
    }

    // Méthode pour réagir aux blocages par un autre mob
    private void resolveMobBlocking(Mob otherMob, float deltaTime) {
        if (blockedDuration > MAX_BLOCKED_TIME) {
            Vector2 avoidanceDirection = new Vector2(position).sub(otherMob.getPosition()).rotateDeg(45).nor().scl(10f);
            position.add(avoidanceDirection.scl(deltaTime));
            targetPosition = null;
            blockedDuration = 0;
        } else {
            Vector2 separationForce = new Vector2(position).sub(otherMob.getPosition()).nor().scl(5f);
            position.add(separationForce.scl(deltaTime));
        }
    }

    // Définir si le mob est bloqué
    public void setBlocked(boolean blocked) {
        if (blocked) {
            blockedDuration += Gdx.graphics.getDeltaTime();
        } else {
            blockedDuration = 0f;
        }
        this.isBlocked = blocked;
    }

    // Méthode pour lisser le chemin calculé
    private void smoothPath() {
        List<Vector2> smoothedPath = new ArrayList<>();
        if (currentPath.size() < 2) {
            return;
        }

        smoothedPath.add(currentPath.get(0));

        for (int i = 1; i < currentPath.size() - 1; i++) {
            Vector2 prev = smoothedPath.get(smoothedPath.size() - 1);
            Vector2 next = currentPath.get(i + 1);

            if (!isBlocked(prev, next)) {
                continue;
            }
            smoothedPath.add(currentPath.get(i));
        }

        smoothedPath.add(currentPath.get(currentPath.size() - 1));
        currentPath = smoothedPath;
    }

    // Méthode pour vérifier si un chemin est bloqué
    private boolean isBlocked(Vector2 from, Vector2 to) {
        return false;
    }

    // Méthodes de récupération des données
    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        Vector2 direction = new Vector2(position).sub(previousPosition);
        if (direction.len() != 0) {
            direction.nor();
        }
        return direction;
    }

    public TextureRegion getCurrentTexture() {
        return currentTexture;
    }

    public List<Vector2> getPathPoints() {
        return currentPath;
    }

    // Méthode de nettoyage de la texture
    public void dispose() {
        texture.dispose();
    }

    // Vérifie si le mob est bloqué
    public boolean isBlocked() {
        return isBlocked;
    }
}
