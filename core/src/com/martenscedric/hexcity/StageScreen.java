package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.martenscedric.hexcity.Const.HEIGHT;
import static com.martenscedric.hexcity.Const.WIDTH;

/**
 * Created by 1544256 on 2017-04-26.
 */

public abstract class StageScreen implements Screen
{
    private Stage stage;
    private OrthographicCamera camera;

    public StageScreen() {

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.setToOrtho(false);
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT, camera));
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
}
