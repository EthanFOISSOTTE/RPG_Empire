package com.empire.rpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class DialogueManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean showDialogueFrame = false;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;  // Déclare une BitmapFont
    private Map<String, List<String>> dialogues;  // Stocke les dialogues pour chaque PNJ
    private String currentPNJ;  // Stocke le nom du PNJ actuel
    private int dialogueIndex = 0; // Index actuel du dialogue affiché
    private String currentSpeaker = ""; // Nom du PNJ qui parle actuellement
    private GlyphLayout glyphLayout = new GlyphLayout();  // Initialiser un GlyphLayout

    // Définir les dimensions du cadre
    private static final float DIALOGUE_WIDTH = 450f;
    private static final float DIALOGUE_HEIGHT = 125f;

    public DialogueManager(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();  // Initialiser la BitmapFont
        loadDialogues("dialogues.json");
    }

    // Charger les dialogues depuis un fichier JSON
    private void loadDialogues(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dialogues = objectMapper.readValue(new File(filename), new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des dialogues !");
        }
    }

    // Affiche le cadre du dialogue
    private void drawDialogueFrame(Vector2 playerPosition) {
        float frameX = playerPosition.x - DIALOGUE_WIDTH / 2;
        float frameY = playerPosition.y - 200;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1); // Couleur noire pour le fond
        shapeRenderer.rect(frameX, frameY, DIALOGUE_WIDTH, DIALOGUE_HEIGHT);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); // Couleur blanche pour les bordures
        shapeRenderer.rect(frameX, frameY, DIALOGUE_WIDTH, DIALOGUE_HEIGHT);
        shapeRenderer.end();
    }

    // Démarrer le dialogue avec un PNJ
    public void startDialogue(String speaker) {
        this.currentSpeaker = speaker;
        this.dialogueIndex = 0;  // Réinitialiser l'index
        this.showDialogueFrame = true;
    }

    // Avancer au dialogue suivant ou fermer le cadre si fini
    public void nextDialogue() {
        if (currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            if (dialogueIndex < dialogues.get(currentSpeaker).size() - 1) {
                dialogueIndex++;
            } else {
                this.showDialogueFrame = false; // Ferme le dialogue lorsque tous les dialogues sont montrés
                this.currentSpeaker = null;
            }
        }
    }

    // Découpe le texte pour qu'il s'adapte à la largeur du cadre
    private List<String> wrapText(String text, float maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");  // Diviser le texte en mots

        for (String word : words) {
            // Tester si ajouter ce mot dépasse la largeur du cadre
            String testLine = currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word;
            glyphLayout.setText(font, testLine);

            // Si la ligne dépasse la largeur maximale, ajouter la ligne actuelle à la liste et commencer une nouvelle ligne
            if (glyphLayout.width > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());  // Ajouter la ligne complète
                    currentLine = new StringBuilder(word);  // Commencer une nouvelle ligne avec le mot actuel
                }
            } else {
                // Si la ligne ne dépasse pas la largeur, ajouter le mot à la ligne
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        // Ajouter la dernière ligne si elle existe
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    // Dessiner le texte du dialogue
    private void drawDialogueText(float frameX, float frameY) {
        if (currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            List<String> pnjDialogues = dialogues.get(currentSpeaker);
            String currentDialogue = pnjDialogues.get(dialogueIndex);

            // Découper le texte en lignes qui s'adaptent à la largeur du cadre
            List<String> wrappedLines = wrapText(currentDialogue, DIALOGUE_WIDTH - 20); // 20px de marge

            // Dessiner le nom du PNJ en haut à gauche
            batch.begin();
            font.setColor(1, 1, 1, 1); // Couleur blanche pour le texte
            font.draw(batch, currentSpeaker + " :", frameX + 10, frameY + DIALOGUE_HEIGHT - 10); // Affiche le nom du PNJ
            batch.end();

            // Dessiner chaque ligne de dialogue dans l'ordre
            float yOffset = frameY + DIALOGUE_HEIGHT - 40;  // Décalage initial du texte (ajuster la position sous le nom du PNJ)
            batch.begin();
            for (String line : wrappedLines) {
                font.draw(batch, line, frameX + 10, yOffset);  // Dessiner chaque ligne
                yOffset -= 20;  // Espacer les lignes verticalement
            }
            batch.end();
        }
    }

    // Méthode de rendu pour afficher le dialogue
    public void render(SpriteBatch batch, Vector2 playerPosition) {
        if (showDialogueFrame) {
            drawDialogueFrame(playerPosition);  // Dessiner le cadre de dialogue
            drawDialogueText(playerPosition.x - DIALOGUE_WIDTH / 2, playerPosition.y - 200);  // Dessiner le texte du dialogue
        }
    }

    // Fermer le dialogue
    public void closeDialogue() {
        this.showDialogueFrame = false;
        this.currentSpeaker = null;
    }

    // Vérifie si le cadre de dialogue doit être affiché
    public boolean isShowDialogueFrame() {
        return showDialogueFrame;
    }

    // Permet de définir si le cadre de dialogue doit être affiché
    public void setShowDialogueFrame(boolean showDialogueFrame) {
        this.showDialogueFrame = showDialogueFrame;
    }
}
