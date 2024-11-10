package com.empire.rpg.System;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.Component.PositionComponent;
import com.empire.rpg.Component.TextureComponent;
import com.empire.rpg.Entity.Entity;
import java.util.List;

/**
 * Système de Rendu (RenderSystem)
 * Bien que moins directement lié aux interactions, un système de rendu est nécessaire pour afficher graphiquement les
 * entités et leurs états. Ce système s'intègre avec une bibliothèque graphique comme LibGDX.
 */

public class RenderSystem {
    private final Batch batch;

    public RenderSystem() {
        this.batch = new SpriteBatch();
    }

    public void render(List<Entity> entities) {
        // Démarrer le batch pour dessiner
        batch.begin();

        // Parcourir toutes les entités à dessiner
        for (Entity entity : entities) {
            PositionComponent position = (PositionComponent) entity.getComponent(PositionComponent.class);
            TextureComponent texture = (TextureComponent) entity.getComponent(TextureComponent.class);

            // Vérifier si les composants sont présents
            if (position != null && texture != null) {
                batch.draw(
                    texture.texture(),               // Texture de l'entité
                    position.getX(), position.getY(),   // Position de l'entité
                    texture.width(), texture.height() // Dimensions de l'entité
                );
            }
        }

        // Terminer le batch
        batch.end();
    }

    // Libérer les ressources du batch
    public void dispose() {
        batch.dispose();
    }
}
