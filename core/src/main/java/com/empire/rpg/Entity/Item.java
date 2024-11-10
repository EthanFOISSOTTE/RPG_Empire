package com.empire.rpg.Entity;

import com.empire.rpg.Component.CollisionComponent;
import com.empire.rpg.Component.InventoryComponent;
import com.empire.rpg.Component.PositionComponent;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represente un item dans le jeu.
 */
public class Item extends Entity {
    private String name;
    private List<String> types;
    private int quantity;

    /**
     * Constructeur d'un item avec un nom, une quantité et des types.
     *
     * @param name     Le nom de l'item
     * @param quantity La quantité de l'item
     * @param types    Les categories d'item
     */
    public Item(String name, int quantity, List<String> types) {
        super(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            CollisionComponent.class, new CollisionComponent(true),
            InventoryComponent.class, new InventoryComponent(0, 100)
        ),
            UUID.randomUUID()
        );

        this.name = name;
        this.types = List.of("weapon", "armor", "potion");
        this.quantity = quantity;
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
    public List<String> getTypes() {
        return types;
    }

    /**
     * Affecter de nouvelles categories à l'item.
     *
     * @param types les nouvelles categories de l'item.
     */
    public void setTypes(List<String> types) {
        this.types = types;
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
