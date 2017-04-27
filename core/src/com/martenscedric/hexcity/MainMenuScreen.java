package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class MainMenuScreen extends StageScreen
{
    private SpriteBatch batch;
    private SkyEffect skyEffect;
    private TextButton playButton;
    private HexCity hexCity;

    public MainMenuScreen(final HexCity hexCity) {

        super();
        this.hexCity = hexCity;
        Gdx.input.setInputProcessor(getStage());
        batch = new SpriteBatch();
        skyEffect = new SkyEffect();

        Skin skin = AssetLoader.getSkin();
        skin.add("default-font", AssetLoader.getFont(), BitmapFont.class);
        playButton = new TextButton("Play", skin);
        playButton.setWidth(Gdx.graphics.getWidth()*0.35f);
        playButton.setHeight(Gdx.graphics.getHeight()*0.20f);
        playButton.setX(Gdx.graphics.getWidth()/2 - playButton.getWidth()/2);
        playButton.setY(Gdx.graphics.getHeight()/2 - playButton.getHeight()/2);
        playButton.getLabel().setFontScale(5f);
        playButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hexCity.setScreen(new PlayScreen(hexCity));
                super.clicked(event, x, y);
            }
        });

        getStage().addActor(playButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getStage());
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
        super.render(delta);
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
