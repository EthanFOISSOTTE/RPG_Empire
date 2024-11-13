package com.empire.rpg.player.Inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private boolean showInteractionFrame = false;

    private int selectedCategoryIndex = 0;
    private final String[] categories = {"Équipement", "Outil", "Consommable", "Objet de quête", "Divers"};

    private int selectedObjectIndex = 0;
    private boolean inCategorySelection = true;
    private List<Item> items;  // Liste des objets chargés à partir du JSON

    public Inventory(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.items = new ArrayList<>();
        loadInventoryFromJson();  // Charger les données JSON à l'initialisation
    }

    private void loadInventoryFromJson() {
        Json json = new Json();
        JsonValue base = new JsonReader().parse(Gdx.files.internal("Bdd/inventory.json"));

        for (JsonValue itemJson : base.get("inventory")) {
            String type = itemJson.getString("type");
            String nom = itemJson.getString("nom");
            int quantité = itemJson.getInt("quantité");
            String description = itemJson.getString("description");
            int valeur = itemJson.getInt("valeur", 0);  // Valeur par défaut 0 si absent

            Item item = new Item(type, nom, quantité, description, valeur);
            items.add(item);
        }
    }

    public void render(Vector2 playerPosition) {
        if (showInteractionFrame) {
            drawInteractionFrame(playerPosition);
        }
    }

    private void drawInteractionFrame(Vector2 playerPosition) {
        float frameWidth = 600;
        float frameHeight = 300;
        float frameX = playerPosition.x - frameWidth / 2;
        float frameY = playerPosition.y - frameHeight / 2;

        // Fond noir transparent
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        // Contour blanc
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        // Calculer la largeur des colonnes pour que objets et description aient la même largeur
        float categoryObjectSeparatorX = frameX + frameWidth / 3; // 1/3 pour les catégories
        float objectDetailSeparatorX = frameX + (2 * frameWidth / 3); // 2/3 pour objets

        // Ligne de séparation entre catégories et objets
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(categoryObjectSeparatorX, frameY, categoryObjectSeparatorX, frameY + frameHeight);
        shapeRenderer.end();

        // Ligne de séparation entre objets et détails
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.line(objectDetailSeparatorX, frameY, objectDetailSeparatorX, frameY + frameHeight);
        shapeRenderer.end();


        batch.begin();
        font.draw(batch, "Inventaire", frameX + 15, frameY + frameHeight - 15);


        drawCategoriesMenu(frameX, frameY, frameHeight);
        drawObjectsMenu(frameX, frameY, frameHeight);

        // Afficher les détails de l'objet sélectionné dans la colonne alignée à la colonne objets
        drawItemDetails(objectDetailSeparatorX, frameY, frameHeight);  // Utiliser objectDetailSeparatorX pour l'alignement

        batch.end();
    }

    private void drawCategoriesMenu(float frameX, float frameY, float frameHeight) {
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            float textY = frameY + frameHeight - 60 - i * 30;
            font.draw(batch, (i == selectedCategoryIndex ? "-> " : "") + category, frameX + 20, textY);
        }
    }

    private void drawObjectsMenu(float frameX, float frameY, float frameHeight) {
        String category = categories[selectedCategoryIndex];
        List<Item> currentItems = getItemsByType(category);

        for (int i = 0; i < currentItems.size(); i++) {
            Item item = currentItems.get(i);
            float textY = frameY + frameHeight - 60 - i * 30;
            String prefix = (!inCategorySelection && i == selectedObjectIndex) ? "-> " : "";
            font.draw(batch, prefix + item.getNom() + " (x" + item.getQuantité() + ")", frameX + 220, textY);
        }
    }

    private void drawItemDetails(float frameX, float frameY, float frameHeight) {
        // Vérifie que l'utilisateur est dans le mode de sélection d'objet
        if (!inCategorySelection) {
            // Récupérer la catégorie et l'objet sélectionnés
            String category = categories[selectedCategoryIndex];
            List<Item> currentItems = getItemsByType(category);

            // Vérifie qu'il y a bien un objet sélectionné dans la catégorie
            if (!currentItems.isEmpty() && selectedObjectIndex < currentItems.size()) {
                Item selectedItem = currentItems.get(selectedObjectIndex);

                // Définir la même position de départ en Y que les sections catégories et objets
                float startY = frameY + frameHeight - 60;

                // Afficher le nom de l'objet
                font.draw(batch, "Nom : " + selectedItem.getNom(), frameX + 10, startY);

                // Afficher la description de l'objet, légèrement en dessous
                font.draw(batch, "Description :", frameX + 10, startY - 30);
                font.draw(batch, selectedItem.getDescription(), frameX + 10, startY - 60);

                // Afficher la valeur de l'objet (si elle est différente de zéro), un peu plus bas
                if (selectedItem.getValeur() > 0) {
                    font.draw(batch, "Valeur : " + selectedItem.getValeur(), frameX + 10, startY - 90);
                }
            }
        }
    }

    private List<Item> getItemsByType(String type) {
        return items.stream().filter(item -> item.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (inCategorySelection) {
                selectedCategoryIndex = (selectedCategoryIndex - 1 + categories.length) % categories.length;
            } else {
                List<Item> currentItems = getItemsByType(categories[selectedCategoryIndex]);
                if (currentItems.size() > 0) {
                    selectedObjectIndex = (selectedObjectIndex - 1 + currentItems.size()) % currentItems.size();
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (inCategorySelection) {
                selectedCategoryIndex = (selectedCategoryIndex + 1) % categories.length;
            } else {
                List<Item> currentItems = getItemsByType(categories[selectedCategoryIndex]);
                if (currentItems.size() > 0) {
                    selectedObjectIndex = (selectedObjectIndex + 1) % currentItems.size();
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (inCategorySelection) {
                inCategorySelection = false;
                selectedObjectIndex = 0;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (!inCategorySelection) {
                inCategorySelection = true;
            }
        }
    }

    public void setShowInteractionFrame(boolean show) {
        this.showInteractionFrame = show;
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
