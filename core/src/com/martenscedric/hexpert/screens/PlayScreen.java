package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.event.ExitDialog;
import com.martenscedric.hexpert.env.MoveEventManager;
import com.martenscedric.hexpert.env.SkyEffect;
import com.martenscedric.hexpert.event.ObjectiveDialog;
import com.martenscedric.hexpert.gestures.PlayScreenGestureBehavior;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.misc.AssetLoader;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;

import java.util.Stack;

import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexpert.misc.Const.WIDTH;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WIND;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen  extends StageScreen
{
    private Hexpert hexpert;
    private HexMap<TileData> grid;
    private Map map;
    private SpriteBatch batch;
    private SpriteBatch absBatch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private Table table, tableBtn;
    private Image menuImage;
    private ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket,
                btnReset, btnUndo, btnBack, btnHelp;
    private int score = 0;
    private BuildingType selection;
    private ImageButton selectedButton;
    private MoveEventManager moveEventManager;
    private boolean debug = false;
    private MapResult mapResult;
    private boolean drawerOpen = false;
    private SkyEffect skyEffect;
    private boolean[] objectivePassed;
    private ExitDialog exitDialog;
    private ObjectiveDialog objectiveDialog;
    private TextButton objectivesButton;

    private Stack<TileData> placementHistory = new Stack<TileData>();

    public PlayScreen(final Hexpert hexpert, final Map map, MapResult result) {
        super();
        this.hexpert = hexpert;
        this.exitDialog = new ExitDialog(AssetLoader.getSkin(), hexpert);
        this.objectiveDialog = new ObjectiveDialog(AssetLoader.getSkin(), hexpert);
        this.map = map;
        mapResult = result;
        this.batch = new SpriteBatch();
        this.absBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        moveEventManager = new MoveEventManager(this);
        grid = map.build();
        objectivePassed = new boolean[map.getObjectives().length];

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);
        }

        getCamera().update();
        setMultiplexer();

        menuImage = new Image((Texture) hexpert.assetManager.get(TEXTURE_MENUUI));
        menuImage.setX(WIDTH - menuImage.getWidth());
        menuImage.setY(0);
        getStage().addActor(menuImage);

        TextureRegionDrawable drawableFarm = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));
        TextureRegionDrawable drawableHouse = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        TextureRegionDrawable drawableMine = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));
        TextureRegionDrawable drawableWind = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        TextureRegionDrawable drawableFactory = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        TextureRegionDrawable drawableMarket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));
        TextureRegionDrawable drawableBank = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));
        TextureRegionDrawable drawableRocket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET)));

        btnFarm = new ImageButton(drawableFarm);
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

        btnHouse = new ImageButton(drawableHouse);
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

        btnMine = new ImageButton(drawableMine);
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


        btnWind = new ImageButton(drawableWind);
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

        btnFactory = new ImageButton(drawableFactory);
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

        btnMarket = new ImageButton(drawableMarket);
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

        btnBank = new ImageButton(drawableBank);
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

        btnRocket = new ImageButton(drawableRocket);
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
        ImageButton imbNotFarm = new ImageButton(drawableFarm);
        imbNotFarm.getImageCell().expand().fill();
        table.add(imbNotFarm);

        table.row();
        table.add(btnHouse);
        ImageButton imbFarm = new ImageButton(drawableFarm);
        imbFarm.getImageCell().expand().fill();
        table.add(imbFarm);

        table.row();
        table.add(btnMine);

        ImageButton imbHouse1 = new ImageButton(drawableHouse);
        imbHouse1.getImageCell().expand().fill();
        table.add(imbHouse1);

        table.row();
        table.add(btnWind);

        ImageButton imbHouse2 = new ImageButton(drawableHouse);
        imbHouse2.getImageCell().expand().fill();
        table.add(imbHouse2);

        table.row();
        table.add(btnFactory);

        ImageButton imbHouse3 = new ImageButton(drawableHouse);
        imbHouse3.getImageCell().expand().fill();
        table.add(imbHouse3);

        ImageButton imbMine1 = new ImageButton(drawableMine);
        imbMine1.getImageCell().expand().fill();
        table.add(imbMine1);

        ImageButton imbWind1 = new ImageButton(drawableWind);
        imbWind1.getImageCell().expand().fill();
        table.add(imbWind1);

        table.row();
        table.add(btnMarket);

        ImageButton imbHouse4 = new ImageButton(drawableHouse);
        imbHouse4.getImageCell().expand().fill();
        table.add(imbHouse4);

        ImageButton imbWind2 = new ImageButton(drawableWind);
        imbWind2.getImageCell().expand().fill();
        table.add(imbWind2);

        ImageButton imbFactory1 = new ImageButton(drawableFactory);
        imbFactory1.getImageCell().expand().fill();
        table.add(imbFactory1);


        table.row();
        table.add(btnBank);

        ImageButton imbHouse5 = new ImageButton(drawableHouse);
        imbHouse5.getImageCell().expand().fill();
        table.add(imbHouse5);

        ImageButton imbMine2 = new ImageButton(drawableMine);
        imbMine2.getImageCell().expand().fill();
        table.add(imbMine2);

        ImageButton imbWind3 = new ImageButton(drawableWind);
        imbWind3.getImageCell().expand().fill();
        table.add(imbWind3);

        ImageButton imbMarket1 = new ImageButton(drawableMarket);
        imbMarket1.getImageCell().expand().fill();
        table.add(imbMarket1);

        table.row();
        table.add(btnRocket);

        ImageButton imbHouse6 = new ImageButton(drawableHouse);
        imbHouse6.getImageCell().expand().fill();
        table.add(imbHouse6);

        ImageButton imbWind4 = new ImageButton(drawableWind);
        imbWind4.getImageCell().expand().fill();
        table.add(imbWind4);

        ImageButton imbFactory2 = new ImageButton(drawableFactory);
        imbFactory2.getImageCell().expand().fill();
        table.add(imbFactory2);

        ImageButton imbBank1 = new ImageButton(drawableBank);
        imbBank1.getImageCell().expand().fill();
        table.add(imbBank1);

        menuImage.setVisible(false);

        table.setX(WIDTH + menuImage.getWidth()/2 + table.getPrefWidth()/5);
        table.setY(HEIGHT - table.getPrefHeight()/2);

        table.setDebug(false);

        btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BACK))));
        btnBack.setX(5);
        btnBack.setY(HEIGHT - btnBack.getPrefHeight());
        btnBack.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hexpert.setScreen(hexpert.levelSelectScreen);
                }
            }
        );

        btnReset = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_RESET))));
        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetGrid();
            }
        });

        btnUndo = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_UNDO))));
        btnUndo.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                undo();
            }
        });

        btnHelp = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HELP))));
        btnHelp.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.setX(table.getX() + (drawerOpen ? 900 : -900));
                drawerOpen = !drawerOpen;
            }
        });


        tableBtn = new Table();
        tableBtn.setY(HEIGHT - 150);
        tableBtn.setX(310);
        tableBtn.defaults().width(125).height(125).pad(15);

        btnBack.getImageCell().expand().fill();
        btnReset.getImageCell().expand().fill();
        btnUndo.getImageCell().expand().fill();
        btnHelp.getImageCell().expand().fill();

        objectivesButton = new TextButton(hexpert.i18NBundle.get("goals"), AssetLoader.getSkin());

        objectivesButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                objectivesButton.setChecked(false);
                objectiveDialog.setObjectives(map.getObjectives(), mapResult.getObjectivePassed());
                objectiveDialog.show(getStage());
            }
        });

        tableBtn.add(btnBack);
        tableBtn.add(btnReset);
        tableBtn.add(btnUndo);
        tableBtn.add(btnHelp);
        tableBtn.row();
        tableBtn.add(objectivesButton).colspan(4).width(500);

        getStage().addActor(table);
        getStage().addActor(tableBtn);

        MapUtils.adjustCamera(getCamera(), grid);
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
            hexpert.setScreen(hexpert.levelSelectScreen);
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

        if(map.scoreIsCalculated())
        {
            absBatch.begin();
            hexpert.getFont().draw(absBatch, hexpert.i18NBundle.format("score", score), 5, 25);
            hexpert.getFont().draw(absBatch, hexpert.i18NBundle.format("best", mapResult.getScore()), 5, 55);
            absBatch.end();
        }

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
            batch.begin();
            for (int i = 0; i < hexagons.length; i++) {
                HexGeometry hexGeo = hexagons[i].getHexGeometry();
                hexpert.getFont().draw(batch, Integer.toString(hexagons[i].getCoordinateSystem().toIndexed().getIndex()),
                        (float)hexGeo.getMiddlePoint().x, (float)hexGeo.getMiddlePoint().y);
            }

            batch.end();
        }
        moveEventManager.render(delta);
        updateScore();
        super.render(delta);

        if(!exitDialog.hasBeenShown() && numObjectivesPassedCurrent() == mapResult.getObjectivePassed().length)
        {
            exitDialog.setShown(true);
            exitDialog.show(getStage());
        }
    }

    public Hexpert getHexpert() {
        return hexpert;
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
        if(map.scoreIsCalculated())
        {
            score = 0;
            for(int i = 0; i < grid.getHexs().length; i++)
            {
                TileData data = (TileData) grid.getHexs()[i].getHexData();
                score+=data.getBuildingType().getScore() * data.getTileType().getMultiplier();
            }

            if(score > mapResult.getScore())
            {
                mapResult.setScore(score);
                savemap();
            }
        }
    }

    private void resetGrid()
    {
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            data.setBuildingType(map.getBuildingTypes()[i]);
            data.setBuildingTexture(hexpert.getTextureByBuilding(map.getBuildingTypes()[i]));
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

    public MoveEventManager getMoveEventManager() {
        return moveEventManager;
    }

    public void updateObjectives() {

        for(int i = 0; i < map.getObjectives().length; i++)
        {
            objectivePassed[i] = (map.getObjectives()[i].hasPassed(grid));
        }

        if(numObjectivesPassedCurrent() > numObjectivesPassedSave())
        {
            mapResult.setObjectivePassed(objectivePassed);
            savemap();
        }
    }

    private int numObjectivesPassedCurrent()
    {
        int n = 0;
        for(int i = 0; i < map.getObjectives().length; i++)
        {
            if(objectivePassed[i])
                n++;
        }

        return n;
    }

    private int numObjectivesPassedSave()
    {
        int n = 0;
        for(int i = 0; i < map.getObjectives().length; i++)
        {
            if(mapResult.getObjectivePassed()[i])
                n++;
        }

        return n;
    }

    private Objective getNextObjective()
    {
        for(int i = 0; i < map.getObjectives().length; i++)
        {
            if(!mapResult.getObjectivePassed()[i])
                return map.getObjectives()[i];
        }

        return null;
    }

    private void savemap()
    {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Gdx.files.local(mapResult.getMapId() + ".mapres").writeString(jsonSerializer.deepSerialize(mapResult), false);
    }

    public Map getMap() {
        return map;
    }
}
