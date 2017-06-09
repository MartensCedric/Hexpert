package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.event.Action;
import com.martenscedric.hexpert.event.ActionDialog;
import com.martenscedric.hexpert.event.LevelComplete;
import com.martenscedric.hexpert.effect.MoveEventManager;
import com.martenscedric.hexpert.gestures.PlayScreenGestureBehavior;
import com.martenscedric.hexpert.social.Achievement;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.tile.Dependency;
import com.martenscedric.hexpert.tile.Rules;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.social.Achievement.GREAT_ESCAPE;
import static com.martenscedric.hexpert.misc.Const.HEX_HEIGHT_RATIO;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen extends PlayStage
{
    private HexMap<TileData> grid;
    private Map map;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private int score = 0;
    private MoveEventManager moveEventManager;
    private boolean debug = false;
    private MapResult mapResult;
    private boolean[] objectivePassed;
    private ShaderProgram hintShader, removeShader, lockedShader;
    private String mapName;
    private List<TileData> defaultBuildings;
    private List<TileData> validBuildings;

    public PlayScreen(final Hexpert hexpert, final Map map, MapResult result, final String mapName) {
        super(hexpert);
        btnFarm.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    setSelection(BuildingType.FARM);
                                    hexpert.sounds.get("select").play();
                                }
                            }
        );

        btnHouse.addListener(new ClickListener()
                             {
                                 @Override
                                 public void clicked(InputEvent event, float x, float y) {
                                     setSelection(BuildingType.HOUSE);
                                     hexpert.sounds.get("select").play();
                                 }
                             }
        );

        btnMine.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    setSelection(BuildingType.MINE);
                                    hexpert.sounds.get("select").play();
                                }
                            }
        );

        btnWind.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    setSelection(BuildingType.WIND);
                                    hexpert.sounds.get("select").play();
                                }
                            }
        );

        btnFactory.addListener(new ClickListener()
                               {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       setSelection(BuildingType.FACTORY);
                                       hexpert.sounds.get("select").play();
                                   }
                               }
        );

        btnMarket.addListener(new ClickListener()
                              {
                                  @Override
                                  public void clicked(InputEvent event, float x, float y) {
                                      setSelection(BuildingType.MARKET);
                                      hexpert.sounds.get("select").play();
                                  }
                              }
        );

        btnBank.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    setSelection(BuildingType.BANK);
                                    hexpert.sounds.get("select").play();
                                }
                            }
        );

        btnRocket.addListener(new ClickListener()
                              {
                                  @Override
                                  public void clicked(InputEvent event, float x, float y) {
                                      setSelection(BuildingType.ROCKET);
                                      hexpert.sounds.get("select").play();
                                  }
                              }
        );

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

        btnLeaderboard.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try{
                    hexpert.playServices.showLeaderboardUI(mapName);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        this.hexpert = hexpert;
        this.mapName = mapName;
        this.map = map;
        mapResult = result;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        moveEventManager = new MoveEventManager(this);
        grid = map.build();
        objectivePassed = new boolean[map.getObjectives().length];

        String vertexShader = Gdx.files.internal("shaders/defaultvertex.vs").readString();
        String hint = Gdx.files.internal("shaders/yellowTint.fs").readString();
        String rmv = Gdx.files.internal("shaders/redTint.fs").readString();
        String lckd = Gdx.files.internal("shaders/locked.fs").readString();

        hintShader = new ShaderProgram(vertexShader, hint);
        if (!hintShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + hintShader.getLog());

        removeShader = new ShaderProgram(vertexShader, rmv);
        if(!removeShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + removeShader.getLog());

        lockedShader = new ShaderProgram(vertexShader, lckd);
        if(!lockedShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + lockedShader.getLog());

        BuildingType[] buildingTypes = mapResult.getBuildings();

        if(buildingTypes != null)
        {
            for(int i = 0; i < grid.getHexs().length; i++)
            {
                TileData data = (TileData) grid.getHexs()[i].getHexData();
                data.setBuildingType(buildingTypes[i]);
            }
        }

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);
        }

        this.defaultBuildings = new ArrayList<>();
        this.validBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            if(((TileData)grid.getHexs()[i].getHexData()).getBuildingType() != BuildingType.NONE)
                defaultBuildings.add((TileData) grid.getHexs()[i].getHexData());
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

            if(removeMode && !defaultBuildings.contains(hex.getHexData()))
            {
                Dependency dependency = Rules.getDependencyLevel(hex.getHexData());

                if(dependency == Dependency.INDEPENDENT)
                    batch.setShader(removeShader);
                else if (dependency == Dependency.PARTIALLY)
                    batch.setShader(hintShader);

            }
            else if(hexpert.config.isBuildHelp() && getSelection() != null
                    && hex.getHexData().getTileType() != TileType.WATER && Rules.isValidPlacement(hex.getHexData(), getSelection()))
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
                if(!validBuildings.contains(hex.getHexData()))
                    batch.setShader(lockedShader);

                Point middlePoint = hex.getHexGeometry().getMiddlePoint();
                batch.draw(hex.getHexData().getBuildingTexture(),
                        (float)(middlePoint.x - grid.getStyle().getSize()/2),
                        (float)(middlePoint.y - grid.getStyle().getSize()/2),
                        (float)grid.getStyle().getSize(),
                        (float)grid.getStyle().getSize());
                batch.setShader(null);
            }
        }
        batch.end();

        if(map.scoreIsCalculated())
        {
            Label lblScore = (Label) tableScore.getChildren().get(0);
            Label lblBest = (Label) tableScore.getChildren().get(1);
            lblScore.setText(hexpert.i18NBundle.format("score", score));
            lblBest.setText(hexpert.i18NBundle.format("best", mapResult.getScore()));
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
        return super.getHexpert();
    }

    public HexMap<TileData> getGrid() {
        return grid;
    }

    public BuildingType getSelection() {
        return selection;
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

    private void updateValidBuildings()
    {
        validBuildings = Rules.getValidBuildings(grid);
    }

    private void updateScore()
    {
        updateValidBuildings();
        if(map.scoreIsCalculated())
        {
            score = 0;
            for(int i = 0; i < validBuildings.size(); i++)
            {
                TileData data = validBuildings.get(i);
                score+=data.getBuildingType().getScore() * data.getTileType().getMultiplier();
            }

            if(score > mapResult.getScore())
            {
                mapResult.setScore(score);
                BuildingType[] buildingTypes = new BuildingType[grid.getHexs().length];
                for(int i = 0; i < grid.getHexs().length; i++)
                {
                    buildingTypes[i] = ((TileData) grid.getHexs()[i].getHexData()).getBuildingType();
                }
                mapResult.setBuildings(buildingTypes);

                saveResult();
                try{
                    hexpert.playServices.submitScore(score, mapName);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetGrid()
    {
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
                updateScore();
            }
        }, hexpert.i18NBundle, hexpert.getSkin(), hexpert);

            actionDialog.show(getStage());
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

    public boolean isRemovable(TileData data)
    {
        if(defaultBuildings.contains(data))
            return false;

        return Rules.getDependencyLevel(data) != Dependency.DEPENDENT;
    }
}
