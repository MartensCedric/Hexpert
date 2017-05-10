package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.martenscedric.hexpert.misc.AssetLoader;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.env.SkyEffect;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.WIDTH;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class MainMenuScreen extends StageScreen
{
    private SpriteBatch batch;
    private SkyEffect skyEffect;
    private TextButton playButton;
    private Hexpert hexpert;

    public MainMenuScreen(final Hexpert hexpert) {

        super();
        this.hexpert = hexpert;
        batch = new SpriteBatch();
        skyEffect = new SkyEffect(hexpert.assetManager);

        Skin skin = AssetLoader.getSkin();
        skin.add("default-font", AssetLoader.getFont(), BitmapFont.class);
        playButton = new TextButton("Play", skin);
        playButton.setWidth(WIDTH*0.35f);
        playButton.setHeight(HEIGHT*0.20f);
        playButton.setX(WIDTH/2 - playButton.getWidth()/2);
        playButton.setY(HEIGHT/2 - playButton.getHeight()/2);

        playButton.getLabel().setFontScale(5);
        playButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hexpert.sounds.get("select").play();
                hexpert.setScreen(hexpert.levelSelectScreen);
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
