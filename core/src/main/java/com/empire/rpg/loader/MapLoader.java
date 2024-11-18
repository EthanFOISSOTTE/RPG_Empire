package com.empire.rpg.loader;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import static com.empire.rpg.GameEngine.MAP_PATH;

/**
 * Classe MapLoader pour charger et gérer les entités et objets d'une carte Tiled.
 */
public class MapLoader implements Screen {
    private com.badlogic.gdx.maps.tiled.TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    /**
     * Constructeur principal pour charger la carte et initialiser la caméra.
     *
     */
    public MapLoader() {
        // Charger la carte depuis le chemin spécifié
        this.tiledMap = new TmxMapLoader().load(MAP_PATH);
        this.mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Configurer la caméra
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Méthode pour rendre les couches inférieures de la carte.
     */
    public void renderLowerLayers() {
        mapRenderer.setView(camera);
        mapRenderer.render(new int[]{0, 1, 2}); // Index des couches inférieures

    }

    /**
     * Méthode pour rendre les couches supérieures de la carte.
     */
    public void renderUpperLayers() {
        mapRenderer.setView(camera);
        mapRenderer.render(new int[]{3, 4, 5}); // Index des couches supérieures
    }

    /**
     * Récupérer la carte chargée.
     *
     * @return TiledMap instance.
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    @Override
    public void show() {
        // Méthode appelée lorsque l'écran devient visible


    }

    @Override
    public void render(float delta) {
        // Mise à jour de la caméra
        camera.update();

        // Rendu des couches
        renderLowerLayers();
        renderUpperLayers();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // Méthode appelée lorsque le jeu est mis en pause
    }

    @Override
    public void resume() {
        // Méthode appelée lorsque le jeu reprend après une pause
    }

    @Override
    public void hide() {
        // Méthode appelée lorsque l'écran n'est plus visible
    }

    @Override
    public void dispose() {
        // Libérer les ressources
        tiledMap.dispose();
        mapRenderer.dispose();
    }

    public TiledMap load(String mapPath) {
        return new TmxMapLoader().load(mapPath);

    }
}
