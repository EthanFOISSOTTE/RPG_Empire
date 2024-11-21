package com.empire.rpg.entity.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.component.pathfinding.*;

import com.empire.rpg.entity.MOB;
import com.empire.rpg.entity.player.PlayerCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Classe abstraite représentant un Mob dans le jeu, étendant l'entité MOB et utilisant des composants.
 */
public abstract class Mob extends MOB {
    // Constantes
    private static final float AGGRO_RADIUS = 300f;
    private static final float STOP_DISTANCE = 20f; // Distance d'arrêt en pixels
    private static final float MAX_BLOCKED_TIME = 0f; // Temps maximum en secondes avant une réorientation
    private static final float RETURN_TO_SPAWN_DISTANCE = 500f; // Distance max avant de revenir au spawn

    // Variables d'instance
    protected Vector2 targetPosition;
    protected List<Vector2> currentPath; // Changé de private à protected

    private boolean isNearPlayer = false;
    private boolean isBlocked = false;
    private float blockedDuration = 0f;

    // Stratégies de pathfinding
    protected PathfindingStrategy currentStrategy;
    private final PathfindingStrategy passiveStrategy;
    private final PathfindingStrategy aggressiveStrategy;
    private final PathfindingStrategy goToStrategy;

    // Textures
    protected Texture texture;
    protected TextureRegion face, dos, droite, gauche;
    protected TextureRegion currentTexture;

    // Gestion des collisions
    protected CollisionManager collisionManager;

    // Liste de tous les mobs
    public static List<Mob> allMobs = new ArrayList<>();

    // Indicateur pour le retour au spawn
    private boolean isReturningToSpawn = false;

    // Vitesse du mob
    protected float speed;

    // Position précédente pour restauration et direction
    protected Vector2 previousPosition;

    // Direction actuelle basée sur l'intention de mouvement
    protected Vector2 currentDirection = new Vector2(0, 0);

    // Référence au CollisionComponent de la map de composants
    protected CollisionComponent collisionComponent;

    // Facteur de zoom (scale)
    protected float scale = 1.0f; // Par défaut, aucun zoom

    // Offsets pour le rendu
    protected float offsetX = 0f;
    protected float offsetY = 0f;

    /**
     * Getter pour le facteur de zoom.
     *
     * @return Le facteur de zoom.
     */
    public float getScale() {
        return scale;
    }

    /**
     * Getter pour l'offset X.
     *
     * @return L'offset X.
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     * Getter pour l'offset Y.
     *
     * @return L'offset Y.
     */
    public float getOffsetY() {
        return offsetY;
    }

    // Constructeur
    public Mob(String name, UUID id, Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components,
               float speed, PathfindingStrategy aggressiveStrategy,
               PathfindingStrategy goToStrategy, CollisionManager collisionManager) {
        super(name, components, id);
        this.collisionManager = collisionManager;
        this.aggressiveStrategy = aggressiveStrategy;
        this.goToStrategy = goToStrategy;
        this.passiveStrategy = new RandomPathfindingStrategy(MobFactory.getPathfinding());
        this.currentStrategy = passiveStrategy;

        this.speed = speed;
        this.currentPath = new ArrayList<>();
        this.targetPosition = null;
        this.previousPosition = new Vector2();

        // Récupérer la référence du CollisionComponent depuis la map de composants
        this.collisionComponent = (CollisionComponent) getComponent(CollisionComponent.class);
        if (this.collisionComponent == null) {
            throw new IllegalArgumentException("CollisionComponent is required in components map");
        }

        allMobs.add(this);
        initializeTextures();

        // Initialiser previousPosition avec la position actuelle
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent != null) {
            previousPosition.set(posComponent.getX(), posComponent.getY());
        }
    }

    // Méthode pour initialiser les textures
    protected abstract void initializeTextures();

    // Méthode pour obtenir les bordures de collision du mob
    public Rectangle getCollisionBounds() {
        CollisionComponent collision = (CollisionComponent) getComponent(CollisionComponent.class);
        if (collision != null) {
            return collision.getBoundingBox();
        }
        return new Rectangle();
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

    /**
     * Méthode concrète pour mettre à jour le mob.
     * Les sous-classes peuvent appeler super.update() pour exécuter la logique de base.
     *
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour.
     * @param player    Le joueur à proximité.
     * @param camera    La caméra du jeu.
     */
    public void update(float deltaTime, PlayerCharacter player, Camera camera) {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent == null) {
            return;
        }
        Vector2 position = new Vector2(posComponent.getX(), posComponent.getY());

        if (!isVisible(camera)) {
            return;
        }

        // Vérifie la distance au spawn pour activer le retour
        boolean isFarFromSpawn = position.dst(getSpawnPosition()) > RETURN_TO_SPAWN_DISTANCE;

        if (isFarFromSpawn) {
            if (!isReturningToSpawn) {
                isReturningToSpawn = true; // Activer le retour au spawn
                currentStrategy = goToStrategy;
                currentStrategy.calculatePath(this, getSpawnPosition(), deltaTime); // Calculer le chemin vers le spawn
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
                Vector2 target = isReturningToSpawn ? getSpawnPosition() : player.getPositionVector();
                currentStrategy.calculatePath(this, target, deltaTime);
                smoothPath();
            }
            moveInStaircasePattern(deltaTime);
            updateTextureDirection();
        }

        handleCollisions(player, deltaTime);
    }

    private Vector2 getSpawnPosition() {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent != null) {
            return new Vector2(posComponent.getX(), posComponent.getY());
        }
        return new Vector2();
    }

    private void handleCollisions(PlayerCharacter player, float deltaTime) {
        if (collisionManager.isColliding(this.getCollisionBounds(), player.getCollisionBounds())) {
            restorePreviousPosition();
            targetPosition = null;
        }

        boolean blockedByOtherMob = false;
        for (Mob otherMob : allMobs) {
            if (otherMob != this && collisionManager.isColliding(this.getCollisionBounds(), otherMob.getCollisionBounds())) {
                restorePreviousPosition();
                blockedByOtherMob = true;
                resolveMobBlocking(otherMob, deltaTime);
                break;
            }
        }
        setBlocked(blockedByOtherMob);
    }

    // Méthode pour vérifier si le joueur est dans la portée d'aggro
    private boolean isPlayerInAggroRange(PlayerCharacter player) {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent == null) {
            return false;
        }
        Vector2 playerPos = player.getPositionVector();
        Vector2 mobPos = new Vector2(posComponent.getX(), posComponent.getY());
        return mobPos.dst(playerPos) <= AGGRO_RADIUS;
    }

    // Méthode pour vérifier si le mob est visible dans la caméra
    private boolean isVisible(Camera camera) {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent == null) {
            return false;
        }
        float viewportHalfWidth = camera.viewportWidth / 2;
        float viewportHalfHeight = camera.viewportHeight / 2;

        float minX = camera.position.x - viewportHalfWidth;
        float maxX = camera.position.x + viewportHalfWidth;
        float minY = camera.position.y - viewportHalfHeight;
        float maxY = camera.position.y + viewportHalfHeight;

        return posComponent.getX() >= minX && posComponent.getX() <= maxX
            && posComponent.getY() >= minY && posComponent.getY() <= maxY;
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

        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent == null) {
            return;
        }
        Vector2 position = new Vector2(posComponent.getX(), posComponent.getY());

        float distanceToTarget = position.dst(targetPosition);

        if (distanceToTarget < speed * deltaTime) {
            // Sauvegarder la position actuelle avant de la modifier
            previousPosition.set(posComponent.getX(), posComponent.getY());

            posComponent.setPosition(targetPosition.x, targetPosition.y);
            targetPosition = !currentPath.isEmpty() ? currentPath.remove(0) : null;

            if (targetPosition == null) {
                currentStrategy.calculatePath(this, null, deltaTime);
            }
        } else {
            float deltaX = targetPosition.x - position.x;
            float deltaY = targetPosition.y - position.y;

            Vector2 movement = new Vector2(deltaX, deltaY).nor().scl(speed * deltaTime);
            Vector2 newPosition = position.add(movement);

            // Sauvegarder la position actuelle avant de la modifier
            previousPosition.set(posComponent.getX(), posComponent.getY());

            posComponent.setPosition(newPosition.x, newPosition.y);
            position.set(newPosition);

            // Mettre à jour la bounding box après le déplacement en utilisant les données du CollisionComponent
            updateCollisionBoundingBox(posComponent);

            // Mettre à jour la direction actuelle
            currentDirection.set(movement).nor();
        }
    }

    /**
     * Met à jour la bounding box du CollisionComponent en fonction de la position actuelle du mob.
     *
     * @param posComponent Le PositionComponent du mob.
     */
    protected void updateCollisionBoundingBox(PositionComponent posComponent) {
        Rectangle collisionBox = collisionComponent.getBoundingBox();
        // Calculer la nouvelle position de la bounding box en centrant sur la position actuelle du mob
        float newX = posComponent.getX() + (collisionBox.width / 2);
        float newY = posComponent.getY() + (collisionBox.height / 2);
        collisionComponent.setBoundingBox(new Rectangle(newX, newY, collisionBox.width, collisionBox.height));

        // Log pour vérifier les nouvelles positions
        // System.out.println("Updated Collision Box for " + getName() + ": (" + newX + ", " + newY + ", " + collisionBox.width + ", " + collisionBox.height + ")");
    }

    // Mise à jour de la direction de la texture du mob
    protected void updateTextureDirection() {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent == null || targetPosition == null) {
            return;
        }

        float deltaX = targetPosition.x - posComponent.getX();
        float deltaY = targetPosition.y - posComponent.getY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            currentTexture = (deltaX > 0) ? droite : gauche;
        } else {
            currentTexture = (deltaY > 0) ? dos : face;
        }
    }

    // Méthode pour réagir aux blocages par un autre mob
    protected void resolveMobBlocking(Mob otherMob, float deltaTime) {
        if (blockedDuration > MAX_BLOCKED_TIME) {
            PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
            PositionComponent otherPos = (PositionComponent) otherMob.getComponent(PositionComponent.class);
            if (posComponent != null && otherPos != null) {
                Vector2 avoidanceDirection = new Vector2(posComponent.getX(), posComponent.getY())
                    .sub(otherPos.getX(), otherPos.getY()).nor().scl(10f);
                posComponent.setPosition(posComponent.getX() + avoidanceDirection.x * deltaTime,
                    posComponent.getY() + avoidanceDirection.y * deltaTime);

                // Mettre à jour la bounding box après le déplacement
                updateCollisionBoundingBox(posComponent);

                targetPosition = null;
                blockedDuration = 0;
            }
        } else {
            PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
            PositionComponent otherPos = (PositionComponent) otherMob.getComponent(PositionComponent.class);
            if (posComponent != null && otherPos != null) {
                Vector2 separationForce = new Vector2(posComponent.getX(), posComponent.getY())
                    .sub(otherPos.getX(), otherPos.getY()).nor().scl(5f);
                posComponent.setPosition(posComponent.getX() + separationForce.x * deltaTime,
                    posComponent.getY() + separationForce.y * deltaTime);

                // Mettre à jour la bounding box après le déplacement
                updateCollisionBoundingBox(posComponent);
            }
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
    protected void smoothPath() {
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

    /**
     * Méthode pour vérifier si un chemin est bloqué.
     *
     * @param from Le point de départ.
     * @param to   Le point d'arrivée.
     * @return True si le chemin est bloqué, sinon false.
     */
    protected boolean isBlocked(Vector2 from, Vector2 to) {
        // Implémenter la logique de vérification si nécessaire
        return false;
    }

    /**
     * Méthode pour restaurer la position précédente en cas de collision.
     */
    public void restorePreviousPosition() {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent != null) {
            posComponent.setPosition(previousPosition.x, previousPosition.y);

            // Mettre à jour la bounding box après la restauration
            updateCollisionBoundingBox(posComponent);
        }
    }

    /**
     * Méthode pour obtenir la position actuelle du mob.
     *
     * @return La position actuelle sous forme de Vector2.
     */
    public Vector2 getPositionVector() {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent != null) {
            return new Vector2(posComponent.getX(), posComponent.getY());
        }
        return new Vector2();
    }

    /**
     * Méthode abstraite pour fournir les informations de mort spécifiques au mob.
     *
     * @return Une map avec les paramètres de mort.
     */
    protected abstract Map<String, Object> getDeathInfo();

    /**
     * Méthode pour afficher les informations de mort sous forme de tableau dans la console.
     */
    public void printDeathInfo() {
        Map<String, Object> deathInfo = getDeathInfo();
        System.out.println("----- Informations de Mort -----");
        System.out.printf("| %-20s | %-15s |\n", "Paramètre", "Valeur");
        System.out.println("--------------------------------");
        for (Map.Entry<String, Object> entry : deathInfo.entrySet()) {
            System.out.printf("| %-20s | %-15s |\n", entry.getKey(), entry.getValue());
        }
        System.out.println("--------------------------------");
    }

    /**
     * Gère la logique de mort du mob.
     */
    protected void onDeath() {
        System.out.println(getName() + " est mort !");
        printDeathInfo(); // Afficher les informations de mort
        dispose(); // Libérer les ressources si nécessaire
        // La suppression de la liste est gérée ailleurs
    }

    /**
     * Méthode pour appliquer des dégâts au mob.
     *
     * @param damage Les dégâts à appliquer.
     */
    public void takeDamage(float damage) {
        HealthComponent health = (HealthComponent) getComponent(HealthComponent.class);
        if (health != null) {
            health.takeDamage((int) damage);
            System.out.println(getName() + " a subi " + (int) damage + " dégâts. PV restants: " + health.getCurrentHealthPoints() + "/" + health.getMaxHealthPoints());
            if (health.getCurrentHealthPoints() <= 0) {
                onDeath();
            }
        }
    }

    // Méthodes de récupération des données
    public Vector2 getPosition() {
        PositionComponent posComponent = (PositionComponent) getComponent(PositionComponent.class);
        if (posComponent != null) {
            return new Vector2(posComponent.getX(), posComponent.getY());
        }
        return new Vector2();
    }

    public Vector2 getDirection() {
        return new Vector2(currentDirection);
    }

    public TextureRegion getCurrentTexture() {
        return currentTexture;
    }

    /**
     * Méthode de nettoyage des ressources du mob.
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}