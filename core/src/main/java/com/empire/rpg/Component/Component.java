package com.empire.rpg.Component;

/**
 * Interface qui represente un composant.
 */
public interface Component {

    /**
     * Affecte le nombre de points de dégâts réduit par le composant.
     *
     * @return les dégâts réduits par le composant
        */

    int getDamageReduction();

    /**
     * Affecte le nombre de points de dégâts réduit par le composant.
     *
     * @param i les dégâts réduits par le composant
     */

    void setCurrentHealthPoints(int i);

    /**
     * Retourne les points de vie actuels du composant.
     *
     * @return les points de vie actuels
     */
    int getCurrentHealthPoints();
}
