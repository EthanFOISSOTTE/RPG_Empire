package com.empire.rpg.player.Inventory;

public class Item {
    private String type;
    private String nom;
    private int quantity;
    private String description;
    private int valeur;
    private boolean states;

    // Constructeur
    public Item(String type, String nom, int quantity, String description, int valeur,boolean states ) {
        this.type = type;
        this.nom = nom;
        this.quantity = quantity;
        this.description = description;
        this.valeur = valeur;
        this.states = states;
    }

    // Getters pour chaque attribut
    public String getType() {
        return type;
    }
    public String getNom() {
        return nom;
    }
    public int getQuantité() {
        return quantity;
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

    // Setters pour chaque attribut
    public void setStates(boolean states) {
        this.states = states;
    }
}
