package com.empire.rpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.TextureComponent;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.Component;
import com.empire.rpg.component.MovementComponent;
import com.empire.rpg.component.PositionComponent;
import java.util.Map;
import java.util.UUID;

/**
 * PNJ (Personnage Non Joueur) est une classe représentant un personnage non joueur dans le jeu.
 */
public class PNJ extends Entity {

    /**
     * Ajoute une nouvelle entité PNJ.
     *
     * @return une nouvelle instance de PNJ
     */
    @Override
    public PNJ addEntity() {
        return new PNJ(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            CollisionComponent.class, new CollisionComponent(true)
        ), UUID.randomUUID());
    }

    /**
     * Supprime une entité par son nom.
     *
     * @param name le nom de l'entité à supprimer
     * @return l'entité supprimée ou null si non trouvée
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    /**
     * Constructeur de la classe PNJ.
     *
     * @param name le nom du PNJ
     * @param components les composants associés au PNJ
     * @param id l'identifiant unique du PNJ
     */
    public PNJ(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Crée une entité avec un identifiant donné.
     *
     * @param id l'identifiant unique de l'entité
     */
    @Override
    public void createEntity(UUID id) {

    }

    public void render(SpriteBatch batch) {
        TextureComponent textureComponent = (TextureComponent) components.get(TextureComponent.class);
        PositionComponent positionComponent = (PositionComponent) components.get(PositionComponent.class);

        if (textureComponent != null && positionComponent != null) {
            float scale = textureComponent.getScale();

            batch.draw(
                textureComponent.getCurrentFrame(),
                positionComponent.getX(),
                positionComponent.getY(),
                textureComponent.getCurrentFrame().getRegionWidth() * scale,  // Largeur avec échelle
                textureComponent.getCurrentFrame().getRegionHeight() * scale  // Hauteur avec échelle
            );
        }
    }


}
