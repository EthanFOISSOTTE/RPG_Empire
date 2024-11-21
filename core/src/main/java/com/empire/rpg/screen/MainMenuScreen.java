package com.empire.rpg.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.empire.rpg.Main;
import com.empire.rpg.system.FightSystem;

public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font;
    private int selectedOption = 0;
    private String[] options = new String[] { "Nouvelle Partie", "Quitter le jeu" };
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // Démarrer le batch pour dessiner les éléments
        batch.begin();

        // Dessiner le menu principal
        float menuX = WORLD_WIDTH / 2f - 100;
        float menuY = WORLD_HEIGHT - 50;

        font.draw(batch, "Main Menu", menuX, menuY);
        // Affiche les options du menu
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                font.setColor(1, 1, 0, 1); // Jaune pour l'option sélectionnée
            } else {
                font.setColor(1, 1, 1, 1); // Blanc pour les autres options
            }
            font.draw(batch, options[i], (float) Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() - 150 - i * 40);
        }

        // Fin du batch
        batch.end();

        // Gérer les entrées utilisateur pour naviguer dans le menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption = (selectedOption + options.length - 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    private void handleMenuSelection() {
        switch (selectedOption) {
            case 0:
                // Démarrer une nouvelle partie
                GameScreen gameScreen = new GameScreen();
                Main.getInstance().setScreen(gameScreen);
                break;
            case 1:
                // Quitter le jeu
                Gdx.app.exit();

                break;
        }
    }
}
