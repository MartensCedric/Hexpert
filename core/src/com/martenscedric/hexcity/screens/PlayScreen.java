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

import java.util.Stack;

import static com.martenscedric.hexcity.misc.Const.HEIGHT;
import static com.martenscedric.hexcity.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexcity.misc.Const.WIDTH;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_BADMOVE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_WIND;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen  extends StageScreen
{
    private HexCity hexCity;
    private HexMap<TileData> grid;
    private Map map;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private Table table;
    private Image menuImage;
    private ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket,
                btnReset, btnUndo;
    private Label lblScore;
    private String scoreTxt = "SCORE : %d";
    private int score = 0;
    private BuildingType selection;
    private ImageButton selectedButton;
    private boolean debug = false;

    private Stack<TileData> placementHistory = new Stack<TileData>();

    public PlayScreen(final HexCity hexCity, Map map) {
        super();
        this.hexCity = hexCity;
        this.map = map;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        grid = map.build();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexCity.getTextureByTerrain(data.getTileType()));
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
                    selectedButton = btnFarm;
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
                    selectedButton = btnHouse;
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
                     selectedButton = btnMine;
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
                    selectedButton = btnWind;
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
                    selectedButton = btnFactory;
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
                    selectedButton = btnMarket;
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
                    selectedButton = btnBank;
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
                    selectedButton = btnRocket;
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

        btnReset = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexCity.assetManager.get(TEXTURE_RESET))));
        btnReset.setX(5);
        btnReset.setY(HEIGHT-btnReset.getPrefHeight());
        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetGrid();
            }
        });

        btnUndo = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexCity.assetManager.get(TEXTURE_UNDO))));
        btnUndo.setX(10 + btnReset.getPrefWidth());
        btnUndo.setY(HEIGHT-btnUndo.getPrefHeight());
        btnUndo.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                undo();
            }
        });


        getStage().addActor(table);
        getStage().addActor(lblScore);
        getStage().addActor(btnReset);
        getStage().addActor(btnUndo);
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
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];

            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData().getTerrainTexture(),
                    (float)(middlePoint.x - grid.getStyle().getSize()),
                    (float)(middlePoint.y - grid.getStyle().getSize()*HEX_HEIGHT_RATIO) - 24,
                    (float)grid.getStyle().getSize()*2,
                    (float) ((float)grid.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

        }

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            if(hex.getHexData().getBuildingTexture() != null)
            {
                Point middlePoint = hex.getHexGeometry().getMiddlePoint();
                batch.draw(hex.getHexData().getBuildingTexture(),
                        (float)(middlePoint.x - grid.getStyle().getSize()/2),
                        (float)(middlePoint.y - grid.getStyle().getSize()/2),
                        (float)grid.getStyle().getSize(),
                        (float)grid.getStyle().getSize());
            }
        }

        batch.end();

        if(debug)
        {
            shapeRenderer.begin();
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            Hexagon<Integer>[] hexagons = grid.getHexs();
            for (int i = 0; i < hexagons.length; i++) {
                HexGeometry hexGeo = hexagons[i].getHexGeometry();
                Point p0 = (Point) hexGeo.getPoints().toArray()[0];
                Point pLast = (Point) hexGeo.getPoints().toArray()[hexGeo.getPoints().size() - 1];
                shapeRenderer.line((float) p0.x, (float) p0.y,
                        (float) pLast.x, (float) pLast.y,
                        Color.BLACK, Color.BLACK);
                for (int j = 1; j < hexGeo.getPoints().size(); j++) {
                    Point current = (Point) hexGeo.getPoints().toArray()[j];
                    Point precedent = (Point) hexGeo.getPoints().toArray()[j - 1];
                    shapeRenderer.line((float) current.x, (float) current.y,
                            (float) precedent.x, (float) precedent.y,
                            Color.BLACK, Color.BLACK);
                }
            }

            shapeRenderer.end();
        }

        updateScore();
        super.render(delta);

        if(selectedButton != null)
        {
            shapeRenderer.begin();
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(WIDTH - 160, selectedButton.getY() + 567, selectedButton.getImage().getImageWidth() + 2, selectedButton.getImage().getImageHeight() + 2);
            shapeRenderer.end();
            shapeRenderer.end();
        }
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

    public Stack<TileData> getPlacementHistory() {
        return placementHistory;
    }

    public void setSelection(BuildingType selection) {
        this.selection = selection;
        if(selection == null)
            selectedButton = null;
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

    private void updateScore()
    {
        score = 0;
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            score+=data.getBuildingType().getScore() * data.getTileType().getMultiplier();
        }

        lblScore.setText(String.format(scoreTxt, score));
    }

    private void resetGrid()
    {
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            data.setBuildingType(map.getBuildingTypes()[i]);
            data.setBuildingTexture(hexCity.getTextureByBuilding(map.getBuildingTypes()[i]));
        }

        setSelection(null);
        placementHistory.clear();
        updateScore();
    }

    private void undo()
    {
        if(placementHistory.size() > 0)
        {
            TileData data = placementHistory.pop();
            data.setBuildingTexture(null);
            data.setBuildingType(BuildingType.NONE);
            setSelection(null);
            updateScore();
        }
    }
}
