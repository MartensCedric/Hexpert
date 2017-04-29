package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * Created by 1544256 on 2017-04-26.
 */

public abstract class StageScreen implements Screen
{
    private Stage stage;
    private OrthographicCamera camera;
    private StretchViewport viewport;

    public StageScreen() {
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(2000, 2000, camera);
        Gdx.input.setInputProcessor(getStage());
    }

    @Override
    public void render(float delta) {
        stage.act();

        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public StretchViewport getViewport() {
        return viewport;
    }
}
