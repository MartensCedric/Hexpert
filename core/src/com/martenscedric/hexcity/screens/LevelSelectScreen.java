package com.martenscedric.hexcity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cedricmartens.hexpert.grid.HexGrid;
import com.martenscedric.hexcity.HexCity;
import com.martenscedric.hexcity.misc.AssetLoader;
import com.martenscedric.hexcity.tile.TileData;

import static com.martenscedric.hexcity.misc.Const.HEIGHT;
import static com.martenscedric.hexcity.misc.Const.WIDTH;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table table;
    private int levelsToDisplay = 5;
    private HexCity hexCity;
    private HexGrid<TileData> previewGrid;

    public LevelSelectScreen(final HexCity hexCity)
    {
        super();
        this.hexCity = hexCity;
        table = new Table();
        table.setX(WIDTH/2);
        table.setY(HEIGHT/4);
        table.defaults().pad(20);
        previewGrid = loadLevel(1);

        for(int i = 0; i < levelsToDisplay; i++)
        {
            TextButton button = new TextButton(Integer.toString(i + 1), AssetLoader.getSkin());
            button.getLabel().setFontScale(5);
            table.add(button);
        }
        table.row();
        TextButton button = new TextButton("Select", AssetLoader.getSkin());
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hexCity.setScreen(new PlayScreen(hexCity));
            }
        });
        button.getLabel().setFontScale(5);
        table.add(button).colspan(levelsToDisplay);
        getStage().addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render(delta);
    }

    @Override
    public void show() {

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

    private HexGrid<TileData> loadLevel(int levelId)
    {
        return null;
    }
}
