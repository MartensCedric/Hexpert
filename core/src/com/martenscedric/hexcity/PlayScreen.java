package com.martenscedric.hexcity;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen implements Screen {

    private final HexCity hexCity;
    private SkyEffect skyEffect;
    private SpriteBatch batch;

    public PlayScreen(HexCity hexCity) {
        this.hexCity = hexCity;
        this.batch = new SpriteBatch();
        this.skyEffect = new SkyEffect();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        skyEffect.tick();
        skyEffect.draw(batch);
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
