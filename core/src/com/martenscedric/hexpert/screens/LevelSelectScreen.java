package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.martenscedric.hexpert.map.Objective;
import com.martenscedric.hexpert.misc.AssetLoader;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.HEX_HEIGHT_RATIO;
import static com.martenscedric.hexpert.misc.Const.WIDTH;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FARM;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class LevelSelectScreen extends StageScreen
{
    private Table table;
    private int levelsToDisplay = 5;
    private int totalLevels = 7;
    private int currentWorld = 1;
    private final Hexpert hexpert;
    private int levelSelect = 1;
    private HexMap<TileData> grid;
    private SpriteBatch batch, absBatch;
    private MapResult result;
    private String currentObjective = "";
    private int starCount = 0;
    private ImageButton btnLeft, btnRight;
    private List<TextButton> buttonList;

    public LevelSelectScreen(final Hexpert hexpert)
    {
        super();
        this.hexpert = hexpert;
        this.batch = new SpriteBatch();
        this.absBatch = new SpriteBatch();
        table = new Table();
        table.defaults().width(150).height(150);
        table.setX(WIDTH/2);
        table.setY(HEIGHT/4);
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
                }
            }
        });
        table.add(btnRight);

        selectLevel(1);
        starCount = getStarCount();

        table.row();

        TextButton button = new TextButton(hexpert.i18NBundle.get("select"), AssetLoader.getSkin());
        button.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

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
//                builder.addHexNextTo(2, 1);
//                builder.addHexNextTo(2, 2);
//                builder.addHexNextTo(5, 4);
//                builder.addHexNextTo(5, 5);
//                builder.addHexNextTo(7, 1);
//                builder.addHexNextTo(8, 1);
//                builder.addHexNextTo(9, 4);
//                builder.addHexNextTo(10, 4);
//                builder.addHexNextTo(10, 5);
//                builder.addHexNextTo(6, 5);
//                builder.addHexNextTo(3, 2);
//                builder.addHexNextTo(8, 2);
//
//
//
//                TileType[] tileTypes = new TileType[20];
//                BuildingType[] buildingTypes = new BuildingType[20];
//
//                for (int i = 0; i < tileTypes.length; i++)
//                {
//                    tileTypes[i] = TileType.GRASS;
//                }
//
//                tileTypes[2] = TileType.SAND;
//                tileTypes[5] = TileType.SNOW;
//
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
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 1, 0}, 12),
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 1, 1}, 14),
//                        new Objective(new int[]{0, 0, 0, 0, 0, 0, 1, 2}, 16)});
//
//                String mapString = jsonSerializer.deepSerialize(map);

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
        Gdx.gl.glClearColor(66f/255f, 206f/255f, 244f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(getCamera().combined);
        batch.begin();
        btnLeft.setVisible(currentWorld > 1);
        btnRight.setVisible(currentWorld * levelsToDisplay < totalLevels);

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
        absBatch.begin();
        AssetLoader.getFont().draw(absBatch, currentObjective, 50, HEIGHT - 50);

        if(result.getScore() > 0)
            AssetLoader.getFont().draw(absBatch, hexpert.i18NBundle.format("best", result.getScore()), WIDTH - 200, HEIGHT - 100);

        AssetLoader.getFont().draw(absBatch, hexpert.i18NBundle.format("star", starCount), WIDTH - 400, 100);

        absBatch.end();
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
            result = new MapResult(levelSelect, 0, new boolean[map.getObjectives().length]);
            JSONSerializer jsonSerializer = new JSONSerializer();
            Gdx.files.local(mapString).writeString(jsonSerializer.deepSerialize(result), false);
        }else{
            JSONDeserializer<MapResult> deserializer = new JSONDeserializer<>();
            result = deserializer.deserialize(Gdx.files.local(mapString).readString());
        }

        getCamera().zoom = (float) (delta/340.0);
        getCamera().position.setZero();
        getCamera().translate(0, -150 + (float)(maxHeight/delta)*50);
        getCamera().update();
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
                str+= (result.getObjectivePassed()[i] ? "[DONE] " : "") + map.getObjectives()[i].toString();
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
