package com.empire.rpg.component;

import com.badlogic.gdx.graphics.Texture;

/**
 * Composant de texture pour un jeu RPG.
 */
public class TextureComponent implements Component {
    private Texture texture;

    /**
     * Constructeur pour initialiser la texture.
     *
     * @param texture la texture à utiliser
     */
    public TextureComponent(Texture texture) {
        this.texture = texture;
    }

    /**
     * Obtient la texture.
     *
     * @return la texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return la réduction des dégâts (toujours 0)
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i les points de vie actuels
     */
    @Override
    public void setCurrentHealthPoints(int i) {

    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return les points de vie actuels (toujours 0)
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
