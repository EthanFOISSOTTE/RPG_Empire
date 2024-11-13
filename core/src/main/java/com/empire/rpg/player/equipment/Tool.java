package com.empire.rpg.player.equipment;

import java.util.List;

public class Tool {
    private String id; // ex : "SW01"
    private String name; // ex : "Épée de base"
    private String categoryKey; // ex : "ONE3"
    private String spritesheetKey; // ex : "SW01"
    private List<String> availableAttacks; // Liste des IDs d'attaques disponibles avec cet outil

    // Constructeur
    public Tool(String id, String name, String categoryKey, String spritesheetKey, List<String> availableAttacks) {
        this.id = id;
        this.name = name;
        this.categoryKey = categoryKey;
        this.spritesheetKey = spritesheetKey;
        this.availableAttacks = availableAttacks;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategoryKey() { return categoryKey; }
    public String getSpritesheetKey() { return spritesheetKey; }
    public List<String> getAvailableAttacks() { return availableAttacks;}

    // Setters
    public void setId(String id) { this.id = id;}
    public void setName(String name) { this.name = name; }
    public void setCategoryKey(String categoryKey) { this.categoryKey = categoryKey; }
    public void setSpritesheetKey(String spritesheetKey) { this.spritesheetKey = spritesheetKey; }
    public void setAvailableAttacks(List<String> availableAttacks) { this.availableAttacks = availableAttacks; }
}
