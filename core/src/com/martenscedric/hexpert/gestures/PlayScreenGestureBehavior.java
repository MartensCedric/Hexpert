package com.martenscedric.hexpert.gestures;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.misc.IntPointTime;
import com.martenscedric.hexpert.misc.PointTime;
import com.martenscedric.hexpert.misc.Rules;
import com.martenscedric.hexpert.screens.PlayScreen;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;
import com.martenscedric.hexpert.tile.TileType;

/**
 * Created by 1544256 on 2017-05-02.
 */

public class PlayScreenGestureBehavior extends StandardGestureBehavior {

    private Stage stage;
    private HexMap<TileData> grid;
    private Hexpert hexpert;
    private PlayScreen playScreen;

    public PlayScreenGestureBehavior(PlayScreen playScreen) {
        super(playScreen.getCamera());
        this.stage = playScreen.getStage();
        this.grid = playScreen.getGrid();
        this.hexpert = playScreen.getHexpert();
        this.playScreen = playScreen;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Viewport viewPort = stage.getViewport();
        Vector3 pos = getCamera().unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Hexagon<TileData> data = grid.getAt(new Point(pos.x, pos.y));

        if(data != null && playScreen.getSelection() != null && data.getHexData().getBuildingType() == BuildingType.NONE)
        {

            if(data.getHexData().getTileType()
                    != TileType.WATER && Rules.isValid(data.getHexData(), playScreen.getSelection()))
            {
                data.getHexData().setBuildingType(playScreen.getSelection());
                data.getHexData().setBuildingTexture(hexpert.getTextureByBuilding(playScreen.getSelection()));
                playScreen.getPlacementHistory().push(data.getHexData());

                int score = data.getHexData().getBuildingType().getScore() * data.getHexData().getTileType().getMultiplier();

                if(playScreen.getMap().isCalculateScore())
                    playScreen.getMoveEventManager()
                            .getScoreList().add(
                                    new IntPointTime(score, new Point(pos.x, pos.y), 0.35f)
                                    );

                playScreen.updateObjectives();
                hexpert.sounds.get("click").play();
                playScreen.setSelection(null);
            }else{
                playScreen.getMoveEventManager().getBadMoves().add(new PointTime(new Point(pos.x,pos.y), 0.35f));
                hexpert.sounds.get("bad").play();
            }
        }
        return true;
    }
}
