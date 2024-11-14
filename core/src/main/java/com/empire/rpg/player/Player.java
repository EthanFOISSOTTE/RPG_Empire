package com.empire.rpg.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.empire.rpg.collisions.CollisionManager;
import com.empire.rpg.player.animations.AnimationController;
import com.empire.rpg.player.animations.AnimationState;
import com.empire.rpg.player.components.*;
import com.empire.rpg.player.rendering.Renderer;
import com.empire.rpg.player.states.State;
import com.empire.rpg.player.states.StandingState;
import com.empire.rpg.player.states.AttackingState;
import com.empire.rpg.player.utils.Constants;
import com.empire.rpg.player.equipment.Tool;
import com.empire.rpg.player.attacks.Attack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Player {
    private AnimationController animationController;
    private Renderer renderer;
    private Body body;
    private Outfit outfit;
    private Hair hair;
    private Hat hat;
    private Tool1 tool1;
    private Tool2 tool2;
    private State currentState;
    private float x;
    private float y;
    private float speed;
    private float scale;

    // Variables pour la direction
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean running;
    private AnimationState lastFacingDirection;

    // Outils équipés
    private Tool currentTool1;
    private Tool currentTool2;

    // Gestion des cooldowns des attaques
    private Map<String, Float> attackCooldowns;

    // CollisionComponent
    private CollisionComponent collisionComponent;

    // Constructeurs
    public Player() {
        this(0f, 0f, Constants.PLAYER_SCALE);
    }

    public Player(float x, float y) {
        this(x, y, Constants.PLAYER_SCALE);
    }

    public Player(float x, float y, float scale) {
        this.body = new Body();
        this.outfit = new Outfit();
        this.hair = new Hair();
        this.hat = new Hat();
        this.tool1 = new Tool1();
        this.tool2 = new Tool2();

        this.x = x;
        this.y = y;
        this.scale = scale;
        this.speed = Constants.PLAYER_WALKING_SPEED;
        this.currentTool1 = Constants.TOOLS.get("SW01");
        this.currentTool2 = Constants.TOOLS.get("SH01");
        this.lastFacingDirection = AnimationState.STANDING_DOWN;

        this.animationController = new AnimationController(this, body, outfit, hair, hat, tool1, tool2);
        this.renderer = new Renderer();

        // Initialiser le CollisionComponent en utilisant les constantes
        float collisionWidth = Constants.PLAYER_COLLISION_WIDTH * scale;
        float collisionHeight = Constants.PLAYER_COLLISION_HEIGHT * scale;
        float offsetX = Constants.PLAYER_COLLISION_OFFSET_X * scale;
        float offsetY = Constants.PLAYER_COLLISION_OFFSET_Y * scale;

        collisionComponent = new CollisionComponent(x, y, collisionWidth, collisionHeight, offsetX, offsetY);

        this.currentState = new StandingState(this);
        currentState.enter();

        // Initialiser la map des cooldowns
        this.attackCooldowns = new HashMap<>();
    }

    public void update(float deltaTime, CollisionManager collisionManager) {
        handleInput();
        currentState.update(deltaTime, collisionManager);
        updateCooldowns(deltaTime);
        updateAnimationState();
        animationController.update(deltaTime);
    }

    public void render(Batch batch) {
        renderer.render(batch, body, outfit, hair, hat, tool1, tool2, x, y, scale);
    }

    public void changeState(State newState) {
        currentState.exit();
        currentState = newState;
        currentState.enter();
    }

    // Gestion des entrées
    private void handleInput() {
        if (!(currentState instanceof AttackingState)) {
            movingUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
            movingDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
            movingLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
            movingRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            running = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

            // Mise à jour de la vitesse
            speed = running ? Constants.PLAYER_RUNNING_SPEED : Constants.PLAYER_WALKING_SPEED;
        } else {
            // Empêcher le mouvement pendant l'attaque
            movingUp = movingDown = movingLeft = movingRight = running = false;
        }

        // Attaque de base (clic gauche)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!(currentState instanceof AttackingState)) {
                if (currentTool1 != null && !currentTool1.getAvailableAttacks().isEmpty()) {
                    String attackId = currentTool1.getAvailableAttacks().get(0); // pour simplifier
                    if (!isAttackOnCooldown(attackId)) {
                        Attack attack = Constants.ATTACKS.get(attackId);
                        if (attack != null) {
                            startAttack(attack, currentTool1);
                        }
                    } else {
                        // Attaque en cooldown, ne rien faire ou afficher un feedback
                        System.out.println("Attaque " + attackId + " en cooldown !");
                    }
                } else {
                    // Aucun outil équipé ou aucune attaque disponible
                }
            }
        }

        // Défense de base (clic droit)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (!(currentState instanceof AttackingState)) {
                if (currentTool2 != null && !currentTool2.getAvailableAttacks().isEmpty()) {
                    String attackId = currentTool2.getAvailableAttacks().get(0);
                    if (!isAttackOnCooldown(attackId)) {
                        Attack attack = Constants.ATTACKS.get(attackId);
                        if (attack != null) {
                            startAttack(attack, currentTool2);
                        }
                    } else {
                        // Attaque en cooldown, ne rien faire ou afficher un feedback
                        System.out.println("Attaque " + attackId + " en cooldown !");
                    }
                } else {
                    // Aucun outil équipé ou aucune attaque disponible
                }
            }
        }
    }

    private boolean isAttackOnCooldown(String attackId) {
        return attackCooldowns.containsKey(attackId);
    }

    private void startAttack(Attack attack, Tool tool) {
        // Démarrer l'attaque
        changeState(new AttackingState(this, attack, tool));
        // Ajouter l'attaque aux cooldowns
        attackCooldowns.put(attack.getId(), attack.getCooldown());
    }

    private void updateCooldowns(float deltaTime) {
        Iterator<Map.Entry<String, Float>> iterator = attackCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Float> entry = iterator.next();
            float remainingTime = entry.getValue() - deltaTime;
            if (remainingTime <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remainingTime);
            }
        }
    }

    public boolean isMoving() {
        return movingUp || movingDown || movingLeft || movingRight;
    }

    public boolean isRunning() {
        return running;
    }

    public void move(float deltaTime, CollisionManager collisionManager) {
        // Empêcher le mouvement pendant l'attaque
        if (currentState instanceof AttackingState) {
            return;
        }

        Vector2 direction = new Vector2(0, 0);

        if (movingUp) {
            direction.y += 1;
        }
        if (movingDown) {
            direction.y -= 1;
        }
        if (movingLeft) {
            direction.x -= 1;
        }
        if (movingRight) {
            direction.x += 1;
        }

        if (direction.len() > 0) {
            direction.nor(); // Normaliser le vecteur de direction
        }

        float deltaX = direction.x * speed * deltaTime;
        float deltaY = direction.y * speed * deltaTime;

        // Sauvegarder la position précédente
        float oldX = x;
        float oldY = y;

        // Tenter le déplacement en X
        x += deltaX;
        collisionComponent.setPosition(x, y);
        if (collisionManager.isColliding(collisionComponent.getBoundingBox())) {
            // Collision détectée, annuler le déplacement en X
            x = oldX;
            collisionComponent.setPosition(x, y);
        }

        // Tenter le déplacement en Y
        y += deltaY;
        collisionComponent.setPosition(x, y);
        if (collisionManager.isColliding(collisionComponent.getBoundingBox())) {
            // Collision détectée, annuler le déplacement en Y
            y = oldY;
            collisionComponent.setPosition(x, y);
        }
    }

    public void updateAnimationState() {
        if (currentState instanceof AttackingState) {
            // Ne rien faire pour éviter d'écraser l'animation personnalisée
            return;
        }

        if (isMoving()) {
            if (running) {
                // États d'animation pour la course
                if (movingLeft) {
                    animationController.setAnimationState(AnimationState.RUNNING_LEFT);
                    lastFacingDirection = AnimationState.RUNNING_LEFT;
                } else if (movingRight) {
                    animationController.setAnimationState(AnimationState.RUNNING_RIGHT);
                    lastFacingDirection = AnimationState.RUNNING_RIGHT;
                } else if (movingUp) {
                    animationController.setAnimationState(AnimationState.RUNNING_UP);
                    lastFacingDirection = AnimationState.RUNNING_UP;
                } else if (movingDown) {
                    animationController.setAnimationState(AnimationState.RUNNING_DOWN);
                    lastFacingDirection = AnimationState.RUNNING_DOWN;
                }
            } else {
                // États d'animation pour la marche
                if (movingLeft) {
                    animationController.setAnimationState(AnimationState.WALKING_LEFT);
                    lastFacingDirection = AnimationState.WALKING_LEFT;
                } else if (movingRight) {
                    animationController.setAnimationState(AnimationState.WALKING_RIGHT);
                    lastFacingDirection = AnimationState.WALKING_RIGHT;
                } else if (movingUp) {
                    animationController.setAnimationState(AnimationState.WALKING_UP);
                    lastFacingDirection = AnimationState.WALKING_UP;
                } else if (movingDown) {
                    animationController.setAnimationState(AnimationState.WALKING_DOWN);
                    lastFacingDirection = AnimationState.WALKING_DOWN;
                }
            }
        } else {
            // États d'animation pour le repos en fonction de la dernière direction
            switch (lastFacingDirection) {
                case WALKING_LEFT:
                case RUNNING_LEFT:
                case STANDING_LEFT:
                    animationController.setAnimationState(AnimationState.STANDING_LEFT);
                    lastFacingDirection = AnimationState.STANDING_LEFT;
                    break;
                case WALKING_RIGHT:
                case RUNNING_RIGHT:
                case STANDING_RIGHT:
                    animationController.setAnimationState(AnimationState.STANDING_RIGHT);
                    lastFacingDirection = AnimationState.STANDING_RIGHT;
                    break;
                case WALKING_UP:
                case RUNNING_UP:
                case STANDING_UP:
                    animationController.setAnimationState(AnimationState.STANDING_UP);
                    lastFacingDirection = AnimationState.STANDING_UP;
                    break;
                case WALKING_DOWN:
                case RUNNING_DOWN:
                case STANDING_DOWN:
                    animationController.setAnimationState(AnimationState.STANDING_DOWN);
                    lastFacingDirection = AnimationState.STANDING_DOWN;
                    break;
                default:
                    animationController.setAnimationState(AnimationState.STANDING_DOWN);
                    lastFacingDirection = AnimationState.STANDING_DOWN;
                    break;
            }
        }
    }

    // Getters et setters pour la position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Getter pour le CollisionComponent
    public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    // Méthode pour obtenir la boîte de collision du joueur
    public Rectangle getCollisionBounds() {
        return collisionComponent.getBoundingBox();
    }

    // Méthode pour obtenir la direction du joueur
    public Vector2 getDirection() {
        float dirX = 0;
        float dirY = 0;
        if (movingUp) dirY += 1;
        if (movingDown) dirY -= 1;
        if (movingRight) dirX += 1;
        if (movingLeft) dirX -= 1;

        Vector2 direction = new Vector2(dirX, dirY);
        if (direction.len() != 0) {
            direction.nor(); // Normaliser le vecteur
        }
        return direction;
    }

    public AnimationState getLastFacingDirection() {
        return lastFacingDirection;
    }

    public String getFacingDirection() {
        switch (lastFacingDirection) {
            case WALKING_UP:
            case RUNNING_UP:
            case STANDING_UP:
                return "UP";
            case WALKING_DOWN:
            case RUNNING_DOWN:
            case STANDING_DOWN:
                return "DOWN";
            case WALKING_LEFT:
            case RUNNING_LEFT:
            case STANDING_LEFT:
                return "LEFT";
            case WALKING_RIGHT:
            case RUNNING_RIGHT:
            case STANDING_RIGHT:
                return "RIGHT";
            default:
                return "DOWN";
        }
    }

    // Getters pour les outils
    public Tool getCurrentTool1() {
        return currentTool1;
    }

    public Tool getCurrentTool2() {
        return currentTool2;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public void dispose() {
        body.dispose();
        outfit.dispose();
        hair.dispose();
        hat.dispose();
    }
}
