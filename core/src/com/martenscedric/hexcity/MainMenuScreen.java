package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class MainMenuScreen implements Screen
{
    private SpriteBatch batch;
    private SkyEffect skyEffect;
    public MainMenuScreen() {
        batch = new SpriteBatch();
        skyEffect = new SkyEffect();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        skyEffect.tick();
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        skyEffect.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
