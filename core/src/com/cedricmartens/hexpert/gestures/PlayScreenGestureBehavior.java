package com.cedricmartens.hexpert.gestures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.misc.IntPointTime;
import com.cedricmartens.hexpert.misc.PointTime;
import com.cedricmartens.hexpert.screens.PlayScreen;
import com.cedricmartens.hexpert.screens.PlayStage;
import com.cedricmartens.hexpert.tile.BuildingType;
import com.cedricmartens.hexpert.tile.Rules;
import com.cedricmartens.hexpert.tile.TileData;
import com.cedricmartens.hexpert.tile.TileType;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FOREST_CUT;

/**
 * Created by 1544256 on 2017-05-02.
 */

public class PlayScreenGestureBehavior extends StandardGestureBehavior {

    private Stage stage;
    private HexMap<TileData> grid;
    private Hexpert hexpert;
    private PlayStage playStage;

    public PlayScreenGestureBehavior(PlayScreen playStage) {
        super(playStage.getCamera());
        this.stage = playStage.getStage();
        this.grid = playStage.getGrid();
        this.hexpert = playStage.getHexpert();
        this.playStage = playStage;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        PlayScreen playScreen = (PlayScreen)playStage;
        Viewport viewPort = stage.getViewport();
        Vector3 pos = getCamera().unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Hexagon<TileData> data = grid.getAt(new Point(pos.x, pos.y));

        if(data != null && playStage.removeMode
                && data.getHexData().getBuildingType() != BuildingType.NONE
                && playScreen.isRemovable(data.getHexData()))
        {
            data.getHexData().setBuildingTexture(null);
            data.getHexData().setBuildingType(BuildingType.NONE);


            if(data.getHexData().getTileType() == TileType.FOREST)
            {
                data.getHexData().setTerrainTexture((Texture)hexpert.assetManager.get(TEXTURE_FOREST));
            }
        }
        else if(data != null && playScreen.getSelection() != null
                && playScreen.canPlaceBuildings())
        {
            if(data.getHexData().getTileType()
                    != TileType.WATER && Rules.isValidPlacement(data.getHexData(), playScreen.getSelection()))
            {
                data.getHexData().setBuildingType(playScreen.getSelection());
                data.getHexData().setBuildingTexture(hexpert.getTextureByBuilding(playScreen.getSelection()));

                if(data.getHexData().getTileType() == TileType.FOREST)
                    data.getHexData().setTerrainTexture((Texture) hexpert.assetManager.get(TEXTURE_FOREST_CUT));

                int score = data.getHexData().getBuildingType().getScore() * data.getHexData().getTileType().getMultiplier();

                if(playScreen.getMap().scoreIsCalculated())
                    playScreen.getMoveEventManager()
                            .getScoreList().add(
                                    new IntPointTime(score, new Point(pos.x, pos.y), 0.35f)
                                    );

                hexpert.sounds.get("click").play(hexpert.masterVolume);

                if(!hexpert.config.isKeepSelection())
                    playStage.setSelection(null);

                playScreen.checkAchievements();

            }else{

                playScreen.getMoveEventManager().getBadMoves().add(new PointTime(new Point(pos.x,pos.y), 0.35f));
                hexpert.sounds.get("bad").play(hexpert.masterVolume);
            }
        }
        playScreen.updateScore();
        return true;
    }
}
