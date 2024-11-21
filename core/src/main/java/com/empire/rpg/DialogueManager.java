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

public class DialogueManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean showDialogueFrame = false;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;  // Declare a BitmapFont
    private Map<String, List<String>> dialogues;  // Store dialogues for each PNJ
    private String currentPNJ;  // Store the current PNJ name
    private int dialogueIndex = 0; // Index actuel du dialogue affiché
    private String currentSpeaker = ""; // Nom du PNJ en train de parler

    public DialogueManager(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();

        font = new BitmapFont();  // Initialize the BitmapFont

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
            // Exemples de style : remplacer par une méthode de dessin plus avancée si nécessaire
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


    // Draw the dialogue text from the current PNJ
    private void drawDialogue(float frameX, float frameY, float frameWidth){
        if (currentPNJ != null && dialogues.containsKey(currentPNJ)) {
            List<String> pnjDialogues = dialogues.get(currentPNJ);
            float textY = frameY + 10;
            for (String line : pnjDialogues) {
                // Use the BitmapFont to draw the dialogue
                batch.begin();
                font.draw(batch, line, frameX + 10, textY);  // Draw each line of dialogue
                batch.end();
                textY += 20;  // Move the text down for the next line
            }
        }
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
