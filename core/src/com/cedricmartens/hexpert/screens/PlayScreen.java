package com.cedricmartens.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.effect.BuildingAnimator;
import com.cedricmartens.hexpert.effect.GridEffect;
import com.cedricmartens.hexpert.effect.MoveEventManager;
import com.cedricmartens.hexpert.event.LevelCompleteDialog;
import com.cedricmartens.hexpert.event.misc.ActionDialog;
import com.cedricmartens.hexpert.gestures.PlayScreenGestureBehavior;
import com.cedricmartens.hexpert.map.Map;
import com.cedricmartens.hexpert.map.MapResult;
import com.cedricmartens.hexpert.map.MapUtils;
import com.cedricmartens.hexpert.map.Objective;
import com.cedricmartens.hexpert.misc.Action;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.misc.TextureData;
import com.cedricmartens.hexpert.social.Achievement;
import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.Rules;
import com.cedricmartens.hexpert.tile.TileData;
import com.cedricmartens.hexpert.tile.TileType;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONSerializer;

import static com.cedricmartens.hexpert.misc.Const.HEIGHT;
import static com.cedricmartens.hexpert.misc.Const.HEX_HEIGHT_RATIO;
import static com.cedricmartens.hexpert.misc.Const.WIDTH;
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
    public int numObjectivePassed;
    private ShaderProgram hintShader, removeShader, lockedShader, lightRedShader;
    public String mapName;
    private List<TileData> defaultBuildings;
    private List<TileData> lockedBuildings;
    private List<TileData> validBuildings;
    private LevelCompleteDialog exitDialog;
    private BuildingAnimator windAnimator;
    private GridEffect gridEffect;
    private float removeTime;
    private boolean isRemovingPressed;
    private boolean hasRemovedGrid;

    public PlayScreen(final Hexpert hexpert, final Map map, final MapResult result, final String mapName) {
        super(hexpert);
        this.hexpert = hexpert;
        removeTime = 0;
        isRemovingPressed = false;
        hasRemovedGrid = false;
        this.windAnimator = new BuildingAnimator(0.5f, 1, SPRITE_FOLDER + "wind.png", hexpert);
        this.mapName = mapName;
        this.map = map;
        mapResult = result;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        moveEventManager = new MoveEventManager(this);
        grid = map.build();
        exitDialog = new LevelCompleteDialog(score, mapName, hexpert.getSkin(), this);

        table = new Table();
        table.defaults().width(200).height(Const.HEIGHT/9).pad(5);
        int numberOfBuilding = getBuildingCountByLevel(mapName);
        for(int i = 1; i < BuildingType.values().length; i++)
        {
            Actor imgBuilding;
            if(numberOfBuilding >= i)
            {
                final BuildingType buildingType = BuildingType.values()[i];
                String path = SPRITE_FOLDER + buildingType.name().toLowerCase() + "_min.png";
                TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(path)));
                imgBuilding = new ImageButton(drawable);
                ImageButton imgButton = (ImageButton) imgBuilding;
                imgButton.getImageCell().expand().fill();
                imgButton.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        setSelection(buildingType);
                        hexpert.sounds.get("select").play(hexpert.masterVolume);
                    }
                });

            }else{
                imgBuilding = new Image();
            }

            table.add(imgBuilding);
            table.row();
        }


        btnRemove.addListener(new ClickListener()
          {
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  if(!hasRemovedGrid)
                  {
                      removeMode = !removeMode;
                      setSelection(null);
                  }

                  hasRemovedGrid = false;
                  removeTime = 0;
              }

              @Override
              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                  isRemovingPressed = true;
                  removeTime = 0;
                  return super.touchDown(event, x, y, pointer, button);
              }

              @Override
              public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                  super.touchUp(event, x, y, pointer, button);
                  isRemovingPressed = false;
              }
          }
        );

        table.setX(WIDTH - 100);
        table.setY(Const.HEIGHT - table.getPrefHeight()/2 - 5);

        table.setDebug(false);
        getStage().addActor(table);

        String vertexShader = Gdx.files.internal("shaders/defaultvertex.vs").readString();
        String hint = Gdx.files.internal("shaders/yellowTint.fs").readString();
        String rmv = Gdx.files.internal("shaders/redTint.fs").readString();
        String lckd = Gdx.files.internal("shaders/locked.fs").readString();
        String lightRed = Gdx.files.internal("shaders/lightRed.fs").readString();

        hintShader = new ShaderProgram(vertexShader, hint);
        if (!hintShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + hintShader.getLog());

        removeShader = new ShaderProgram(vertexShader, rmv);
        if(!removeShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + removeShader.getLog());

        lockedShader = new ShaderProgram(vertexShader, lckd);
        if(!lockedShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + lockedShader.getLog());

        lightRedShader = new ShaderProgram(vertexShader, lightRed);
        if(!lightRedShader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader : " + lightRedShader.getLog());

        this.defaultBuildings = new ArrayList<>();
        this.validBuildings = new ArrayList<>();
        this.lockedBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            BuildingType buildingType = data.getBuildingType();

            if(buildingType != BuildingType.NONE)
            {
                defaultBuildings.add(data);
                if(!Rules.isValid(data))
                {
                    lockedBuildings.add(data);
                }
            }
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

        updateScore();
        gridEffect = new GridEffect(grid, 0.1f, 0.75f);

        if(mapResult.getObjectivePassedCount() == map.getObjectives().length)
        {
            exitDialog.setShown(true);
        }

        gridEffect.setAction(getAction(mapName));
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

        if(isRemovingPressed)
        {
            removeTime += Gdx.graphics.getDeltaTime();
            if(removeTime >= 0.75f && !hasRemovedGrid)
            {
                hasRemovedGrid = true;
                resetGrid();
            }
        }

        windAnimator.tick(Gdx.graphics.getDeltaTime());
        gridEffect.tick(Gdx.graphics.getDeltaTime());

        batch.setProjectionMatrix(getCamera().combined);
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            drawHexagon(grid.getHexs()[i], batch);
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

        super.render(delta);

        if(!exitDialog.hasBeenShown()
                && numObjectivePassed == map.getObjectives().length)
        {
            this.exitDialog = new LevelCompleteDialog(score, mapName, hexpert.getSkin(), this);
            exitDialog.setShown(true);
            exitDialog.show(getStage());
        }
    }

    private void drawHexagon(Hexagon<TileData> hexagon, SpriteBatch batch)
    {

        Hexagon<TileData> hex = hexagon;

        if(removeMode && hex.getHexData().getBuildingType() != BuildingType.NONE
                && !defaultBuildings.contains(hex.getHexData()))
        {
            boolean isANeed = Rules.isANeed(hex.getHexData(), lockedBuildings);

            if(!isANeed)
                batch.setShader(removeShader);

        }
        else if(hexpert.config.isBuildHelp() && getSelection() != null
                && hex.getHexData().getTileType() != TileType.WATER && Rules.isValidPlacement(hex.getHexData(), getSelection()))
        {
            batch.setShader(hintShader);
        }

        int hexConst = 22;

        if(gridEffect.getActiveTiles().contains(hex))
        {
            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData().getTerrainTexture(),
                    (float)(middlePoint.x - grid.getStyle().getSize()),
                    (float)(gridEffect.getNewCoords().get(hex) - grid.getStyle().getSize()* Const.HEX_HEIGHT_RATIO) - hexConst,
                    (float)grid.getStyle().getSize()*2,
                    (float) ((float)grid.getStyle().getSize()*2 * Const.HEX_HEIGHT_RATIO) + hexConst);
        }else if(gridEffect.hasFallen(hex)){

            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData().getTerrainTexture(),
                    (float)(middlePoint.x - grid.getStyle().getSize()),
                    (float)(middlePoint.y - grid.getStyle().getSize()* Const.HEX_HEIGHT_RATIO) - hexConst,
                    (float)grid.getStyle().getSize()*2,
                    (float) ((float)grid.getStyle().getSize()*2 * Const.HEX_HEIGHT_RATIO) + hexConst);

        }
        batch.setShader(null);

        if(hex.getHexData().getBuildingTexture() != null)
        {

            if(removeMode && defaultBuildings.contains(hex.getHexData()))
                batch.setShader(lightRedShader);

            if(!validBuildings.contains(hex.getHexData()))
                batch.setShader(lockedShader);

            Texture buildingTexture = null;

            if(hex.getHexData().getBuildingType() == BuildingType.WIND)
            {
                buildingTexture = windAnimator.getTexture();
            }else{
                buildingTexture = hex.getHexData().getBuildingTexture();
            }

            float dimensions = (float) (grid.getStyle().getSize() / (HEX_HEIGHT_RATIO * HEX_HEIGHT_RATIO));
            if(gridEffect.getActiveTiles().contains(hex))
            {
                Point middlePoint = hex.getHexGeometry().getMiddlePoint();
                batch.draw(buildingTexture,
                        (float)(middlePoint.x - dimensions/2),
                        (float)(gridEffect.getNewCoords().get(hex) - dimensions/2),
                        dimensions, dimensions);

            }else if(gridEffect.hasFallen(hex))
            {
                Point middlePoint = hex.getHexGeometry().getMiddlePoint();
                batch.draw(buildingTexture,
                        (float)(middlePoint.x - dimensions/2),
                        (float)(middlePoint.y - dimensions/2),
                        dimensions, dimensions);
            }
            batch.setShader(null);
        }

    }

    public int numberOfObjectivePassed(Objective[] objectives, HexMap<TileData> grid)
    {
        int n = 0;

        for(int i = 0; i < objectives.length; i++)
        {
            if(objectives[i].hasPassed(grid))
                n++;
        }

        return n;
    }

    private Objective getNextObjective(int numObjectivePassed, Objective[] objectives)
    {
        if(numObjectivePassed >= objectives.length)
            return null;

        return objectives[numObjectivePassed];
    }

    private Texture getNextObjectiveTexture(int numObjectivePassed, Objective[] objectives)
    {
        if(numObjectivePassed >= objectives.length)
            return null;

        int diff = objectives.length - numObjectivePassed;

        switch (diff)
        {
            case 1 :
                return hexpert.assetManager.get(TextureData.TEXTURE_HEXGOLD, Texture.class);
            case 2 :
                return hexpert.assetManager.get(TextureData.TEXTURE_HEXSILVER, Texture.class);
            case 3:
                return hexpert.assetManager.get(TextureData.TEXTURE_HEXBRONZE, Texture.class);
            default:
                return hexpert.assetManager.get(TextureData.TEXTURE_GRASS, Texture.class);
        }
    }

    public Hexpert getHexpert() {
        return super.getHexpert();
    }

    public boolean canPlaceBuildings()
    {
        return !gridEffect.isActive();
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

    public void updateScore()
    {
        updateValidBuildings();

        score = map.getScore(grid);
        int objectivePassedBest = mapResult.getObjectivePassedCount();
        numObjectivePassed = numberOfObjectivePassed(map.getObjectives(), grid);

        if(map.scoreIsCalculated())
        {
            if(score > mapResult.getScore() && numObjectivePassed >= objectivePassedBest)
            {
                mapResult.setBuildingFromGrid(grid);
                mapResult.setScore(score);
                mapResult.updateObjectives(map.getObjectives(), grid);

                saveResult();

                if(numObjectivePassed == map.getObjectives().length)
                {    try{
                        hexpert.playServices.submitScore(score, mapName);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(numObjectivePassed >= objectivePassedBest)
        {
            mapResult.setBuildingFromGrid(grid);
            mapResult.updateObjectives(map.getObjectives(), grid);
            saveResult();
        }


        Objective nextObjective = getNextObjective(mapResult.getObjectivePassedCount(), map.getObjectives());

        if(nextObjective == null)
        {
            lblNextObjective.setText("");
            nextObjectiveImage.setVisible(false);
        }
        else{
            updateTableButton(nextObjective);
        }
    }

    public void updateTableButton(Objective nextObjective)
    {
        tableBtn.clearChildren();
        tableBtn.add(btnMore);
        tableBtn.add(btnRemove);
        tableBtn.add(btnHelp);
        tableBtn.row();
        lblNextObjective = new Label("", hexpert.getSkin());
        lblNextObjective.setWrap(true);
        lblNextObjective.setText(nextObjective.toString(hexpert.i18NBundle));
        nextObjectiveImage = new Image(getNextObjectiveTexture(mapResult.getObjectivePassedCount(), map.getObjectives()));
        tableBtn.add(nextObjectiveImage).width(96).height(96);
        tableBtn.add(lblNextObjective).width(lblNextObjective.getPrefWidth()).left();
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

                    TileType tileType = data.getTileType();
                    if(tileType == TileType.FOREST)
                    {
                        data.setTerrainTexture((Texture) hexpert.assetManager.get(TEXTURE_FOREST));
                    }

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

    public Pixmap getMapScreenShot()
    {

        float oldCameraZoom = getCamera().zoom;
        float oldCameraX = getCamera().position.x;
        float oldCameraY = getCamera().position.y;

        MapUtils.adjustCamera(getCamera(), grid);

        final FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, false);

        fbo.begin();
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            drawHexagon(grid.getHexs()[i], batch);
        }

        batch.end();
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, WIDTH, HEIGHT);
        fbo.end();

        //fbo.dispose();

        getCamera().zoom = oldCameraZoom;
        getCamera().position.x = oldCameraX;
        getCamera().position.y = oldCameraY;

        return pixmap;
    }

    public boolean isRemovable(TileData data)
    {
        if(defaultBuildings.contains(data))
            return false;

        return !Rules.isANeed(data, lockedBuildings);
    }

    public String getMapName() {
        return mapName;
    }

    public List<TileData> getDefaultBuildings() {
        return defaultBuildings;
    }

    public List<TileData> getLockedBuildings() {
        return lockedBuildings;
    }

    public List<TileData> getValidBuildings() {
        return validBuildings;
    }
}
