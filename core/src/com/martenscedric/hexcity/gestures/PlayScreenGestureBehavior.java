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
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;

/**
 * Created by 1544256 on 2017-05-02.
 */

public class PlayScreenGestureBehavior extends StandardGestureBehavior {

    private Stage stage;
    private HexMap<TileData> grid;
    private HexCity hexCity;

    public PlayScreenGestureBehavior(OrthographicCamera camera, Stage stage, HexMap<TileData> grid, HexCity hexCity) {
        super(camera);
        this.stage = stage;
        this.grid = grid;
        this.hexCity = hexCity;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("gdxdebug", x + " " + y);
        Viewport viewPort = stage.getViewport();
        Vector3 pos = getCamera().unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Gdx.app.log("gdxdebug", pos
                .toString());

        Hexagon<TileData> data = grid.getAt(new Point(pos.x, pos.y));

        if(data == null)
            Gdx.app.log("gdxdebug", "NULL");
        else
        {
            data.getHexData().setBuildingType(BuildingType.HOUSE);
            data.getHexData().setTexture(hexCity.getTextureByBuilding(BuildingType.HOUSE));
        }
        return true;
    }
}
