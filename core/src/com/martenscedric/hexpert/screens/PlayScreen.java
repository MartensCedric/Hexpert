package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.event.Action;
import com.martenscedric.hexpert.event.ActionDialog;
import com.martenscedric.hexpert.event.LevelComplete;
import com.martenscedric.hexpert.env.MoveEventManager;
import com.martenscedric.hexpert.event.ObjectiveDialog;
import com.martenscedric.hexpert.event.OptionDialog;
import com.martenscedric.hexpert.gestures.PlayScreenGestureBehavior;
import com.martenscedric.hexpert.google.Achievement;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.misc.Rules;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

import java.util.Stack;

import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.google.Achievement.GREAT_ESCAPE;
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
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_OPTIONS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WIND;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen extends PlayStage
{
    private Hexpert hexpert;
    private HexMap<TileData> grid;
    private Map map;
    private SpriteBatch batch;
    private SpriteBatch absBatch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private int score = 0;
    private MoveEventManager moveEventManager;
    private boolean debug = false;
    private MapResult mapResult;
    private BuildingType selection;
    private ImageButton selectedButton;
    private boolean[] objectivePassed;
    private ShaderProgram hintShader;
    private String mapName;

    private Stack<TileData> placementHistory = new Stack<TileData>();

    public PlayScreen(final Hexpert hexpert, final Map map, MapResult result, String mapName) {
        super(hexpert);

        btnFarm.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    selection = BuildingType.FARM;
                                    selectedButton = btnFarm;
                                }
                            }
        );

        btnHouse.addListener(new ClickListener()
                             {
                                 @Override
                                 public void clicked(InputEvent event, float x, float y) {
                                     selection = BuildingType.HOUSE;
                                     selectedButton = btnHouse;
                                 }
                             }
        );

        btnMine.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    selection = BuildingType.MINE;
                                    selectedButton = btnMine;
                                }
                            }
        );

        btnWind.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    selection = BuildingType.WIND;
                                    selectedButton = btnWind;
                                }
                            }
        );

        btnFactory.addListener(new ClickListener()
                               {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       selection = BuildingType.FACTORY;
                                       selectedButton = btnFactory;
                                   }
                               }
        );

        btnMarket.addListener(new ClickListener()
                              {
                                  @Override
                                  public void clicked(InputEvent event, float x, float y) {
                                      selection = BuildingType.MARKET;
                                      selectedButton = btnMarket;
                                  }
                              }
        );

        btnBank.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    selection = BuildingType.BANK;
                                    selectedButton = btnBank;
                                }
                            }
        );

        btnRocket.addListener(new ClickListener()
                              {
                                  @Override
                                  public void clicked(InputEvent event, float x, float y) {
                                      selection = BuildingType.ROCKET;
                                      selectedButton = btnRocket;
                                  }
                              }
        );

        btnUndo.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                undo();
            }
        });

        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetGrid();
            }
        });

        objectivesButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                objectivesButton.setChecked(false);
                objectiveDialog.setObjectives(map.getObjectives(), mapResult.getObjectivePassed());
                objectiveDialog.show(getStage());
            }
        });

        this.hexpert = hexpert;
        this.mapName = mapName;
        this.map = map;
        mapResult = result;
        this.batch = new SpriteBatch();
        this.absBatch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        moveEventManager = new MoveEventManager(this);
        grid = map.build();
        objectivePassed = new boolean[map.getObjectives().length];

        String vertexShader = Gdx.files.internal("shaders/defaultvertex.vs").readString();
        String hint = Gdx.files.internal("shaders/hint.fs").readString();
        hintShader = new ShaderProgram(vertexShader, hint);
        if (!hintShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + hintShader.getLog());

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);
        }

        setMultiplexer();
        MapUtils.adjustCamera(getCamera(), grid);
        getCamera().zoom *= 0.6;
        getCamera().translate(0, 25);
        getCamera().update();
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
        if(Math.abs(getCamera().position.x) > 5000
                || Math.abs(getCamera().position.y) > 5000)
            hexpert.playServices.unlockAchievement(GREAT_ESCAPE);

        batch.setProjectionMatrix(getCamera().combined);
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];

            if(hexpert.config.isBuildHelp() && getSelection() != null
                    && hex.getHexData().getTileType() != TileType.WATER && Rules.isValid(hex.getHexData(), getSelection()))
            {
                batch.setShader(hintShader);
            }

            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData().getTerrainTexture(),
                    (float)(middlePoint.x - grid.getStyle().getSize()),
                    (float)(middlePoint.y - grid.getStyle().getSize()*HEX_HEIGHT_RATIO) - 24,
                    (float)grid.getStyle().getSize()*2,
                    (float) ((float)grid.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

            batch.setShader(null);
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
            this.exitDialog = new LevelComplete(hexpert.getSkin(), hexpert);
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
                saveResult();
            }
        }
    }

    private void resetGrid()
    {
        if(placementHistory.size() > 0) {

            Label label = new Label(hexpert.i18NBundle.get("confirm_reset"), hexpert.getSkin());
            ActionDialog actionDialog = new ActionDialog(label, new Action(){

                @Override
                public void doAction()
                {
                    for (int i = 0; i < grid.getHexs().length; i++) {
                        TileData data = (TileData) grid.getHexs()[i].getHexData();
                        data.setBuildingType(map.getBuildingTypes()[i]);
                        data.setBuildingTexture(hexpert.getTextureByBuilding(map.getBuildingTypes()[i]));
                    }

                    setSelection(null);
                    placementHistory.clear();
                    updateScore();
                }
            }, hexpert.i18NBundle, hexpert.getSkin());

            actionDialog.show(getStage());
        }
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
            saveResult();
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

    private void saveResult()
    {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Gdx.files.local(mapName + ".mapres").writeString(jsonSerializer.deepSerialize(mapResult), false);
    }

    public Map getMap() {
        return map;
    }

    public void checkAchievements()
    {
        int numBanks = 0;
        for(int i = 0; i < grid.getHexs().length; i++)
        {

            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();

            if(data.getBuildingType() == BuildingType.ROCKET)
            {
                hexpert.playServices.unlockAchievement(Achievement.TO_SPACE);

                if(data.getTileType() == TileType.FOREST)
                    hexpert.playServices.unlockAchievement(Achievement.EFFICIENT_ROCKET);

                for(int j = 0; j < hex.getNeighbors().size(); j++)
                {
                    TileData neighborData = hex.getNeighbors().get(j).getHexData();
                    if(neighborData.getBuildingType() == BuildingType.ROCKET)
                        hexpert.playServices.unlockAchievement(Achievement.SPACE_RACE);
                }
            }

            if(data.getBuildingType() == BuildingType.BANK)
                numBanks++;

            if(data.getBuildingType().getScore() < 0
                    && data.getTileType() == TileType.FOREST)
            {
                hexpert.playServices.unlockAchievement(Achievement.DOUBLE_THE_NEGATIVE);
            }

            if(data.getBuildingType().getScore() > 0
                    && data.getTileType() == TileType.SAND)
            {
                hexpert.playServices.unlockAchievement(Achievement.USELESS_PROPERTY);
            }
        }

        if(numBanks >= 3)
            hexpert.playServices.unlockAchievement(Achievement.TOO_BIG_TO_FAIL);
    }
}
