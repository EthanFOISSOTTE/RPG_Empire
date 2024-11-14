package com.empire.rpg.player.equipment;

import java.util.List;

public class Tool {
    private String id; // ex : "SW01"
    private String name; // ex : "Épée de base"
    private String spritesheetKey; // ex : "SW01"
    private List<String> availableAttacks; // Liste des IDs d'attaques disponibles avec cet outil

    // Constructeur
    public Tool(String id, String name, String spritesheetKey, List<String> availableAttacks) {
        this.id = id;
        this.name = name;
        this.spritesheetKey = spritesheetKey;
        this.availableAttacks = availableAttacks;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpritesheetKey() { return spritesheetKey; }
    public List<String> getAvailableAttacks() { return availableAttacks;}

    // Setters
    public void setId(String id) { this.id = id;}
    public void setName(String name) { this.name = name; }
    public void setSpritesheetKey(String spritesheetKey) { this.spritesheetKey = spritesheetKey; }
    public void setAvailableAttacks(List<String> availableAttacks) { this.availableAttacks = availableAttacks; }
}
