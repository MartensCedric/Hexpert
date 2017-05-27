package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexStyle;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.hexagon.HexagonOrientation;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.gestures.LevelSelectGesture;
import com.martenscedric.hexpert.google.Achievement;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapLoadException;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.tile.TileData;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexpert.misc.Const.WIDTH;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ACHIEVEMENTS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CORRECT;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_GRASS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXBRONZE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXGOLD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXSILVER;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_LEFT;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RIGHT;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table objectiveTable;
    private Table statsTable;
    private int levelsToDisplay = 5;
    private int totalLevels;
    public int currentWorld = 1;
    public final Hexpert hexpert;
    private int levelSelect = 1;
    private HexMap<TileData> grid;
    private HexMap<Texture> gridLvlSelect;
    private SpriteBatch batch, uiBatch, displayBatch;
    private MapResult result;
    private OrthographicCamera uiCamera, displayLevelCamera;
    private int hexCount = 0;
    private ImageButton btnLeft, btnRight;
    private boolean debug = false;
    private Rectangle mapCollision;
    private ShapeRenderer shapeRenderer;
    private LevelSelectGesture behavior;
    private GestureDetector detector;
    private ShaderProgram shdDark;
    private Label lblHexCount,lblHighScore;

    public LevelSelectScreen(final Hexpert hexpert)
    {
        super();
        this.totalLevels = hexpert.levelIndex.size();
        this.hexpert = hexpert;
        this.batch = new SpriteBatch();
        this.uiBatch = new SpriteBatch();
        this.displayBatch = new SpriteBatch();
        this.uiCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.displayLevelCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.objectiveTable = new Table();
        objectiveTable.setX(100);
        objectiveTable.setY(850);
        objectiveTable.defaults().pad(20);
        getStage().addActor(objectiveTable);
        hexCount = getHexCount();

        String vertexShader = Gdx.files.internal("shaders/defaultvertex.vs").readString();
        String darkShader = Gdx.files.internal("shaders/darkness.fs").readString();
        shdDark = new ShaderProgram(vertexShader, darkShader);
        if (!shdDark.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shdDark.getLog());

        getCamera().translate(300, 350);
        getCamera().update();

        btnLeft = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_LEFT))));
        btnRight = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_RIGHT))));
        btnLeft.getImageCell().expand().fill();

        btnLeft.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(currentWorld > 1)
                {
                    currentWorld--;
                    updateLevelSelectGrid();

                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);
                }
            }
        });

        btnRight.getImageCell().expand().fill();
        btnRight.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(currentWorld * levelsToDisplay < totalLevels)
                {
                    currentWorld++;
                    updateLevelSelectGrid();

                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);

                }
            }
        });

        createLevelSelectGrid();

        statsTable = new Table();
        statsTable.defaults().width(160).height(160);

        ImageButton btnGoldHex = new ImageButton(
                new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_CORRECT))));

        ImageButton btnAchievements = new ImageButton(
                new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_ACHIEVEMENTS))));

        btnAchievements.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hexpert.playServices.showAchievementsUI();
            }
        });

        lblHexCount = new Label(String.format("x%d", hexCount), hexpert.getSkin());
        statsTable.add(btnGoldHex).width(100).height(100);
        statsTable.add(lblHexCount).width(80);
        statsTable.add(btnAchievements);
        statsTable.row();
        lblHighScore = new Label("", hexpert.getSkin());
        statsTable.add(lblHighScore).colspan(3);

        btnGoldHex.getImageCell().expand().fill();
        btnAchievements.getImageCell().expand().fill();

        statsTable.setX(1650);
        statsTable.setY(885);

        getStage().addActor(statsTable);
    }

    @Override
    public void render(float delta) {

        btnLeft.setVisible(currentWorld > 1);
        btnRight.setVisible(currentWorld * levelsToDisplay < totalLevels);

        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();

        for(int i = gridLvlSelect.getHexs().length - 1; i >= 0; i--)
        {
            if(i == gridLvlSelect.getHexs().length - 1 && isLastWorld(currentWorld)) continue;
            if(i % 2 == 0) continue;

            batch.setShader(levelSelect == (currentWorld - 1) * levelsToDisplay + i ? shdDark : null);

            Hexagon<Texture> hex = gridLvlSelect.getHexs()[i];
            Point p = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData(),
                    (float)(p.x - gridLvlSelect.getStyle().getSize()),
                    (float)(p.y - gridLvlSelect.getStyle().getSize() * HEX_HEIGHT_RATIO),
                    (float)gridLvlSelect.getStyle().getSize()*2,
                    (float)((float)gridLvlSelect.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

            if(i > 0 && i < gridLvlSelect.getHexs().length - 1)
            {
                hexpert.getFont().draw(batch,
                        Integer.toString((currentWorld - 1) * levelsToDisplay + i),
                        (int)p.x - 10, (int)p.y + 30);
            }
        }

        for(int i = gridLvlSelect.getHexs().length - 1; i >= 0; i--)
        {
            if(i == 0 && currentWorld == 1)continue;
            if(i % 2 == 1) continue;
            batch.setShader(levelSelect == (currentWorld - 1) * levelsToDisplay + i ? shdDark : null);
            Hexagon<Texture> hex = gridLvlSelect.getHexs()[i];
            Point p = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData(),
                    (float)(p.x - gridLvlSelect.getStyle().getSize()),
                    (float)(p.y - gridLvlSelect.getStyle().getSize() * HEX_HEIGHT_RATIO),
                    (float)gridLvlSelect.getStyle().getSize()*2,
                    (float)((float)gridLvlSelect.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

            if(i > 0 && i < gridLvlSelect.getHexs().length - 1)
            {
                hexpert.getFont().draw(batch,
                        Integer.toString((currentWorld - 1) * levelsToDisplay + i),
                        (int)p.x - 10, (int)p.y + 30);
            }
        }
        batch.setShader(null);

        batch.end();
        displayBatch.setProjectionMatrix(displayLevelCamera.combined);
        displayBatch.begin();
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];

            Point middlePoint = hex.getHexGeometry().getMiddlePoint();
            displayBatch.draw(hex.getHexData().getTerrainTexture(),
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
                displayBatch.draw(hex.getHexData().getBuildingTexture(),
                        (float)(middlePoint.x - grid.getStyle().getSize()/2),
                        (float)(middlePoint.y - grid.getStyle().getSize()/2),
                        (float)grid.getStyle().getSize(),
                        (float)grid.getStyle().getSize());
            }
        }

        displayBatch.end();

        if(debug)
        {
            double maxHeight = -1, maxWidth = -1;
            double minHeight = Double.MAX_VALUE, minWidth = Double.MAX_VALUE;
            for(int i = 0; i < grid.getHexs().length; i++)
            {
                Hexagon<TileData> hex = grid.getHexs()[i];
                TileData data = hex.getHexData();
                data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
                data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
                hex.setHexData(data);

                for(Point p : hex.getHexGeometry().getPoints())
                {
                    if(p.y < minHeight)
                        minHeight = p.y;

                    if(p.y > maxHeight)
                        maxHeight = p.y;

                    if(p.x < minWidth)
                        minWidth = p.x;

                    if(p.x > maxWidth)
                        maxWidth = p.x;
                }
            }

            double deltaX = maxWidth - minWidth;
            double middleX = minWidth + deltaX/2;

            double deltaY = maxHeight - minHeight;
            double middleY = minHeight + deltaY/2;

            shapeRenderer.setProjectionMatrix(displayLevelCamera.combined);
            shapeRenderer.begin();
            shapeRenderer.line((float) minWidth, -HEIGHT, (float) minWidth, HEIGHT, Color.BLACK, Color.BLACK);
            shapeRenderer.line((float) maxWidth, -HEIGHT, (float) maxWidth, HEIGHT, Color.BLACK, Color.BLACK);
            shapeRenderer.line(-WIDTH, (float) minHeight, WIDTH, (float) minHeight, Color.BLACK, Color.BLACK);
            shapeRenderer.line(-WIDTH, (float) maxHeight, WIDTH, (float) maxHeight, Color.BLACK, Color.BLACK);

            shapeRenderer.line((float)middleX, -HEIGHT, (float)middleX, HEIGHT, Color.RED, Color.RED);
            shapeRenderer.line(-WIDTH, (float) middleY, WIDTH, (float) middleY, Color.RED, Color.RED);

            shapeRenderer.end();
        }

        super.render(delta);
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

    @Override
    public void show() {
        super.show();
        selectLevel(levelSelect);
        updateLevelSelectGrid();
        hexCount = getHexCount();
        lblHexCount.setText(String.format("x%d", hexCount));
        setMultiplexer();
    }

    private Map loadDisplayLevel()
    {
        String mapLoc = Gdx.files.internal("maps/" + hexpert.levelIndex.get(levelSelect) + ".hexmap").readString();
        Map map = new JSONDeserializer<Map>().deserialize(mapLoc);
        grid = map.build();

        String mapString = hexpert.levelIndex.get(levelSelect) + ".mapres";
        if(!Gdx.files.local(mapString).exists())
        {
            result = new MapResult(levelSelect, 0, new boolean[map.getObjectives().length]);
            JSONSerializer jsonSerializer = new JSONSerializer();
            Gdx.files.local(mapString).writeString(jsonSerializer.deepSerialize(result), false);
        }else{
            JSONDeserializer<MapResult> deserializer = new JSONDeserializer<>();
            result = deserializer.deserialize(Gdx.files.local(mapString).readString());
        }

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
        }

        displayLevelCamera.position.setZero();
        MapUtils.adjustCamera(displayLevelCamera, grid);
        displayLevelCamera.translate(0, -75);
        displayLevelCamera.update();
        setMapCollision();

        return map;
    }

    public void selectLevel(int levelID)
    {
        try{
            levelSelect = levelID;
            Map map = loadDisplayLevel();
            TextureRegionDrawable textureBad = new TextureRegionDrawable(
                    new TextureRegion(hexpert.assetManager.get(TEXTURE_BAD, Texture.class)));

            TextureRegionDrawable textureCorrect = new TextureRegionDrawable(
                    new TextureRegion(hexpert.assetManager.get(TEXTURE_CORRECT, Texture.class)));

            if(result.getScore() > 0)
                lblHighScore.setText(hexpert.i18NBundle.format("best", result.getScore()));
            else lblHighScore.setText("");

            float y = 750;

            switch (result.getObjectivePassed().length)
            {
                case 1 :
                    y = 1000;
                    break;
                case 2 :
                    y = 800;
                    break;
                case 3 :
                    y = 860;
                    break;
            }

            objectiveTable.setY(y);


            objectiveTable.clearChildren();


            for(int i = 0; i < map.getObjectives().length; i++)
            {
                ImageButton imgBad = new ImageButton(textureBad);
                ImageButton imgCorrect = new ImageButton(textureCorrect);

                imgBad.getImageCell().expand().fill();
                imgCorrect.getImageCell().expand().fill();

                objectiveTable.add(result.getObjectivePassed()[i] ? imgCorrect : imgBad).width(100).height(100);

                Label lblDesc = new Label(map.getObjectives()[i].toString(hexpert.i18NBundle), hexpert.getSkin());
                lblDesc.setWidth(200);
                lblDesc.setWrap(true);

                objectiveTable.add(lblDesc);
                objectiveTable.row();
            }

        }catch (Exception e)
        {

        }
    }

    private int getHexCount()
    {
        int total = 0;
        for(int i = 1; i <= totalLevels; i++)
        {
            if(Gdx.files.local(hexpert.levelIndex.get(i) + ".mapres").exists())
            {
                String mapResLoc = Gdx.files.local(hexpert.levelIndex.get(i) + ".mapres").readString();
                MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapResLoc);

                for(int j = 0; j < mapResult.getObjectivePassed().length; j++)
                {
                    total += mapResult.getObjectivePassed()[j] ? 1 : 0;
                }
            }else{
                String mapString = Gdx.files.internal("maps/" + hexpert.levelIndex.get(i) + ".hexmap").readString();
                Map map = new JSONDeserializer<Map>().deserialize(mapString);
                MapResult res = new MapResult(i, 0, new boolean[map.getObjectives().length]);
                JSONSerializer jsonSerializer = new JSONSerializer();
                Gdx.files.local(hexpert.levelIndex.get(i) + ".mapres").writeString(jsonSerializer.deepSerialize(res), false);
            }
        }

        return total;
    }

    private boolean[] getObjectivePassed(int level)
    {
        FileHandle mapresFile = Gdx.files.local(hexpert.levelIndex.get(level) + ".mapres");

        if(mapresFile.exists())
        {
            MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapresFile.readString());
            return mapResult.getObjectivePassed();
        }else{
            throw new MapLoadException(String.format("Can't find %s.mapres", hexpert.levelIndex.get(level)));
        }
    }

    private void createLevelSelectGrid()
    {
        HexFreeShapeBuilder<Texture> builder = new HexFreeShapeBuilder<>();
        builder.setStyle(new HexStyle(100, HexagonOrientation.FLAT_TOP));
        builder.addHex(new Point(0, 0));
        builder.addHexNextTo(0, 1);
        builder.addHexNextTo(1, 0);
        builder.addHexNextTo(2, 1);
        builder.addHexNextTo(3, 0);
        builder.addHexNextTo(4, 1);
        builder.addHexNextTo(5, 0);
        gridLvlSelect = builder.build();
    }

    private void updateLevelSelectGrid()
    {
        gridLvlSelect.getHexs()[0].setHexData(hexpert.assetManager.get(TEXTURE_LEFT));
        gridLvlSelect.getHexs()[gridLvlSelect.getHexs().length - 1].setHexData(hexpert.assetManager.get(TEXTURE_RIGHT));
        for(int i = 1; i < gridLvlSelect.getHexs().length - 1; i++)
        {
            try{
                boolean[] objectiveStatus = getObjectivePassed((currentWorld - 1) * levelsToDisplay + i + 1);
                gridLvlSelect.getHexs()[i].setHexData(getTextureByObjectiveStatus(objectiveStatus));
            }catch (MapLoadException e)
            {
                gridLvlSelect.getHexs()[i].setHexData(hexpert.assetManager.get(TEXTURE_GRASS));
            }
        }
    }

    private Texture getTextureByObjectiveStatus(boolean[] objective)
    {
        int missingObjectives = 0;

        for(int i = 0; i < objective.length; i++)
            if(!objective[i])
                missingObjectives++;

        if(missingObjectives == objective.length)
            return hexpert.assetManager.get(TEXTURE_GRASS, Texture.class);

        if(objective.length >= 3 && missingObjectives == 0)
            hexpert.playServices.unlockAchievement(Achievement.PERFECT);


        switch (missingObjectives)
        {
            case 0 :
                return hexpert.assetManager.get(TEXTURE_HEXGOLD, Texture.class);
            case 1 :
                return hexpert.assetManager.get(TEXTURE_HEXSILVER, Texture.class);
            case 2:
                return hexpert.assetManager.get(TEXTURE_HEXBRONZE, Texture.class);
            default:
                return hexpert.assetManager.get(TEXTURE_GRASS, Texture.class);
        }
    }

    public HexMap<Texture> getLvlSelectGrid() {
        return gridLvlSelect;
    }

    private void setMultiplexer()
    {
        behavior = new LevelSelectGesture(this);
        detector = new GestureDetector(behavior);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(getStage());
        inputMultiplexer.addProcessor(detector);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public int getLevelsToDisplay() {
        return levelsToDisplay;
    }


    private void setMapCollision()
    {
        double maxHeight = -1, maxWidth = -1;
        double minHeight = Double.MAX_VALUE, minWidth = Double.MAX_VALUE;
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);

            for(Point p : hex.getHexGeometry().getPoints())
            {
                if(p.y < minHeight)
                    minHeight = p.y;

                if(p.y > maxHeight)
                    maxHeight = p.y;

                if(p.x < minWidth)
                    minWidth = p.x;

                if(p.x > maxWidth)
                    maxWidth = p.x;
            }
        }

        double deltaX = maxWidth - minWidth;
        double middleX = minWidth + deltaX/2;

        double deltaY = maxHeight - minHeight;
        double middleY = minHeight + deltaY/2;

        shapeRenderer.setProjectionMatrix(displayLevelCamera.combined);
        mapCollision = new Rectangle((float)minWidth, (float)minHeight, (float)deltaX, (float)deltaY);
    }

    public OrthographicCamera getDisplayLevelCam() {
        return displayLevelCamera;
    }

    public void goToLevel()
    {
        String mapName = hexpert.levelIndex.get(levelSelect);
        String mapLoc = Gdx.files.internal("maps/" + mapName + ".hexmap").readString();
        Map map = new JSONDeserializer<Map>().deserialize(mapLoc);

        String mapResLoc = Gdx.files.local(mapName + ".mapres").readString();
        MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapResLoc);

        hexpert.setScreen(new PlayScreen(hexpert, map, mapResult, mapName));
    }

    public Rectangle getMapCollision() {
        return mapCollision;
    }

    public boolean isLastWorld(int world)
    {
        return world == Math.ceil((float)getTotalLevels() / (float)getLevelsToDisplay());
    }

    public int getTotalLevels() {
        return totalLevels;
    }
}
