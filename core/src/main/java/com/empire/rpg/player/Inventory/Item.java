package com.empire.rpg.player.Inventory;

public class Item {
    private String type;
    private String nom;
    private int quantité;
    private String description;
    private int valeur;

    // Constructeur
    public Item(String type, String nom, int quantité, String description, int valeur) {
        this.type = type;
        this.nom = nom;
        this.quantité = quantité;
        this.description = description;
        this.valeur = valeur;
    }

    // Getters pour chaque attribut
    public String getType() {
        return type;
    }
    public String getNom() {
        return nom;
    }
    public int getQuantité() {
        return quantité;
    }
    public String getDescription() {
        return description;
    }
    public int getValeur() {
        return valeur;
    }
}
