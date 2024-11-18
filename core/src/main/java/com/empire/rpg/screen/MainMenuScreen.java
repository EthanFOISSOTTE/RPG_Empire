package com.empire.rpg.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MainMenuScreen implements Screen {

    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BitmapFont font;
    private int selectedOption = 0; // Option sélectionnée (0 = Start Game, 1 = Exit)
    private final String[] menuOptions = {"1. Start Game", "2. Exit"};

    public MainMenuScreen() {
        // Initialisation des composants graphiques
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        font = new BitmapFont(); // Utilise la police par défaut
        font.setColor(Color.WHITE);
        font.getData().setScale(2); // Taille du texte
    }

    @Override
    public void show() {
        Gdx.app.log("MainMenuScreen", "Menu principal affiché");
    }

    @Override
    public void render(float delta) {
        // Gestion des entrées utilisateur
        handleInput();

        // Nettoyage de l'écran
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Mise à jour de la caméra
        camera.update();

        // Début du dessin
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Calcul des positions pour centrer le texte
        float centerX = WORLD_WIDTH / 2;
        float startY = WORLD_HEIGHT - 100;

        // Dessiner le menu principal
        font.setColor(Color.WHITE);
        font.draw(batch, "Main Menu", centerX - 100, startY);

        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                font.setColor(Color.YELLOW); // Couleur pour l'option sélectionnée
            } else {
                font.setColor(Color.WHITE);
            }
            font.draw(batch, menuOptions[i], centerX - 100, startY - 50 * (i + 1));
        }

        batch.end();
    }

    private void handleInput() {
        // Navigation avec les touches fléchées
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length; // Remonte dans le menu
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1) % menuOptions.length; // Descend dans le menu
        }

        // Validation de l'option avec la touche ENTER
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selectedOption == 0) {
                // Lancer le jeu
                Gdx.app.log("MainMenuScreen", "Start Game sélectionné");



            } else if (selectedOption == 1) {
                // Quitter le jeu
                Gdx.app.log("MainMenuScreen", "Exit sélectionné");
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0); // Recentrage de la caméra
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.app.log("MainMenuScreen", "Menu principal masqué");
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
