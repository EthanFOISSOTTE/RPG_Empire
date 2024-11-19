package com.empire.rpg.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * La classe CollisionComponent représente un composant qui gère la détection de collision
 * et la bounding box pour une entité dans le jeu.
 */
public class CollisionComponent implements Component {
    private boolean isCollidable;
    private Rectangle boundingBox;

    /**
     * Constructeur d'un CollisionComponent avec une BoundingBox.
     *
     * @param isCollidable l'état de collision initial
     * @param boundingBox  la bounding box de l'entité
     */
    public CollisionComponent(boolean isCollidable, Rectangle boundingBox) {
        this.isCollidable = isCollidable;
        this.boundingBox = boundingBox;
    }

    /**
     * Retourne si le composant est collidable.
     *
     * @return true si le composant est collidable, false sinon
     */
    public boolean isCollidable() {
        return isCollidable;
    }

    /**
     * Affecte l'état de collision du composant.
     *
     * @param collidable le nouvel état de collision
     */
    public void setCollidable(boolean collidable) {
        isCollidable = collidable;
    }

    /**
     * Retourne la bounding box de l'entité.
     *
     * @return la bounding box
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Définit une nouvelle bounding box.
     *
     * @param boundingBox la nouvelle bounding box
     */
    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {
        // No implementation needed
    }

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
