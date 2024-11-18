package com.empire.rpg.player;

// Importations
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.List;

// Classe Player
public class Player {
    // Position du joueur
    private Vector2 position;
    private final float walkSpeed = 150f;
    private final float runSpeed = 300f;
    private Texture playerTexture;

    // Taille du joueur (1.0 est la taille normale)
    private float size = 2.0f;

    // Définition des marges pour la collision
    private final float marginLeft = 15f;
    private final float marginRight = 15f;
    private final float marginTop = 40f;
    private final float marginBottom = 5f;

    // Champs d'animation
    private Animation<TextureRegion> walkFaceAnimation, walkBackAnimation, walkLeftAnimation, walkRightAnimation;
    private Animation<TextureRegion> runFaceAnimation, runBackAnimation, runLeftAnimation, runRightAnimation;
    private Animation<TextureRegion> standFaceAnimation, standBackAnimation, standLeftAnimation, standRightAnimation;
    private Animation<TextureRegion> currentAnimation;

    // Temps d'animation
    private float stateTime;
    private String direction = "stand_face";

    // Dépendances
    private final CollisionManager collisionManager;
    private final SpriteSheet spriteSheet;

    // Constructeur
    public Player(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.position = new Vector2(51 * 48 + 24, 45 * 48 + 24); // Position Tableau des quêtes du joueur
        this.spriteSheet = new SpriteSheet();
        loadAnimations();
        currentAnimation = standFaceAnimation;
    }

    // Obtenir la position actuelle du joueur
    public Vector2 getPosition() {
        return position;
    }

    // Définir la taille du joueur
    public void setSize(float size) {
        this.size = size;
    }

    // Obtenir la taille actuelle du joueur
    public float getSize() {
        return size;
    }

    // Initialisation des animations
    private void loadAnimations() {
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        TextureRegion[][] tmp = TextureRegion.split(playerTexture, 64, 64);

        // Initialisation des animations de stand
        standFaceAnimation = createAnimation(tmp, List.of(new FrameConfig(0, false, 1.0f)));
        standBackAnimation = createAnimation(tmp, List.of(new FrameConfig(16, false, 1.0f)));
        standRightAnimation = createAnimation(tmp, List.of(new FrameConfig(32, false, 1.0f)));
        standLeftAnimation = createAnimation(tmp, List.of(new FrameConfig(32, true, 1.0f)));

        // Initialisation des animations de marche
        walkFaceAnimation = createAnimation(tmp, List.of(
            new FrameConfig(48, false, 0.135f), new FrameConfig(49, false, 0.135f), new FrameConfig(50, false, 0.135f),
            new FrameConfig(48, true, 0.135f), new FrameConfig(49, true, 0.135f), new FrameConfig(50, true, 0.135f)
        ));
        walkBackAnimation = createAnimation(tmp, List.of(
            new FrameConfig(52, false, 0.135f), new FrameConfig(53, false, 0.135f), new FrameConfig(54, false, 0.135f),
            new FrameConfig(52, true, 0.135f), new FrameConfig(53, true, 0.135f), new FrameConfig(54, true, 0.135f)
        ));
        walkRightAnimation = createAnimation(tmp, List.of(
            new FrameConfig(64, false, 0.135f),new FrameConfig(65, false, 0.135f),new FrameConfig(66, false, 0.135f),
            new FrameConfig(67, false, 0.135f), new FrameConfig(68, false, 0.135f), new FrameConfig(69, false, 0.135f)
        ));
        walkLeftAnimation = createAnimation(tmp, List.of(
            new FrameConfig(64, true, 0.135f), new FrameConfig(65, true, 0.135f), new FrameConfig(66, true, 0.135f),
            new FrameConfig(67, true, 0.135f), new FrameConfig(68, true, 0.135f), new FrameConfig(69, true, 0.135f)
        ));

        // Initialisation des animations de course
        runFaceAnimation = createAnimation(tmp, List.of(
            new FrameConfig(48, false, 0.080f), new FrameConfig(49, false, 0.055f), new FrameConfig(51, false, 0.115f),
            new FrameConfig(48, true, 0.080f), new FrameConfig(49, true, 0.055f), new FrameConfig(51, true, 0.115f)
        ));
        runBackAnimation = createAnimation(tmp, List.of(
            new FrameConfig(52, false, 0.080f), new FrameConfig(53, false, 0.055f), new FrameConfig(55, false, 0.115f),
            new FrameConfig(52, true, 0.080f), new FrameConfig(53, true, 0.055f), new FrameConfig(55, true, 0.115f)
        ));
        runRightAnimation = createAnimation(tmp, List.of(
            new FrameConfig(64, false, 0.080f), new FrameConfig(65, false, 0.055f), new FrameConfig(70, false, 0.115f),
            new FrameConfig(67, false, 0.080f), new FrameConfig(68, false, 0.055f), new FrameConfig(71, false, 0.115f)
        ));
        runLeftAnimation = createAnimation(tmp, List.of(
            new FrameConfig(64, true, 0.080f), new FrameConfig(65, true, 0.055f), new FrameConfig(70, true, 0.115f),
            new FrameConfig(67, true, 0.080f), new FrameConfig(68, true, 0.055f), new FrameConfig(71, true, 0.115f)
        ));
    }

    // Créer une animation à partir d'une configuration de frame
    private Animation<TextureRegion> createAnimation(TextureRegion[][] tmp, List<FrameConfig> frameConfigs) {
        Array<TextureRegion> frames = new Array<>();
        for (FrameConfig config : frameConfigs) {
            TextureRegion frame = new TextureRegion(spriteSheet.getTextureRegionById(tmp, config.id));
            if (config.flip) {
                frame.flip(true, false);
            }
            frames.add(frame);
        }
        return new Animation<>(frameConfigs.get(0).duration, frames, Animation.PlayMode.LOOP);
    }

    // Configuration de frame
    public void update(float deltaTime) {
        // Sauvegarde de la position actuelle
        Vector2 oldPosition = new Vector2(position);
        boolean moving = false;

        // Vitesse du joueur
        float speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? runSpeed : walkSpeed;
        float moveX = 0, moveY = 0;

        // Gestion de l'input et du mouvement
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveY = 1;
            direction = "walk_back";
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveY = -1;
            direction = "walk_face";
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveX = -1;
            direction = "walk_left";
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveX = 1;
            direction = "walk_right";
            moving = true;
        }

        // Normalisation du vecteur de déplacement
        if (moveX != 0 && moveY != 0) {
            moveX *= 0.7071f;
            moveY *= 0.7071f;
        }

        // Mise à jour de la position du joueur
        position.x += moveX * speed * deltaTime;
        position.y += moveY * speed * deltaTime;

        // Vérification de la collision (rectangle de collision du joueur ajusté avec les marges
        Rectangle playerBounds = new Rectangle(
            position.x - 32 + marginLeft,
            position.y - 32 + marginBottom,
            64 - (marginLeft + marginRight),
            64 - (marginTop + marginBottom)
        );

        // Vérification de la collision et ajustement du mouvement
        if (collisionManager.isColliding(playerBounds)) {
            // Si collision, revenir à la position précédente
            position.set(oldPosition);

            // Tester le mouvement sur l'axe X uniquement
            position.x += moveX * speed * deltaTime;
            playerBounds.setPosition(position.x - 32 + marginLeft, position.y - 32 + marginBottom);
            if (collisionManager.isColliding(playerBounds)) {
                // Si toujours en collision sur l'axe X, annuler le mouvement X
                position.x = oldPosition.x;
            }

            // Tester le mouvement sur l'axe Y uniquement
            position.y += moveY * speed * deltaTime;
            playerBounds.setPosition(position.x - 32 + marginLeft, position.y - 32 + marginBottom);
            if (collisionManager.isColliding(playerBounds)) {
                // Si toujours en collision sur l'axe Y, annuler le mouvement Y
                position.y = oldPosition.y;
            }
        }

        // Mise à jour de l'animation actuelle
        if (!moving) {
            switch (direction) {
                case "walk_face": currentAnimation = standFaceAnimation; break;
                case "walk_back": currentAnimation = standBackAnimation; break;
                case "walk_left": currentAnimation = standLeftAnimation; break;
                case "walk_right": currentAnimation = standRightAnimation; break;
            }
        } else {
            boolean isRunning = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
            switch (direction) {
                case "walk_face":
                    currentAnimation = isRunning ? runFaceAnimation : walkFaceAnimation;
                    break;
                case "walk_back":
                    currentAnimation = isRunning ? runBackAnimation : walkBackAnimation;
                    break;
                case "walk_left":
                    currentAnimation = isRunning ? runLeftAnimation : walkLeftAnimation;
                    break;
                case "walk_right":
                    currentAnimation = isRunning ? runRightAnimation : walkRightAnimation;
                    break;
            }
        }
        // Mise à jour du temps d'animation
        stateTime += deltaTime;
    }

    // Rendre le joueur en utilisant la frame actuel de l'animation active et en fonction de la taille
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        float width = 64 * size;
        float height = 64 * size;
        batch.draw(currentFrame, position.x - width / 2, position.y - height / 2, width, height);
    }

    // Libère la mémoire utilisée par la texture du joueur
    public void dispose() {
        playerTexture.dispose();
    }
}
