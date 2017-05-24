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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CORRECT;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_GRASS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXBRONZE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXGOLD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HEXSILVER;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table table;
    private Table objectiveTable;
    private int levelsToDisplay = 5;
    private int totalLevels = 9;
    private int currentWorld = 1;
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
    private ShapeRenderer shapeRenderer;
    private LevelSelectGesture behavior;
    private GestureDetector detector;
    private ShaderProgram shdDark;

    public LevelSelectScreen(final Hexpert hexpert)
    {
        super();
        this.hexpert = hexpert;
        this.batch = new SpriteBatch();
        this.uiBatch = new SpriteBatch();
        this.displayBatch = new SpriteBatch();
        this.uiCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.displayLevelCamera = new OrthographicCamera(WIDTH, HEIGHT);
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.objectiveTable = new Table();
        objectiveTable.setX(150);
        objectiveTable.setY(850);
        objectiveTable.defaults().pad(20);
        getStage().addActor(objectiveTable);
        hexCount = getHexCount();


        String vertexShader = Gdx.files.internal("shaders/defaultvertex.vs").readString();
        String darkShader = Gdx.files.internal("shaders/darkness.fs").readString();
        shdDark = new ShaderProgram(vertexShader, darkShader);
        if (!shdDark.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shdDark.getLog());

        table = new Table();
        table.defaults().width(150).height(150);
        table.setX(900);
        table.setY(100);
        table.defaults().pad(20);
        getCamera().translate(400, 400);
        getCamera().update();

        btnLeft = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get("sprites/nextlevelleft.png"))));
        btnRight = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get("sprites/nextlevelright.png"))));
        btnLeft.getImageCell().expand().fill();

        btnLeft.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(currentWorld > 1)
                {
                    currentWorld--;
                    updateLabels();
                    updateLevelSelectGrid();


                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);
                }
            }
        });

        table.add(btnLeft);

//            button.addListener(new ClickListener()
//            {
//                @Override
//                public void clicked(InputEvent event, float x, float y)
//                {
//                    selectLevel((currentWorld - 1) * levelsToDisplay + finalI + 1);
//                    hexpert.sounds.get("select").play();
//                }
//            });


        table.add(new WidgetGroup()).width(800);


        btnRight.getImageCell().expand().fill();
        btnRight.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(currentWorld * levelsToDisplay < totalLevels)
                {
                    currentWorld++;
                    updateLabels();
                    updateLevelSelectGrid();

                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);

                }
            }
        });
        table.add(btnRight);

        createLevelSelectGrid();
        final TextButton button = new TextButton(hexpert.i18NBundle.get("select"), hexpert.getSkin());
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                button.setChecked(false);
                hexpert.sounds.get("select").play();

//                JSONSerializer jsonSerializer = new JSONSerializer();
//
//
//                HexFreeShapeBuilder builder = new HexFreeShapeBuilder()
//                        .setStyle(new HexStyle(80, HexagonOrientation.FLAT_TOP));
//
//                builder.addHex(new Point(0, 0));
//                builder.addHexNextTo(0, 0);
//                builder.addHexNextTo(0, 1);
//                builder.addHexNextTo(0, 2);
//                builder.addHexNextTo(0, 3);
//                builder.addHexNextTo(0, 4);
//                builder.addHexNextTo(0, 5);
//                builder.addHexNextTo(1, 0);
//                builder.addHexNextTo(1, 1);
//                builder.addHexNextTo(2, 1);
//                builder.addHexNextTo(2, 2);
//                builder.addHexNextTo(3, 2);
//                builder.addHexNextTo(3, 3);
//                builder.addHexNextTo(4, 3);
//                builder.addHexNextTo(4, 4);
//                builder.addHexNextTo(5, 4);
//                builder.addHexNextTo(5, 5);
//                builder.addHexNextTo(6, 5);
//                builder.addHexNextTo(6, 0);
//                builder.addHexNextTo(7, 5);
//                builder.addHexNextTo(7, 0);
//                builder.addHexNextTo(7, 1);
//                builder.addHexNextTo(8, 1);
//                builder.addHexNextTo(9, 1);
//                builder.addHexNextTo(9, 2);
//                builder.addHexNextTo(10, 2);
//                builder.addHexNextTo(11, 2);
//                builder.addHexNextTo(12, 2);
//                builder.addHexNextTo(13, 2);
//                builder.addHexNextTo(13, 3);
//                builder.addHexNextTo(14, 3);
//                builder.addHexNextTo(15, 3);
//                builder.addHexNextTo(15, 4);
//                builder.addHexNextTo(15, 5);
//                builder.addHexNextTo(16, 5);
//                builder.addHexNextTo(17, 5);
//                builder.addHexNextTo(18, 5);
//
//                TileType[] tileTypes = new TileType[builder.getHexagons().size()];
//                BuildingType[] buildingTypes = new BuildingType[builder.getHexagons().size()];
//
//                for (int i = 0; i < tileTypes.length; i++)
//                {
//                    tileTypes[i] = TileType.GRASS;
//                }
//                tileTypes[13] = TileType.SNOW;
//                tileTypes[9] = TileType.SNOW;
//                tileTypes[15] = TileType.SAND;
//                tileTypes[7] = TileType.SAND;
//                tileTypes[17] = TileType.WATER;
//                tileTypes[11] = TileType.SAND;
//
//                for (int i = 0; i < buildingTypes.length; i++)
//                {
//                    buildingTypes[i] = BuildingType.NONE;
//                }
//
//                Map map = new Map();
//                map.setBuilder(builder);
//                map.setTileTypes(tileTypes);
//                map.setBuildingType(buildingTypes);
//                map.setCalculateScore(true);
//                map.setObjectives(new Objective[]{
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 60),
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 68),
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 76)});
//
//                String mapString = jsonSerializer.deepSerialize(map);
//
                String mapLoc = Gdx.files.internal("maps/" + levelSelect + ".hexmap").readString();
                Map map = new JSONDeserializer<Map>().deserialize(mapLoc);

                String mapResLoc = Gdx.files.local(levelSelect + ".mapres").readString();
                MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapResLoc);

                hexpert.setScreen(new PlayScreen(hexpert, map, mapResult));
            }
        });
        table.add(button).colspan(levelsToDisplay).width(350).center();
        getStage().addActor(table);
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
            if(i % 2 == 1) continue;

            batch.setShader(levelSelect == (currentWorld - 1) * levelsToDisplay + i + 1 ? shdDark : null);

            Hexagon<Texture> hex = gridLvlSelect.getHexs()[i];
            Point p = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData(),
                    (float)(p.x - gridLvlSelect.getStyle().getSize()),
                    (float)(p.y - gridLvlSelect.getStyle().getSize() * HEX_HEIGHT_RATIO),
                    (float)gridLvlSelect.getStyle().getSize()*2,
                    (float)((float)gridLvlSelect.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

            hexpert.getFont().draw(batch,
                    Integer.toString((currentWorld - 1) * levelsToDisplay + i + 1),
                    (int)p.x - 10, (int)p.y + 30);
        }

        for(int i = gridLvlSelect.getHexs().length - 1; i >= 0; i--)
        {
            if(i % 2 == 0) continue;
            batch.setShader(levelSelect == (currentWorld - 1) * levelsToDisplay + i + 1 ? shdDark : null);
            Hexagon<Texture> hex = gridLvlSelect.getHexs()[i];
            Point p = hex.getHexGeometry().getMiddlePoint();
            batch.draw(hex.getHexData(),
                    (float)(p.x - gridLvlSelect.getStyle().getSize()),
                    (float)(p.y - gridLvlSelect.getStyle().getSize() * HEX_HEIGHT_RATIO),
                    (float)gridLvlSelect.getStyle().getSize()*2,
                    (float)((float)gridLvlSelect.getStyle().getSize()*2 * HEX_HEIGHT_RATIO) + 24);

            hexpert.getFont().draw(batch,
                    Integer.toString((currentWorld - 1) * levelsToDisplay + i + 1),
                    (int)p.x - 10, (int)p.y + 30);
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
        uiBatch.begin();

        uiBatch.setProjectionMatrix(uiCamera.combined);

        if(result.getScore() > 0)
            hexpert.getFont().draw(uiBatch, hexpert.i18NBundle.format("best", result.getScore()), 700, 400);

        hexpert.getFont().draw(uiBatch, hexpert.i18NBundle.format("hexCount", hexCount), -500, -250);
        uiBatch.end();

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
        setMultiplexer();
    }

    private Map loadDisplayLevel()
    {
        String mapLoc = Gdx.files.internal("maps/" + levelSelect + ".hexmap").readString();
        Map map = new JSONDeserializer<Map>().deserialize(mapLoc);
        grid = map.build();

        String mapString = Integer.toString(levelSelect) + ".mapres";
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

                Label lblDesc = new Label(map.getObjectives()[i].toString(), hexpert.getSkin());
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
            if(Gdx.files.local(i + ".mapres").exists())
            {
                String mapResLoc = Gdx.files.local(i + ".mapres").readString();
                MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapResLoc);

                for(int j = 0; j < mapResult.getObjectivePassed().length; j++)
                {
                    total += mapResult.getObjectivePassed()[j] ? 1 : 0;
                }
            }else{
                String mapString = Gdx.files.internal("maps/" + i + ".hexmap").readString();
                Map map = new JSONDeserializer<Map>().deserialize(mapString);
                MapResult res = new MapResult(i, 0, new boolean[map.getObjectives().length]);
                JSONSerializer jsonSerializer = new JSONSerializer();
                Gdx.files.local(i + ".mapres").writeString(jsonSerializer.deepSerialize(res), false);
            }
        }

        return total;
    }

    private boolean[] getObjectivePassed(int level)
    {
        FileHandle mapresFile = Gdx.files.local(level + ".mapres");

        if(mapresFile.exists())
        {
            MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapresFile.readString());
            return mapResult.getObjectivePassed();
        }else{
            throw new MapLoadException(String.format("Can't find %d.mapres", level));
        }
    }

    private void updateLabels()
    {
        //for(int i = 0; i < labelList.size(); i++)
       // {
        //    labelList.get(i).setText(Integer.toString((currentWorld - 1) * levelsToDisplay + 1 + i));
        //}
    }

    private void createLevelSelectGrid()
    {
        HexFreeShapeBuilder<Texture> builder = new HexFreeShapeBuilder<>();
        builder.setStyle(new HexStyle(80, HexagonOrientation.FLAT_TOP));
        builder.addHex(new Point(0, 0));
        builder.addHexNextTo(0, 0);
        builder.addHexNextTo(1, 1);
        builder.addHexNextTo(2, 0);
        builder.addHexNextTo(3, 1);
        gridLvlSelect = builder.build();
    }

    private void updateLevelSelectGrid()
    {
        for(int i = 0; i < gridLvlSelect.getHexs().length; i++)
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

    public int getCurrentWorld() {
        return currentWorld;
    }
}
