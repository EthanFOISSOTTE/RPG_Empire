package com.empire.rpg.utils;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.dat";

    // Sauvegarder les données dans un fichier
    public static void saveGame(SaveData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(data);
            System.out.println("Jeu sauvegardé !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    // Charger les données depuis un fichier
    public static SaveData loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            SaveData data = (SaveData) ois.readObject();
            System.out.println("Données chargées : " +
                "Nom=" + data.playerName +
                ", X=" + data.positionX +
                ", Y=" + data.positionY +
                ", Santé=" + data.currentHealth + "/" + data.maxHealth);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }
}

