package com.empire.rpg.entity;

import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.PositionComponent;

import java.util.Map;
import java.util.UUID;

/**
 * Represente un item dans le jeu.
 */
public class Item extends Entity {
    private String name;
    private String type;
    private int quantity;
    private String description;
    private int valeur;
    private boolean states;
    private String style;


    /**
     * Constructeur d'un item avec un nom, une quantité et des types.
     *
     * @param name     Le nom de l'item
     * @param quantity La quantité de l'item
     * @param type    La categorie d'item
     */
    public Item(String name, int quantity, String type, String description, int valeur,boolean states, String style) {
        super(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            CollisionComponent.class, new CollisionComponent(true)
        ),
            UUID.randomUUID()
        );

        this.name = name;
        this.type= type;
        this.quantity = quantity;
        this.description = description;
        this.valeur = valeur;
        this.states = states;
        this.style = style;

    }

    /**
     * Recuperer le nom de l'item.
     *
     * @return Le nom de l'item
     */
    public String getName() {
        return name;
    }

    /**
     * Affecter un nouveau nom à l'item.
     *
     * @param name le nouveau nom de l'item.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Recuperer les categories d'item.
     *
     * @return les categories de l'item
     */
    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
    public int getValeur() {
        return valeur;
    }
    public boolean getStates(){
        return states;
    }

    public String getStyle() {
        return style;
    }


    // Setters pour chaque attribut
    public void setStates(boolean states) {
        this.states = states;
    }

    /**
     * Affecter de nouvelles categories à l'item.
     *
     * @param type les nouvelles categories de l'item.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Recuperer la quantité de l'item.
     *
     * @return La quantité de l'item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Affecter une nouvelle quantité à l'item.
     *
     * @param quantity La nouvelle quantité de l'item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Ajouter une entité item dans le jeu.
     *
     * @return null (not implemented)
     */
    @Override
    public Entity addEntity() {
        return null;
    }

    /**
     * Supprimer un item dans le jeu par son nom.
     *
     * @param name le nom de l'item à supprimer
     * @return null (not implemented)
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }
}
