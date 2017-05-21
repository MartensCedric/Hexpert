package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexStyle;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.hexagon.HexagonOrientation;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.misc.AssetLoader;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexpert.misc.Const.WIDTH;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table table;
    private int levelsToDisplay = 5;
    private int totalLevels = 9;
    private int currentWorld = 1;
    private final Hexpert hexpert;
    private int levelSelect = 1;
    private HexMap<TileData> grid;
    private SpriteBatch batch, uiBatch, displayBatch;
    private MapResult result;
    private OrthographicCamera uiCamera, displayLevelCamera;
    private String currentObjective = "";
    private int starCount = 0;
    private ImageButton btnLeft, btnRight;
    private List<TextButton> buttonList;
    private boolean debug = false;
    private ShapeRenderer shapeRenderer;

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
        table = new Table();
        table.defaults().width(150).height(150);
        table.setX(900);
        table.setY(100);
        table.defaults().pad(20);
        getCamera().update();

        starCount = getStarCount();

        buttonList = new ArrayList<>();

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
                    updateButtons();


                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);

                    for(int i = 0; i < buttonList.size(); i++)
                        buttonList.get(i).setChecked(i == 0);

                }
            }
        });

        table.add(btnLeft);
        for(int i = 0; i < levelsToDisplay; i++)
        {
            final TextButton button = new TextButton(Integer.toString(i + 1), AssetLoader.getSkin());
            button.getLabel().setFontScale(5);
            button.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    selectLevel(Integer.parseInt(button.getText().toString()));
                    hexpert.sounds.get("select").play();
                    for(TextButton b : buttonList)
                        b.setChecked(false);
                    button.setChecked(true);
                }
            });
            buttonList.add(button);
            table.add(button);
        }


        btnRight.getImageCell().expand().fill();
        btnRight.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(currentWorld * levelsToDisplay < totalLevels)
                {
                    currentWorld++;
                    updateButtons();

                    hexpert.sounds.get("select").play();
                    selectLevel((currentWorld - 1) * levelsToDisplay + 1);

                    for(int i = 0; i < buttonList.size(); i++)
                        buttonList.get(i).setChecked(i == 0);
                }
            }
        });
        table.add(btnRight);

        for(int i = 0; i < buttonList.size(); i++)
            buttonList.get(i).setChecked(i == 0);

        selectLevel(1);
        starCount = getStarCount();

        final TextButton button = new TextButton(hexpert.i18NBundle.get("select"), AssetLoader.getSkin());
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
        button.getLabel().setFontScale(5);
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
        hexpert.getFont().draw(uiBatch, currentObjective, -850, 500);

        if(result.getScore() > 0)
            hexpert.getFont().draw(uiBatch, hexpert.i18NBundle.format("best", result.getScore()), 700, 400);

        hexpert.getFont().draw(uiBatch, hexpert.i18NBundle.format("star", starCount), 700, 500);
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
        starCount = getStarCount();
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

    private void selectLevel(int levelID)
    {
        try{
            levelSelect = levelID;
            Map map = loadDisplayLevel();
            String str = "";
            for(int i = 0; i < map.getObjectives().length; i++)
            {
                str+= (result.getObjectivePassed()[i] ? "[DONE] " : "") + map.getObjectives()[i].toString() + "\n";
            }
            currentObjective = str;
        }catch (Exception e)
        {

        }
    }

    private int getStarCount()
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

    private void updateButtons()
    {
        for(int i = 0; i < buttonList.size(); i++)
        {
            buttonList.get(i).setText(Integer.toString((currentWorld - 1) * levelsToDisplay + 1 + i));
        }
    }
}
