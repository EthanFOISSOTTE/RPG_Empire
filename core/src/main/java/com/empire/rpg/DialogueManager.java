package com.empire.rpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DialogueManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean showDialogueFrame = false;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Map<String, List<String>> dialogues;
    private String currentPNJ;
    private int dialogueIndex = 0; // Index actuel du dialogue affiché
    private String currentSpeaker = ""; // Nom du PNJ en train de parler
    private GlyphLayout glyphLayout = new GlyphLayout();

    public DialogueManager(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();

        loadDialogues("dialogues.json");
    }

    // Charger les dialogues depuis le fichier JSON
    private void loadDialogues(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dialogues = objectMapper.readValue(new File(filename), new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des dialogues !");
        }
    }

    // Méthode pour obtenir les dialogues
    public Map<String, List<String>> getDialogues() {
        return dialogues;
    }

    // Méthode pour obtenir un dialogue spécifique
    public List<String> getDialogue(String key) {
        return dialogues.get(key);
    }

    // Set the current PNJ the player is interacting with
    public void setCurrentPNJ(String pnjName) {
        this.currentPNJ = pnjName;
    }

    public void render(SpriteBatch batch, Vector2 playerPosition){
        if(showDialogueFrame){
            drawDialogueFrame(playerPosition);
        }

        if (showDialogueFrame && currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            // Affiche le dialogue actuel
            batch.begin();
            BitmapFont font = new BitmapFont();
            font.draw(batch, currentSpeaker + " : " + dialogues.get(currentSpeaker).get(dialogueIndex),
                playerPosition.x - 215, playerPosition.y - 110);
            batch.end();
        }

    }

    private void drawDialogueFrame(Vector2 playerPosition){
        float frameWidth = 450;
        float frameHeight = 100;
        float frameX = playerPosition.x - frameWidth / 2;
        float frameY = playerPosition.y - 200;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        if(showDialogueFrame){
            drawDialogue(frameX, frameY, frameWidth);
        }
    }

    // Active le cadre de dialogue pour un PNJ donné
    public void startDialogue(String speaker) {
        this.currentSpeaker = speaker;
        this.dialogueIndex = 0;
        this.showDialogueFrame = true;
    }

    // Avance au dialogue suivant ou ferme le cadre
    public void nextDialogue() {
        if (currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            if (dialogueIndex < dialogues.get(currentSpeaker).size() - 1) {
                dialogueIndex++;
            } else {
                // Tous les dialogues ont été affichés
                this.showDialogueFrame = false;
                this.currentSpeaker = null;
            }
        }
    }


    private void drawDialogue(float frameX, float frameY, float maxTextWidth) {
        if (currentPNJ != null && dialogues.containsKey(currentPNJ)) {
            List<String> pnjDialogues = dialogues.get(currentPNJ);
            float textY = frameY + 10;

            // Boucle à travers chaque ligne du dialogue
            for (String line : pnjDialogues) {
                // Diviser la ligne de texte pour qu'elle ne dépasse pas la largeur définie
                String[] wrappedLines = wrapText(line, maxTextWidth);  // Utiliser la largeur manuelle
                for (String wrappedLine : wrappedLines) {
                    batch.begin();
                    font.draw(batch, wrappedLine, frameX + 10, textY);  // Dessiner chaque ligne
                    batch.end();
                    textY += 20;  // Déplacer le texte vers le bas pour la prochaine ligne
                }
            }
        }
    }

    // Méthode pour diviser le texte en plusieurs lignes en fonction de la largeur personnalisée
    private String[] wrapText(String text, float maxTextWidth) {
        List<String> wrappedLines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        // Diviser le texte en mots
        String[] words = text.split(" ");

        for (String word : words) {
            // Ajouter le mot actuel à la ligne en cours
            String testLine = currentLine + word + " ";

            // Utiliser GlyphLayout pour calculer la largeur du texte avec la largeur personnalisée
            glyphLayout.setText(font, testLine);  // Mesurer la largeur du texte
            if (glyphLayout.width <= maxTextWidth) {
                currentLine.append(word).append(" ");  // Si la ligne ne dépasse pas, on ajoute le mot
            } else {
                // Si la ligne dépasse, ajouter la ligne en cours aux wrappedLines et commencer une nouvelle ligne
                wrappedLines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(word + " ");  // Commencer une nouvelle ligne avec le mot courant
            }
        }

        // Ajouter la dernière ligne après la boucle
        if (currentLine.length() > 0) {
            wrappedLines.add(currentLine.toString().trim());
        }

        return wrappedLines.toArray(new String[0]);  // Retourner toutes les lignes
    }


    // Désactive le cadre de dialogue
    public void closeDialogue() {
        this.showDialogueFrame = false;
        this.currentSpeaker = null;
    }

    public boolean isShowDialogueFrame() {
        return showDialogueFrame;
    }

    public void setShowDialogueFrame(boolean showDialogueFrame) {
        this.showDialogueFrame = showDialogueFrame;
    }
}
