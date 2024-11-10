package com.empire.rpg.System;

import com.empire.rpg.Entity.Item;
import java.util.HashMap;
import java.util.Map;

public class InventorySystem {

    private final Map<Item, Integer> inventory;

    public InventorySystem() {
        inventory = new HashMap<>();
    }

    // Ajoute un item à l'inventaire
    public void addItem(Item item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
        System.out.println("Ajouté " + quantity + " de " + item.getName() + " à l'inventaire.");
    }

    // Retire un item de l'inventaire
    public void removeItem(Item item, int quantity) {
        if (inventory.containsKey(item)) {
            int currentQuantity = inventory.get(item);
            if (currentQuantity > quantity) {
                inventory.put(item, currentQuantity - quantity);
                System.out.println("Retiré " + quantity + " de " + item.getName() + " de l'inventaire.");
            } else {
                inventory.remove(item);
                System.out.println("Retiré " + item.getName() + " de l'inventaire.");
            }
        } else {
            System.out.println(item.getName() + " n'est pas présent dans l'inventaire.");
        }
    }

    // Vérifie si un item est dans l'inventaire
    public boolean hasItem(Item item) {
        return inventory.containsKey(item) && inventory.get(item) > 0;
    }

    // Affiche le contenu de l'inventaire
    public void displayInventory() {
        System.out.println("Contenu de l'inventaire :");
        if (inventory.isEmpty()) {
            System.out.println("Inventaire vide.");
        } else {
            for (Map.Entry<Item, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey().getName() + " x" + entry.getValue());
            }
        }
    }
}
