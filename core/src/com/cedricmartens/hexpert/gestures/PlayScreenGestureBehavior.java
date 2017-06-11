package com.cedricmartens.hexpert.gestures;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;

/**
 * Created by 1544256 on 2017-05-02.
 */

public class PlayScreenGestureBehavior extends StandardGestureBehavior {

    private Stage stage;
    private HexMap<com.cedricmartens.hexpert.tile.TileData> grid;
    private com.cedricmartens.hexpert.Hexpert hexpert;
    private com.cedricmartens.hexpert.screens.PlayStage playStage;

    public PlayScreenGestureBehavior(com.cedricmartens.hexpert.screens.PlayScreen playStage) {
        super(playStage.getCamera());
        this.stage = playStage.getStage();
        this.grid = playStage.getGrid();
        this.hexpert = playStage.getHexpert();
        this.playStage = playStage;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        com.cedricmartens.hexpert.screens.PlayScreen playScreen = (com.cedricmartens.hexpert.screens.PlayScreen)playStage;
        Viewport viewPort = stage.getViewport();
        Vector3 pos = getCamera().unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Hexagon<com.cedricmartens.hexpert.tile.TileData> data = grid.getAt(new Point(pos.x, pos.y));

        if(data != null && playStage.removeMode && playScreen.isRemovable(data.getHexData()))
        {
            data.getHexData().setBuildingTexture(null);
            data.getHexData().setBuildingType(com.cedricmartens.hexpert.tile.BuildingType.NONE);
        }
        else if(data != null && playScreen.getSelection() != null
                && data.getHexData().getBuildingType() == com.cedricmartens.hexpert.tile.BuildingType.NONE)
        {

            if(data.getHexData().getTileType()
                    != com.cedricmartens.hexpert.tile.TileType.WATER && com.cedricmartens.hexpert.tile.Rules.isValidPlacement(data.getHexData(), playScreen.getSelection()))
            {
                data.getHexData().setBuildingType(playScreen.getSelection());
                data.getHexData().setBuildingTexture(hexpert.getTextureByBuilding(playScreen.getSelection()));

                int score = data.getHexData().getBuildingType().getScore() * data.getHexData().getTileType().getMultiplier();

                if(playScreen.getMap().isCalculateScore())
                    playScreen.getMoveEventManager()
                            .getScoreList().add(
                                    new com.cedricmartens.hexpert.misc.IntPointTime(score, new Point(pos.x, pos.y), 0.35f)
                                    );

                playScreen.updateObjectives();
                hexpert.sounds.get("click").play();

                if(!hexpert.config.isKeepSelection())
                    playStage.setSelection(null);

                playScreen.checkAchievements();
            }else{
                playScreen.getMoveEventManager().getBadMoves().add(new com.cedricmartens.hexpert.misc.PointTime(new Point(pos.x,pos.y), 0.35f));
                hexpert.sounds.get("bad").play();
            }
        }
        return true;
    }
}
