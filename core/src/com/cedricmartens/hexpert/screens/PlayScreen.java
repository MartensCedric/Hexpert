package com.cedricmartens.hexpert.screens;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.misc.Action;
import com.cedricmartens.hexpert.event.misc.ActionDialog;
import com.cedricmartens.hexpert.event.LevelCompleteDialog;
import com.cedricmartens.hexpert.effect.MoveEventManager;
import com.cedricmartens.hexpert.gestures.PlayScreenGestureBehavior;
import com.cedricmartens.hexpert.social.Achievement;
import com.cedricmartens.hexpert.map.Map;
import com.cedricmartens.hexpert.map.MapResult;
import com.cedricmartens.hexpert.map.MapUtils;
import com.cedricmartens.hexpert.map.Objective;
import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.Dependency;
import com.cedricmartens.hexpert.tile.Rules;
import com.cedricmartens.hexpert.tile.TileData;
import com.cedricmartens.hexpert.tile.TileType;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONSerializer;

import static com.cedricmartens.hexpert.misc.TextureData.SPRITE_FOLDER;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST_CUT;
import static com.cedricmartens.hexpert.social.Achievement.GREAT_ESCAPE;

/**
 * Created by 1544256 on 2017-04-26.
 */

public class PlayScreen extends PlayStage
{
    private HexMap<TileData> grid;
    public Map map;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GestureDetector detector;
    private PlayScreenGestureBehavior behavior;
    private int score = 0;
    private MoveEventManager moveEventManager;
    private boolean debug = false;
    public MapResult mapResult;
    private boolean[] objectivePassed;
    private ShaderProgram hintShader, removeShader, lockedShader;
    public String mapName;
    private List<TileData> defaultBuildings;
    private List<TileData> validBuildings;
    private LevelCompleteDialog exitDialog;

    public PlayScreen(final Hexpert hexpert, final Map map, MapResult result, final String mapName) {
        super(hexpert);

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
        exitDialog = new LevelCompleteDialog(score, mapName, hexpert.getSkin(), hexpert);


        table = new Table();
        table.defaults().width(200).height(Const.HEIGHT/9).pad(5);
        int numberOfBuilding = getBuildingCountForMap(mapName);
        for(int i = 1; i < BuildingType.values().length; i++)
        {
            Actor imgBuilding;
            if(numberOfBuilding >= i)
            {
                final BuildingType buildingType = BuildingType.values()[i];
                String path = SPRITE_FOLDER + buildingType.name().toLowerCase() + ".png";
                TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(path)));
                imgBuilding = new ImageButton(drawable);
                ImageButton imgButton = (ImageButton) imgBuilding;
                imgButton.getImageCell().expand().fill();
                imgButton.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        setSelection(buildingType);
                        hexpert.sounds.get("select").play();
                    }
                });

            }else{
                imgBuilding = new Image();
            }

            table.add(imgBuilding);
            table.row();
        }

        table.setX(Const.WIDTH - 100);
        table.setY(Const.HEIGHT - table.getPrefHeight()/2 - 5);

        table.setDebug(false);
        getStage().addActor(table);

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

        this.defaultBuildings = new ArrayList<>();
        this.validBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            if(((TileData)grid.getHexs()[i].getHexData()).getBuildingType() != BuildingType.NONE)
                defaultBuildings.add((TileData) grid.getHexs()[i].getHexData());
        }

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
            if(data.getTileType() == TileType.FOREST)
            {
                data.setTerrainTexture((Texture) hexpert.assetManager.get(data.getBuildingType() == BuildingType.NONE ?
                        TEXTURE_FOREST : TEXTURE_FOREST_CUT));
            }else {
                data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            }

            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);
        }

        setMultiplexer();
        MapUtils.adjustCamera(getCamera(), grid);
        getCamera().zoom *= 0.6;
        getCamera().translate(0, 25);
        getCamera().update();

        doAction(mapName);
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
                    (float)(middlePoint.y - grid.getStyle().getSize()* Const.HEX_HEIGHT_RATIO) - 24,
                    (float)grid.getStyle().getSize()*2,
                    (float) ((float)grid.getStyle().getSize()*2 * Const.HEX_HEIGHT_RATIO) + 24);

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
            this.exitDialog = new LevelCompleteDialog(score, mapName, hexpert.getSkin(), hexpert);
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

    public void resetGrid()
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
