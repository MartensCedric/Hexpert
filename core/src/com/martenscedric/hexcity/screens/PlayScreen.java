package com.martenscedric.hexcity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexcity.HexCity;
import com.martenscedric.hexcity.gestures.PlayScreenGestureBehavior;
import com.martenscedric.hexcity.map.Map;
import com.martenscedric.hexcity.misc.AssetLoader;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

import static com.martenscedric.hexcity.misc.Const.HEIGHT;
import static com.martenscedric.hexcity.misc.Const.WIDTH;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_WIND;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen  extends StageScreen
{
    private HexCity hexCity;
    private HexMap<TileData> grid;
    private SpriteBatch batch;
    private PolygonSpriteBatch polyBatch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private Table table;
    private Image menuImage;
    private ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket;
    private Label lblScore;
    private String scoreTxt = "SCORE : %d";
    private int score = 0;
    private BuildingType selection;

    public PlayScreen(final HexCity hexCity, Map map) {
        super();
        this.hexCity = hexCity;
        this.batch = new SpriteBatch();
        this.polyBatch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        grid = map.build();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setColor(TileType.values()[data.getTileType().ordinal()].getColor());
            data.setBuildingType(BuildingType.ROCKET);
            data.setTexture(hexCity.getTextureByBuilding(BuildingType.ROCKET));
            hex.setHexData(data);
        }


        getCamera().update();
        setMultiplexer();


        menuImage = new Image((Texture)hexCity.assetManager.get(TEXTURE_MENUUI));
        menuImage.setX(WIDTH - menuImage.getWidth());
        menuImage.setY(0);
        getStage().addActor(menuImage);

        btnFarm = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_FARM))));
        btnFarm.getImageCell().expand().fill();
        btnFarm.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.FARM;
                }
            }
        );

        btnHouse = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_HOUSE))));
        btnHouse.getImageCell().expand().fill();

        btnHouse.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.HOUSE;
                }
            }
        );

        btnMine = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_MINE))));
        btnMine.getImageCell().expand().fill();

        btnMine.addListener(new ClickListener()
             {
                 @Override
                 public void clicked(InputEvent event, float x, float y) {
                     selection = BuildingType.MINE;
                 }
             }
        );


        btnWind = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_WIND))));
        btnWind.getImageCell().expand().fill();

        btnWind.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.WIND;
                }
            }
        );

        btnFactory = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_FACTORY))));
        btnFactory.getImageCell().expand().fill();
        btnFactory.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.FACTORY;
                }
            }
        );

        btnMarket = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_MARKET))));
        btnMarket.getImageCell().expand().fill();

        btnMarket.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.MARKET;
                }
            }
        );

        btnBank = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_BANK))));
        btnBank.getImageCell().expand().fill();

        btnBank.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.BANK;
                }
            }
        );

        btnRocket = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexCity.assetManager.get(TEXTURE_ROCKET))));
        btnRocket.getImageCell().expand().fill();

        btnRocket.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selection = BuildingType.ROCKET;
                }
            }
        );

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

        lblScore = new Label(String.format(scoreTxt, score), AssetLoader.getSkin());
        lblScore.setFontScale(5);
        lblScore.setX(5);
        lblScore.setY(25);

        getStage().addActor(table);
        getStage().addActor(lblScore);
    }

    private void setMultiplexer()
    {
        behavior = new PlayScreenGestureBehavior(this);
        detector = new GestureDetector(behavior);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(getStage());
        inputMultiplexer.addProcessor(detector);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {
        setMultiplexer();
    }

    @Override
    public void render(float delta)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK))
        {
            hexCity.setScreen(hexCity.levelSelectScreen);
        }

        batch.setProjectionMatrix(getCamera().combined);
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        polyBatch.setProjectionMatrix(getCamera().combined);
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        polyBatch.begin();
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            PolygonSprite sprite = hex.getHexData().getSprite();
            sprite.draw(polyBatch);
        }
        polyBatch.end();

        batch.begin();
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData().getTexture(),
                    (float)(middlePoint.x - grid.getStyle().getSize()/2),
                    (float)(middlePoint.y - grid.getStyle().getSize()/2),
                    (float)grid.getStyle().getSize(),
                    (float)grid.getStyle().getSize());
        }
        batch.end();

        shapeRenderer.begin();
        Hexagon<Integer>[] hexagons = grid.getHexs();
        for(int i = 0; i < hexagons.length; i++)
        {
            HexGeometry hexGeo = hexagons[i].getHexGeometry();
            Point p0 = (Point) hexGeo.getPoints().toArray()[0];
            Point pLast = (Point) hexGeo.getPoints().toArray()[hexGeo.getPoints().size() - 1];
            shapeRenderer.line((float)p0.x, (float)p0.y,
                    (float)pLast.x, (float)pLast.y,
                    Color.BLACK, Color.BLACK);
            for(int j = 1; j < hexGeo.getPoints().size(); j++)
            {
                Point current = (Point)hexGeo.getPoints().toArray()[j];
                Point precedent = (Point)hexGeo.getPoints().toArray()[j - 1];
                shapeRenderer.line((float)current.x, (float)current.y,
                        (float)precedent.x, (float)precedent.y,
                        Color.BLACK, Color.BLACK);
            }
        }

        shapeRenderer.end();

        lblScore.setText(String.format(scoreTxt, score));
        super.render(delta);
    }

    public HexCity getHexCity() {
        return hexCity;
    }

    public HexMap<TileData> getGrid() {
        return grid;
    }

    public BuildingType getSelection() {
        return selection;
    }

    public void setSelection(BuildingType selection) {
        this.selection = selection;
    }

    public SpriteBatch getBatch() {
        return batch;
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
