package com.empire.rpg.Component;

/**
 * La classe CollisionComponent représente un composant qui gère la détection de collision
 * et l'état de collision pour une entité dans le jeu.
 */

public class CollisionComponent implements Component {
    private boolean isCollidable;

    /**
     * Constructeur d'un CollisionComponent avec un état de collision spécifié.
     *
     * @param isCollidable l'état de collision initial
     */

    public CollisionComponent(boolean isCollidable) {
        this.isCollidable = isCollidable;
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
     * Detecte une collision et affiche un message dans la console.
     *
     * @return true si une collision est détectée
     */

    public boolean detectCollision() {
        System.out.println("Collision detected");
        return true;
    }

    /**
     * Inverse l'état de collision du composant.
     *
     * @return le nouvel état de collision
     */

    public boolean toggleCollision() {
        isCollidable = !isCollidable;
        return isCollidable;
    }

    /**
     * Retourne le type du composant.
     *
     * @return le type du composant
     */

    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Affecte le type du composant.
     *
     * @param i le nouveau type du composant
     */

    @Override
    public void setCurrentHealthPoints(int i) {
        // No implementation needed
    }

    /**
     * Retourne les points de vie actuels du composant.
     *
     * @return les points de vie actuels
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
