package com.martenscedric.hexcity.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cedricmartens.hexmap.coordinate.CoordinateSystem;
import com.cedricmartens.hexmap.coordinate.CubeCoordinate;
import com.cedricmartens.hexmap.coordinate.IndexedCoordinate;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexStyle;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.hexagon.HexagonOrientation;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.martenscedric.hexcity.HexCity;
import com.martenscedric.hexcity.map.Map;
import com.martenscedric.hexcity.map.MapResult;
import com.martenscedric.hexcity.misc.AssetLoader;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import static com.martenscedric.hexcity.misc.Const.HEIGHT;
import static com.martenscedric.hexcity.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexcity.misc.Const.WIDTH;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table table;
    private int levelsToDisplay = 5;
    private final HexCity hexCity;
    private HexMap<TileData> previewGrid;
    private int levelSelect = 1;
    private HexMap<TileData> grid;
    private SpriteBatch batch;
    private MapResult result;

    public LevelSelectScreen(final HexCity hexCity)
    {
        super();
        this.hexCity = hexCity;
        this.batch = new SpriteBatch();
        table = new Table();
        table.setX(WIDTH/2);
        table.setY(HEIGHT/4);
        table.defaults().pad(20);
        previewGrid = loadLevel(1);
        getCamera().update();
        for(int i = 0; i < levelsToDisplay; i++)
        {
            TextButton button = new TextButton(Integer.toString(i + 1), AssetLoader.getSkin());
            button.getLabel().setFontScale(5);
            final int level = i + 1;
            button.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    levelSelect = level;
                    loadDisplayLevel();
                }
            });
            table.add(button);
        }
        table.row();
        TextButton button = new TextButton("Select", AssetLoader.getSkin());
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

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
//                builder.addHexNextTo(3, 1);
//                builder.addHexNextTo(3, 2);
//                builder.addHexNextTo(3, 3);
//                builder.addHexNextTo(4, 3);
//                builder.addHexNextTo(8, 5);
//                builder.addHexNextTo(5, 2);
//                builder.addHexNextTo(9, 0);
//                builder.addHexNextTo(8, 2);
//                builder.addHexNextTo(12, 1);
//                builder.addHexNextTo(12, 2);
//                builder.addHexNextTo(10, 3);
//
//                TileType[] tileTypes = new TileType[17];
//                BuildingType[] buildingTypes = new BuildingType[17];
//
//                for (int i = 0; i < tileTypes.length; i++)
//                {
//                    tileTypes[i] = TileType.GRASS;
//                }
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
//
//                String mapString = jsonSerializer.deepSerialize(map);
//
                String mapLoc = Gdx.files.internal("maps/" + levelSelect + ".hexmap").readString();
                Map result = new JSONDeserializer<Map>().deserialize(mapLoc);

                hexCity.setScreen(new PlayScreen(hexCity, result));
            }
        });
        button.getLabel().setFontScale(5);
        table.add(button).colspan(levelsToDisplay);
        getStage().addActor(table);
        loadDisplayLevel();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(getCamera().combined);
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

    private HexMap<TileData> loadLevel(int levelId)
    {
        return null;
    }

    private void loadDisplayLevel()
    {
        String mapLoc = Gdx.files.internal("maps/" + levelSelect + ".hexmap").readString();
        Map result = new JSONDeserializer<Map>().deserialize(mapLoc);
        grid = result.build();

        double maxHeight = -1;
        double minHeight = Double.MAX_VALUE;
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            data.setTerrainTexture(hexCity.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexCity.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);

            if(hex.getHexGeometry().getMiddlePoint().y < minHeight)
                minHeight = hex.getHexGeometry().getMiddlePoint().y;

            if(hex.getHexGeometry().getMiddlePoint().y > maxHeight)
                maxHeight = hex.getHexGeometry().getMiddlePoint().y;
        }
        double delta = (maxHeight - minHeight);

        String mapString = Integer.toString(levelSelect) + ".mapres";
        if(Gdx.files.local(mapString).exists())
        {
            MapResult mapResult = new MapResult(levelSelect, 0);
            JSONSerializer jsonSerializer = new JSONSerializer();
            Gdx.files.local(mapString).writeString(jsonSerializer.deepSerialize(mapResult), false);
        }else{

        }

        getCamera().zoom = (float) (delta/340.0);
        getCamera().position.setZero();
        getCamera().translate(0, -150 + (float)(maxHeight/delta)*50);
        getCamera().update();
    }
}
