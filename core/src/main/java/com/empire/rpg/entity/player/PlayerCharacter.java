package com.empire.rpg.entity.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.empire.rpg.component.Component;
import com.empire.rpg.entity.Player;
import com.empire.rpg.CollisionManager;
import com.empire.rpg.entity.player.animations.AnimationController;
import com.empire.rpg.entity.player.animations.AnimationState;
import com.empire.rpg.entity.player.components.*;
import com.empire.rpg.entity.player.rendering.Renderer;
import com.empire.rpg.entity.player.states.State;
import com.empire.rpg.entity.player.states.StandingState;
import com.empire.rpg.entity.player.states.AttackingState;
import com.empire.rpg.entity.player.utils.Constants;
import com.empire.rpg.entity.player.equipment.Tool;
import com.empire.rpg.entity.player.attacks.Attack;

import java.util.*;

/**
 * Classe principale représentant le joueur dans le jeu.
 * Elle gère l'état du joueur, les entrées utilisateur, les mouvements,
 * les attaques, l'équipement, les animations et les collisions.
 */
public class PlayerCharacter extends Player {
    // Contrôleur d'animations pour gérer les animations du joueur
    private AnimationController animationController;
    // Rendu du joueur
    private Renderer renderer;
    // Composants visuels du joueur
    private Body body;
    private Outfit outfit;
    private Hair hair;
    private Hat hat;
    private Tool1 tool1;
    private Tool2 tool2;
    // État actuel du joueur (debout, marchant, attaquant, etc.)
    private State currentState;
    // Position du joueur sur l'axe X et Y
    private float x;
    private float y;
    // Vitesse de déplacement du joueur
    private float speed;
    // Échelle du joueur pour le rendu
    private float scale;

    // Variables pour gérer les directions de mouvement
    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean running;
    private AnimationState lastFacingDirection;

    // Outils équipés par le joueur
    private Tool currentTool1; // Outil principal (main gauche)
    private Tool currentTool2; // Outil secondaire (main droite)

    // Listes des ensembles d'outils (pour changer d'équipement rapidement)
    private List<Tool[]> toolSets;
    private int currentToolSetIndex;

    // Gestion des temps de recharge des attaques
    private Map<String, Float> attackCooldowns;

    // Attributs pour gérer les combos d'attaques
    private int comboStep; // Étape actuelle du combo
    private float comboTimer; // Temps écoulé depuis la dernière attaque du combo
    private final float maxComboDelay = 0.5f; // Délai maximum entre les attaques d'un combo

    // File d'attente des attaques à exécuter
    private Queue<AttackData> attackQueue;

    // Classe interne pour stocker les données d'une attaque en attente
    private class AttackData {
        public Attack attack; // Attaque à exécuter
        public Tool tool;     // Outil utilisé pour l'attaque

        public AttackData(Attack attack, Tool tool) {
            this.attack = attack;
            this.tool = tool;
        }
    }

    // Composant de collision du joueur
    private CollisionComponent collisionComponent;

    // Constructeurs

    /**
     * Constructeur par défaut qui initialise le joueur à la position (0,0) avec l'échelle par défaut.
     */
    public PlayerCharacter() {
        this(0f, 0f, Constants.PLAYER_SCALE, UUID.randomUUID(), "PlayerCharacter", new HashMap<>());
    }

    /**
     * Constructeur principal qui initialise le joueur avec une position, une échelle, un identifiant unique,
     * un nom et une map de composants.
     *
     * @param x          Position X initiale.
     * @param y          Position Y initiale.
     * @param scale      Échelle du joueur.
     * @param id         Identifiant unique du joueur.
     * @param name       Nom du joueur.
     * @param components Map des composants associés.
     */
    public PlayerCharacter(float x, float y, float scale, UUID id, String name, Map<Class<? extends Component>, Component> components) {
        super(name, components, id); // Appel au constructeur de la superclasse

        // Initialisation des composants visuels
        this.body = new Body();
        this.outfit = new Outfit();
        this.hair = new Hair();
        this.hat = new Hat();
        this.tool1 = new Tool1();
        this.tool2 = new Tool2();

        // Initialisation des attributs de position et d'échelle
        this.x = x;
        this.y = y;
        this.scale = scale;
        // Vitesse de marche par défaut
        this.speed = Constants.PLAYER_WALKING_SPEED;
        // Direction par défaut du joueur
        this.lastFacingDirection = AnimationState.STANDING_DOWN;

        // Initialisation du contrôleur d'animation et du renderer
        this.animationController = new AnimationController(this, body, outfit, hair, hat, tool1, tool2);
        this.renderer = new Renderer();

        // Initialisation du composant de collision en utilisant les constantes
        float collisionWidth = Constants.PLAYER_COLLISION_WIDTH * scale;
        float collisionHeight = Constants.PLAYER_COLLISION_HEIGHT * scale;
        float offsetX = Constants.PLAYER_COLLISION_OFFSET_X * scale;
        float offsetY = Constants.PLAYER_COLLISION_OFFSET_Y * scale;

        collisionComponent = new CollisionComponent(x, y, collisionWidth, collisionHeight, offsetX, offsetY);

        // État initial du joueur (debout)
        this.currentState = new StandingState(this);
        currentState.enter();

        // Initialisation des temps de recharge des attaques
        this.attackCooldowns = new HashMap<>();

        // Initialisation des attributs de combo
        this.comboStep = 0;
        this.comboTimer = 0f;

        // Initialisation de la file d'attente des attaques
        this.attackQueue = new LinkedList<>();

        // Initialisation des ensembles d'outils
        initializeToolSets();
    }

    // Méthode pour initialiser les ensembles d'outils
    private void initializeToolSets() {
        toolSets = new ArrayList<>();

        // Ensemble 1 : Deux outils équipés (épée et bouclier)
        Tool[] set1 = {
            Constants.TOOLS.get("SW01"), // Outil principal (épée)
            Constants.TOOLS.get("SH01")  // Outil secondaire (bouclier)
        };
        toolSets.add(set1);

        // Ensemble 2 : Un seul outil équipé (lance)
        Tool[] set2 = {
            Constants.TOOLS.get("HB01"), // Outil principal (lance)
            null                         // Pas d'outil secondaire
        };
        toolSets.add(set2);

        // Ensemble 3 : Arc et carquois
        Tool[] set3 = {
            Constants.TOOLS.get("BO02"), // Outil principal (arc)
            Constants.TOOLS.get("QV01")  // Outil secondaire (carquois)
        };
        toolSets.add(set3);

        // Index de l'ensemble d'outils actuel (commence par le premier ensemble)
        currentToolSetIndex = 0;

        // Équiper le premier ensemble par défaut
        equipToolSet(currentToolSetIndex);
    }

    // Méthode de mise à jour appelée à chaque frame
    public void update(float deltaTime, CollisionManager collisionManager) {
        handleInput(); // Gérer les entrées utilisateur
        currentState.update(deltaTime, collisionManager); // Mettre à jour l'état actuel
        updateCooldowns(deltaTime); // Mettre à jour les temps de recharge des attaques
        updateComboTimer(deltaTime); // Mettre à jour le timer du combo
        updateAnimationState(); // Mettre à jour l'état d'animation
        animationController.update(deltaTime); // Mettre à jour le contrôleur d'animation
    }

    // Méthode pour rendre le joueur à l'écran
    public void render(Batch batch) {
        renderer.render(batch, body, outfit, hair, hat, tool1, tool2, x, y, scale);
    }

    // Méthode pour changer l'état du joueur
    public void changeState(State newState) {
        currentState.exit(); // Quitter l'état actuel
        currentState = newState; // Définir le nouvel état
        currentState.enter(); // Entrer dans le nouvel état
    }

    // Getter pour obtenir l'état actuel du joueur
    public State getCurrentState() {
        return currentState;
    }

    // Gestion des entrées utilisateur
    private void handleInput() {
        // Si le joueur n'est pas en train d'attaquer
        if (!(currentState instanceof AttackingState)) {
            // Gérer les touches pour le mouvement
            movingUp = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
            movingDown = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
            movingLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
            movingRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            running = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

            // Mettre à jour la vitesse en fonction de si le joueur court ou marche
            speed = running ? Constants.PLAYER_RUNNING_SPEED : Constants.PLAYER_WALKING_SPEED;
        } else {
            // Empêcher le mouvement pendant une attaque
            movingUp = movingDown = movingLeft = movingRight = running = false;
        }

        // Changer d'ensemble d'outils avec les touches 1, 2, 3
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            switchToolSet(0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            switchToolSet(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            switchToolSet(2);
        }

        // Attaque de base (clic gauche)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (currentTool1 != null) {
                queueComboAttack(); // Ajouter une attaque au combo
            } else {
                // Pas d'outil équipé dans la main gauche
                System.out.println("Aucune arme équipée dans la main gauche !");
            }
        }

        // Défense de base (clic droit)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (!(currentState instanceof AttackingState) && currentTool2 != null) {
                if (!currentTool2.getAvailableAttacks().isEmpty()) {
                    // Récupérer l'ID de l'attaque disponible avec l'outil secondaire
                    String attackId = currentTool2.getAvailableAttacks().get(0);
                    if (!isAttackOnCooldown(attackId)) {
                        Attack attack = Constants.ATTACKS.get(attackId);
                        if (attack != null) {
                            startAttack(attack, currentTool2); // Démarrer l'attaque
                            attackCooldowns.put(attack.getId(), attack.getCooldown()); // Ajouter le cooldown
                        }
                    } else {
                        // L'attaque est en cooldown
                        System.out.println("Attaque " + attackId + " en recharge !");
                    }
                } else {
                    // Pas d'attaques disponibles pour l'outil secondaire
                    System.out.println("Aucune attaque disponible pour l'outil équipé dans la main droite !");
                }
            } else {
                // Pas d'outil équipé dans la main droite ou déjà en train d'attaquer
                if (currentTool2 == null) {
                    System.out.println("Aucune arme équipée dans la main droite !");
                }
            }
        }
    }

    // Méthode pour changer l'ensemble d'outils équipé
    private void switchToolSet(int toolSetIndex) {
        if (toolSetIndex >= 0 && toolSetIndex < toolSets.size()) {
            currentToolSetIndex = toolSetIndex;
            equipToolSet(currentToolSetIndex);
            resetCombo(); // Réinitialiser le combo

            // Interrompre l'attaque en cours si nécessaire
            if (currentState instanceof AttackingState) {
                currentState.exit();
                changeState(new StandingState(this));
            }
        }
    }

    // Méthode pour équiper un ensemble d'outils
    private void equipToolSet(int toolSetIndex) {
        Tool[] selectedSet = toolSets.get(toolSetIndex);
        currentTool1 = selectedSet[0];
        currentTool2 = selectedSet[1];
    }

    // Méthode pour ajouter une attaque au combo
    private void queueComboAttack() {
        if (currentTool1 != null && !currentTool1.getAvailableAttacks().isEmpty()) {
            String attackId = getNextAttackId(); // Obtenir l'ID de la prochaine attaque du combo
            if (!isAttackOnCooldown(attackId)) {
                Attack attack = Constants.ATTACKS.get(attackId);
                if (attack != null) {
                    attackQueue.add(new AttackData(attack, currentTool1)); // Ajouter l'attaque à la file d'attente
                    attackCooldowns.put(attack.getId(), attack.getCooldown()); // Ajouter le cooldown
                    comboStep++; // Passer à l'étape suivante du combo
                    comboTimer = 0f; // Réinitialiser le timer du combo

                    // Si le joueur n'est pas déjà en train d'attaquer, démarrer l'attaque
                    if (!(currentState instanceof AttackingState)) {
                        startNextAttack();
                    }
                }
            } else {
                // L'attaque est en cooldown
                System.out.println("Attaque " + attackId + " en recharge !");
                resetCombo();
            }
        } else {
            // Pas d'outil équipé ou pas d'attaques disponibles
            System.out.println("Aucune attaque disponible pour l'outil équipé dans la main gauche !");
        }
    }

    // Méthode pour obtenir l'ID de la prochaine attaque dans le combo
    private String getNextAttackId() {
        if (currentTool1 != null && comboStep < currentTool1.getAvailableAttacks().size()) {
            return currentTool1.getAvailableAttacks().get(comboStep);
        } else {
            // Si toutes les attaques du combo ont été utilisées, réinitialiser le combo
            resetCombo();
            if (currentTool1 != null && !currentTool1.getAvailableAttacks().isEmpty()) {
                return currentTool1.getAvailableAttacks().get(0);
            } else {
                return null;
            }
        }
    }

    // Vérifie si une attaque est en cooldown
    private boolean isAttackOnCooldown(String attackId) {
        return attackCooldowns.containsKey(attackId);
    }

    // Démarrer une attaque avec un outil donné
    private void startAttack(Attack attack, Tool tool) {
        if (tool != null) {
            changeState(new AttackingState(this, attack, tool));
        } else {
            // Si l'outil est null, afficher un message d'erreur
            System.out.println("Impossible de démarrer l'attaque sans outil !");
        }
    }

    // Démarrer la prochaine attaque dans la file d'attente
    public void startNextAttack() {
        if (!attackQueue.isEmpty()) {
            AttackData nextAttack = attackQueue.poll();
            startAttack(nextAttack.attack, nextAttack.tool);
        } else {
            // Plus d'attaques en attente, réinitialiser le combo
            resetCombo();
            changeState(new StandingState(this));
        }
    }

    // Mettre à jour les temps de recharge des attaques
    private void updateCooldowns(float deltaTime) {
        Iterator<Map.Entry<String, Float>> iterator = attackCooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Float> entry = iterator.next();
            float remainingTime = entry.getValue() - deltaTime;
            if (remainingTime <= 0) {
                iterator.remove(); // Retirer l'attaque du cooldown
            } else {
                entry.setValue(remainingTime); // Mettre à jour le temps restant
            }
        }
    }

    // Mettre à jour le timer du combo
    private void updateComboTimer(float deltaTime) {
        if (comboStep > 0 && !(currentState instanceof AttackingState)) {
            comboTimer += deltaTime;
            if (comboTimer > maxComboDelay) {
                resetCombo(); // Réinitialiser le combo si le délai est dépassé
            }
        }
    }

    // Réinitialiser les attributs du combo
    public void resetCombo() {
        comboStep = 0;
        comboTimer = 0f;
        attackQueue.clear();
    }

    // Getter pour l'étape actuelle du combo
    public int getComboStep() {
        return comboStep;
    }

    // Getter pour la file d'attente des attaques
    public Queue<AttackData> getAttackQueue() {
        return attackQueue;
    }

    // Vérifie si le joueur est en mouvement
    public boolean isMoving() {
        return movingUp || movingDown || movingLeft || movingRight;
    }

    // Vérifie si le joueur court
    public boolean isRunning() {
        return running;
    }

    // Méthode pour déplacer le joueur en fonction des entrées utilisateur
    public void move(float deltaTime, CollisionManager collisionManager) {
        // Empêcher le mouvement pendant une attaque
        if (currentState instanceof AttackingState) {
            return;
        }

        Vector2 direction = new Vector2(0, 0);

        // Déterminer la direction du mouvement
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
            direction.nor(); // Normaliser le vecteur directionnel
        }

        // Calculer le déplacement
        float deltaX = direction.x * speed * deltaTime;
        float deltaY = direction.y * speed * deltaTime;

        // Sauvegarder la position précédente
        float oldX = x;
        float oldY = y;

        // Tentative de déplacement en X
        x += deltaX;
        collisionComponent.setPosition(x, y);
        if (collisionManager.isColliding(collisionComponent.getBoundingBox())) {
            // Collision détectée, annuler le déplacement en X
            x = oldX;
            collisionComponent.setPosition(x, y);
        }

        // Tentative de déplacement en Y
        y += deltaY;
        collisionComponent.setPosition(x, y);
        if (collisionManager.isColliding(collisionComponent.getBoundingBox())) {
            // Collision détectée, annuler le déplacement en Y
            y = oldY;
            collisionComponent.setPosition(x, y);
        }
    }

    // Mettre à jour l'état d'animation en fonction de l'état du joueur
    public void updateAnimationState() {
        // Ne pas changer l'animation si le joueur est en train d'attaquer
        if (currentState instanceof AttackingState) {
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
            // États d'animation de repos basés sur la dernière direction regardée
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

    // Getters et setters pour la position du joueur
    public float getX() { return x; }
    public float getY() { return y; }

    // Getter pour le composant de collision
    public CollisionComponent getCollisionComponent() {
        return collisionComponent;
    }

    // Méthode pour obtenir les limites de collision du joueur
    public Rectangle getCollisionBounds() {
        return collisionComponent.getBoundingBox();
    }

    // Méthode pour obtenir la direction du joueur sous forme de vecteur
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

    // Getter pour la dernière direction regardée par le joueur
    public AnimationState getLastFacingDirection() {
        return lastFacingDirection;
    }

    // Méthode pour obtenir la direction sous forme de chaîne ("UP", "DOWN", "LEFT", "RIGHT")
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

    // Getters pour les outils équipés
    public Tool getCurrentTool1() {
        return currentTool1;
    }
    public Tool getCurrentTool2() {
        return currentTool2;
    }

    // Getter pour le contrôleur d'animation
    public AnimationController getAnimationController() {
        return animationController;
    }

    // Méthode pour libérer les ressources associées aux composants
    public void dispose() {
        body.dispose();
        outfit.dispose();
        hair.dispose();
        hat.dispose();
        // Les autres composants (tool1, tool2) n'ont pas de ressources à libérer
    }
}