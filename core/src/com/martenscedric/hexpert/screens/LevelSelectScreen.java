package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.HexStyle;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.hexagon.HexagonOrientation;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.map.Map;
import com.martenscedric.hexpert.map.MapResult;
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.misc.AssetLoader;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

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
    private final Hexpert hexpert;
    private HexMap<TileData> previewGrid;
    private int levelSelect = 1;
    private HexMap<TileData> grid;
    private SpriteBatch batch;
    private MapResult result;

    public LevelSelectScreen(final Hexpert hexpert)
    {
        super();
        this.hexpert = hexpert;
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
                    hexpert.sounds.get("select").play();
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

               hexpert.sounds.get("select").play();

             JSONSerializer jsonSerializer = new JSONSerializer();


                HexFreeShapeBuilder builder = new HexFreeShapeBuilder()
                        .setStyle(new HexStyle(80, HexagonOrientation.FLAT_TOP));

                builder.addHex(new Point(0, 0));
                builder.addHexNextTo(0, 0);
                builder.addHexNextTo(0, 1);
                builder.addHexNextTo(0, 2);
                builder.addHexNextTo(0, 3);
                builder.addHexNextTo(0, 4);
                builder.addHexNextTo(0, 5);
                builder.addHexNextTo(3, 1);
                builder.addHexNextTo(3, 2);
                builder.addHexNextTo(3, 3);
                builder.addHexNextTo(4, 3);
                builder.addHexNextTo(4, 4);
                builder.addHexNextTo(5, 4);
                builder.addHexNextTo(6, 0);
                builder.addHexNextTo(6, 4);
                builder.addHexNextTo(6, 5);
                builder.addHexNextTo(13, 0);
                builder.addHexNextTo(13, 5);


                TileType[] tileTypes = new TileType[18];
                BuildingType[] buildingTypes = new BuildingType[18];

                for (int i = 0; i < tileTypes.length; i++)
                {
                    tileTypes[i] = TileType.GRASS;
                }

                tileTypes[1] = TileType.WATER;
                tileTypes[10] = TileType.WATER;
                tileTypes[5] = TileType.SAND;


                for (int i = 0; i < buildingTypes.length; i++)
                {
                    buildingTypes[i] = BuildingType.NONE;
                }

                Map map = new Map();
                map.setBuilder(builder);
                map.setTileTypes(tileTypes);
                map.setBuildingType(buildingTypes);
                map.setCalculateScore(true);
                map.setObjectives(new Objective[]{
                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 12),
                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 14),
                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 0, 0}, 16)});

                String mapString = jsonSerializer.deepSerialize(map);

                String mapLoc = Gdx.files.internal("maps/" + levelSelect + ".hexmap").readString();
                //Map map = new JSONDeserializer<Map>().deserialize(mapLoc);


                String mapResLoc = Gdx.files.local(levelSelect + ".mapres").readString();
                MapResult mapResult = new JSONDeserializer<MapResult>().deserialize(mapResLoc);

                hexpert.setScreen(new PlayScreen(hexpert, map, mapResult));
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
            data.setTerrainTexture(hexpert.getTextureByTerrain(data.getTileType()));
            data.setBuildingTexture(hexpert.getTextureByBuilding(data.getBuildingType()));
            hex.setHexData(data);

            if(hex.getHexGeometry().getMiddlePoint().y < minHeight)
                minHeight = hex.getHexGeometry().getMiddlePoint().y;

            if(hex.getHexGeometry().getMiddlePoint().y > maxHeight)
                maxHeight = hex.getHexGeometry().getMiddlePoint().y;
        }
        double delta = (maxHeight - minHeight);

        String mapString = Integer.toString(levelSelect) + ".mapres";
        if(!Gdx.files.local(mapString).exists())
        {
            MapResult mapResult = new MapResult(levelSelect, 0, new boolean[result.getObjectives().length]);
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
