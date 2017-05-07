package com.martenscedric.hexcity.gestures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexcity.HexCity;
import com.martenscedric.hexcity.screens.PlayScreen;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

/**
 * Created by 1544256 on 2017-05-02.
 */

public class PlayScreenGestureBehavior extends StandardGestureBehavior {

    private Stage stage;
    private HexMap<TileData> grid;
    private HexCity hexCity;
    private PlayScreen playScreen;

    public PlayScreenGestureBehavior(PlayScreen playScreen) {
        super(playScreen.getCamera());
        this.stage = playScreen.getStage();
        this.grid = playScreen.getGrid();
        this.hexCity = playScreen.getHexCity();
        this.playScreen = playScreen;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Viewport viewPort = stage.getViewport();
        Vector3 pos = getCamera().unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Hexagon<TileData> data = grid.getAt(new Point(pos.x, pos.y));

        if(data != null &&  playScreen.getSelection() != null && data.getHexData().getTileType() != TileType.WATER)
        {
            data.getHexData().setBuildingType(playScreen.getSelection());
            data.getHexData().setTexture(hexCity.getTextureByBuilding(playScreen.getSelection()));
            playScreen.setSelection(null);
        }
        return true;
    }
}
