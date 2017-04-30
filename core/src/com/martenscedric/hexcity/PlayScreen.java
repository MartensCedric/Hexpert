package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cedricmartens.hexpert.HexGeometry;
import com.cedricmartens.hexpert.HexStyle;
import com.cedricmartens.hexpert.Hexagon;
import com.cedricmartens.hexpert.coordinate.Point;
import com.cedricmartens.hexpert.grid.HexGrid;
import com.cedricmartens.hexpert.grid.HexGridBuilder;
import com.cedricmartens.hexpert.grid.HexagonOrientation;
import com.cedricmartens.hexpert.grid.HexagonShape;

import static com.martenscedric.hexcity.Const.HEIGHT;
import static com.martenscedric.hexcity.Const.WIDTH;
import static com.martenscedric.hexcity.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexcity.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexcity.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexcity.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexcity.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexcity.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexcity.TextureData.TEXTURE_WIND;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen  extends StageScreen
{
    private final HexCity hexCity;
    private Map currentMap;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private StandardGestureBehavior behavior;
    private Table table;
    private Image menuImage;
    private ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket;

    public PlayScreen(HexCity hexCity) {
        super();
        this.hexCity = hexCity;
        this.batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        currentMap = new Map();
        currentMap.setGrid(new HexGridBuilder<Integer>()
                            .setHeight(7)
                            .setWidth(7)
                            .setShape(HexagonShape.HEXAGON)
                            .setStyle(new HexStyle(80.0, HexagonOrientation.FLAT_TOP))
                            .setOrigin(new Point(WIDTH/4, HEIGHT/5)));

        behavior = new StandardGestureBehavior(getCamera());
        detector = new GestureDetector(behavior);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(detector);
        inputMultiplexer.addProcessor(getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);


        menuImage = new Image((Texture)hexCity.assetManager.get(TEXTURE_MENUUI));
        menuImage.setX(WIDTH - menuImage.getWidth());
        menuImage.setY(0);
        getStage().addActor(menuImage);

        btnFarm = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_FARM))));
        btnFarm.getImageCell().expand().fill();
        btnHouse = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_HOUSE))));
        btnHouse.getImageCell().expand().fill();
        btnMine = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_MINE))));
        btnMine.getImageCell().expand().fill();
        btnWind = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_WIND))));
        btnWind.getImageCell().expand().fill();
        btnFactory = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_FACTORY))));
        btnFactory.getImageCell().expand().fill();
        btnMarket = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_MARKET))));
        btnMarket.getImageCell().expand().fill();
        btnBank = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_BANK))));
        btnBank.getImageCell().expand().fill();
        btnRocket = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_ROCKET))));
        btnRocket.getImageCell().expand().fill();

        table = new Table();
        table.defaults().width(menuImage.getPrefWidth()).height(menuImage.getPrefHeight()/9).pad(5);
        table.add(btnFarm);
        table.row();
        table.add(btnHouse);
        table.row();
        table.add(btnMine);
        table.row();
        table.add(btnWind);
        table.row();
        table.add(btnFactory);
        table.row();
        table.add(btnMarket);
        table.row();
        table.add(btnBank);
        table.row();
        table.add(btnRocket);


        table.setX(WIDTH - menuImage.getWidth()/2);
        table.setY(HEIGHT - table.getPrefHeight()/2);
        table.setDebug(false);
        getStage().addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta)
    {
        batch.setProjectionMatrix(getCamera().combined);
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin();
        Hexagon<Integer>[] hexagons = currentMap.getGrid().getHexs();
        for(int i = 0; i < hexagons.length; i++)
        {
            HexGeometry hexGeo = hexagons[i].getHexGeometry();
            Point p0 = (Point) hexGeo.getPoints().toArray()[0];
            Point pLast = (Point) hexGeo.getPoints().toArray()[hexGeo.getPoints().size() - 1];
            shapeRenderer.line((float)p0.getX(), (float)p0.getY(),
                    (float)pLast.getX(), (float)pLast.getY(),
                    Color.BLACK, Color.BLACK);
            for(int j = 1; j < hexGeo.getPoints().size(); j++)
            {
                Point current = (Point)hexGeo.getPoints().toArray()[j];
                Point precedent = (Point)hexGeo.getPoints().toArray()[j - 1];
                shapeRenderer.line((float)current.getX(), (float)current.getY(),
                        (float)precedent.getX(), (float)precedent.getY(),
                        Color.BLACK, Color.BLACK);
            }
        }

        shapeRenderer.end();
        batch.begin();

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
