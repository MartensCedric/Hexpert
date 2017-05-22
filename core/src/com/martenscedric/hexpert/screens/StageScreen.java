package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.WIDTH;

/**
 * Created by 1544256 on 2017-04-26.
 */

public abstract class StageScreen implements Screen
{
    private Stage stage;
    private OrthographicCamera camera;


    public StageScreen() {
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        stage = new Stage(new StretchViewport(WIDTH, HEIGHT));
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

    @Override
    public void resize(int width, int height) {
       stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getStage());
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
