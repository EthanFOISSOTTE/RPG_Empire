package com.empire.rpg.Component;

import com.badlogic.gdx.graphics.Texture;

/**
 *  Les records sont des classes immuables qui encapsulent principalement des données sans nécessiter beaucoup de code
 *  standardisé (comme les getters, equals, hashCode, ou toString).
 * Avantages d'un record :
 *          Moins de code boilerplate : Avec un record, Java génère automatiquement des méthodes courantes
 *                                      (equals, hashCode, toString) et des champs private final.
 *          Immutabilité : Les champs dans un record sont immuables par défaut, ce qui signifie qu'une fois assignées,
*                          eurs valeurs ne peuvent pas être modifiées.
*           Meilleure lisibilité : En un seul bloc, vous définissez les données que le record stocke, rendant le code
 *                                 plus lisible.
 *
* si TextureComponent est simplement un conteneur de données sans logique complexe, un record est un bon choix.
 */

public record TextureComponent(Texture texture, int width, int height) implements Component {

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {

    }

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
