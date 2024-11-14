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

    //partie ouverture de l'inventaire
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private boolean showInteractionFrame = false;

    //partie organisation de l'inventaire
    private final String[] categories = {"Armes", "Tenue" , "Outil", "Consommable", "Objet de quête", "Divers"};
    private int selectedCategoryIndex = 0;
    private int selectedObjectIndex = 0;
    private boolean inCategorySelection = true;
    private List<Item> items;  // Liste des objets chargés à partir du JSON

    //scroll des objet
    private static final int MAX_VISIBLE_ITEMS = 7; // Nombre maximum d'objets affichés en même temps
    private int scrollOffset = 0; // Décalage de défilement pour l'affichage des objets


    private Item equippedArmor;
    private Item equippedConsommable;
    private Item equippedLance;
    private Item equippedEpee;
    private Item equippedArc;


    public Inventory(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.items = new ArrayList<>();
        loadInventoryFromJson();  // Charger les données JSON à l'initialisation
    }

    //partie trie de la liste du fichier JSON
    private void loadInventoryFromJson() {
        Json json = new Json();
        JsonValue base = new JsonReader().parse(Gdx.files.internal("Bdd/inventory.json"));

        for (JsonValue itemJson : base.get("inventory")) {
            String type = itemJson.getString("type");
            String nom = itemJson.getString("nom");
            int quantity = itemJson.getInt("quantity");
            String description = itemJson.getString("description");
            int valeur = itemJson.getInt("valeur", 0);  // Valeur par défaut 0 si absent
            boolean states = itemJson.getBoolean("states",false);
            String style = itemJson.getString("style","null");

            Item item = new Item(type, nom, quantity, description, valeur, states, style);
            items.add(item);
        }
    }

    public void render(Vector2 playerPosition) {
        if (showInteractionFrame) {
            drawInteractionFrame(playerPosition);
        }
    }

    // partie création de l'interface graphique de l'inventaire
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

        // Affiche les catégorie
        drawCategoriesMenu(frameX, frameY, frameHeight);

        // Affiche les objet
        drawObjectsMenu(frameX, frameY, frameHeight);

        // Affiche les détails de l'objet sélectionné
        drawItemDetails(objectDetailSeparatorX, frameY, frameHeight);  // Utiliser objectDetailSeparatorX pour l'alignement

        batch.end();
    }

    // partie création de la partie catégorie
    private void drawCategoriesMenu(float frameX, float frameY, float frameHeight) {
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            float textY = frameY + frameHeight - 60 - i * 30;
            font.draw(batch, (i == selectedCategoryIndex ? "-> " : "") + category, frameX + 20, textY);
        }
    }

    // partie création de la partie objet
    private void drawObjectsMenu(float frameX, float frameY, float frameHeight) {
        String category = categories[selectedCategoryIndex];
        List<Item> currentItems = getItemsByType(category);

        int start = scrollOffset;
        int end = Math.min(start + MAX_VISIBLE_ITEMS, currentItems.size());

        for (int i = start; i < end; i++) {
            Item item = currentItems.get(i);
            float textY = frameY + frameHeight - 60 - (i - start) * 30;

            // Indicateur pour l'objet sélectionné et équipé
            String prefix = (!inCategorySelection && i == selectedObjectIndex) ? "-> " : "";
            String equippedIndicator = item.getStates() ? " <-" : "";

            font.draw(batch, prefix + item.getNom() + " (x" + item.getQuantity() + ")" + equippedIndicator, frameX + 220, textY);
        }
    }

    // partie création de la partie détail d'objet
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
                font.draw(batch,"Statue: " + selectedItem.getStates(),frameX + 10, startY - 120); // A supp
                font.draw(batch,"Style: " + selectedItem.getStyle(),frameX + 10, startY - 150); // A supp
            }
        }
    }

    // Appelle un objet selon son type
    private List<Item> getItemsByType(String type) {
        return items.stream().filter(item -> item.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    //partie initialisation de touche
    public void update() {
        List<Item> currentItems = getItemsByType(categories[selectedCategoryIndex]);

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (inCategorySelection) {
                selectedCategoryIndex = (selectedCategoryIndex - 1 + categories.length) % categories.length;
            } else {
                if (selectedObjectIndex > 0) {
                    selectedObjectIndex--;
                    // Vérifier si on doit défiler vers le haut
                    if (selectedObjectIndex < scrollOffset) {
                        scrollOffset--;
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if (inCategorySelection) {
                selectedCategoryIndex = (selectedCategoryIndex + 1) % categories.length;
            } else {
                if (selectedObjectIndex < currentItems.size() - 1) {
                    selectedObjectIndex++;
                    // Vérifier si on doit défiler vers le bas
                    if (selectedObjectIndex >= scrollOffset + MAX_VISIBLE_ITEMS) {
                        scrollOffset++;
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (inCategorySelection) {
                inCategorySelection = false;
                selectedObjectIndex = 0;
                scrollOffset = 0; // Réinitialiser le défilement pour le nouvel écran d'objets
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (!inCategorySelection) {
                inCategorySelection = true;
                selectedObjectIndex = 0;
                scrollOffset = 0; // Réinitialiser le défilement pour le nouvel écran de catégories
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (!inCategorySelection && !currentItems.isEmpty() && selectedObjectIndex < currentItems.size()) {
                Item selectedItem = currentItems.get(selectedObjectIndex);

                // Si l'objet est actuellement équipé (states = true), le déséquiper (mettre states à false)
                if (selectedItem.getStates()) {
                    selectedItem.setStates(false);

                    // Vérifier si c'est un type équipé et le retirer de l'équipement actif
                    if (selectedItem.getType().equalsIgnoreCase("Tenue")) {
                        equippedArmor = null;
                    } else if (selectedItem.getType().equalsIgnoreCase("Consommable")) {
                        equippedConsommable = null;
                    }
                } else {
                    // Si l'objet n'est pas équipé, l'équiper
                    if (selectedItem.getType().equalsIgnoreCase("Tenue")) {
                        if (equippedArmor != null) equippedArmor.setStates(false);
                        equippedArmor = selectedItem;
                        selectedItem.setStates(true);
                    } else if (selectedItem.getType().equalsIgnoreCase("Consommable")) {
                        if (equippedConsommable != null) equippedConsommable.setStates(false);
                        equippedConsommable = selectedItem;
                        selectedItem.setStates(true);
                    }
                }
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
