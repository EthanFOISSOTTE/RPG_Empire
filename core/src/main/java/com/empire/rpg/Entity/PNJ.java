package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.Component;
import com.empire.rpg.Component.MovementComponent;
import com.empire.rpg.Component.PositionComponent;

import java.util.Map;
import java.util.UUID;

/**
 * PNJ (Personnage Non Joueur) est une classe représentant un personnage non joueur dans le jeu.
 */
public class PNJ extends Entity {
    private String dialog;
    private int progress;

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
        ), UUID.randomUUID(), "Hello", 0);
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
     * @param dialog le dialogue du PNJ
     * @param progress le progrès du PNJ
     */
    public PNJ(String name, Map<Class<? extends Component>, Component> components, UUID id, String dialog, int progress) {
        super(name, components, id);
        this.dialog = dialog;
        this.progress = progress;
    }

    /**
     * Obtient le dialogue du PNJ.
     *
     * @return le dialogue du PNJ
     */
    public String getDialog() {
        return dialog;
    }

    /**
     * Définit le dialogue du PNJ.
     *
     * @param dialog le nouveau dialogue du PNJ
     */
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    /**
     * Interagit avec un joueur.
     *
     * @param player le joueur avec lequel interagir
     */
    public void interact(Player player) {
        System.out.println(this.dialog);
    }

    /**
     * Met à jour le progrès du PNJ.
     */
    public void updateProgress() {
        this.progress++;
    }

    /**
     * Crée une entité avec un identifiant donné.
     *
     * @param id l'identifiant unique de l'entité
     */
    @Override
    public void createEntity(UUID id) {

    }
}
