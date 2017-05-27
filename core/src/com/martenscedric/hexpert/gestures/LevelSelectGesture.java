package com.martenscedric.hexpert.gestures;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.screens.LevelSelectScreen;

/**
 * Created by martens on 5/22/17.
 */

public class LevelSelectGesture  implements GestureDetector.GestureListener
{
    private LevelSelectScreen levelSelectScreen;
    private HexMap<Texture> grid;
    private Stage stage;
    private OrthographicCamera camera;
    private OrthographicCamera displayLevelCam;

    public LevelSelectGesture(LevelSelectScreen levelSelectScreen) {
        this.levelSelectScreen = levelSelectScreen;
        this.grid = levelSelectScreen.getLvlSelectGrid();
        this.stage = levelSelectScreen.getStage();
        this.camera = levelSelectScreen.getCamera();
        this.displayLevelCam = levelSelectScreen.getDisplayLevelCam();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        Viewport viewPort = stage.getViewport();
        Vector3 pos = camera.unproject(new Vector3(x,y,0),
                viewPort.getScreenX(), viewPort.getScreenY(),
                viewPort.getScreenWidth(), viewPort.getScreenHeight());

        Hexagon<Texture> hex = grid.getAt(new Point(pos.x, pos.y));

        if(hex != null)
        {
            int index = hex.getCoordinateSystem().toIndexed().getIndex();

            if(index == 0)
            {
                if(levelSelectScreen.currentWorld > 1)
                {
                    levelSelectScreen.previousWorld();
                }

            }else if(index == levelSelectScreen.getLevelsToDisplay() + 1)
            {
                if(!levelSelectScreen.isLastWorld(levelSelectScreen.currentWorld))
                {
                    levelSelectScreen.nextWorld();
                }

            }else{
                levelSelectScreen.selectLevel(
                        (levelSelectScreen.currentWorld - 1) * levelSelectScreen.getLevelsToDisplay()
                                + hex.getCoordinateSystem().toIndexed().getIndex());
                levelSelectScreen.hexpert.sounds.get("select").play();
            }
        }else{
            pos = displayLevelCam.unproject(new Vector3(x, y, 0),
                        viewPort.getScreenX(), viewPort.getScreenY(),
                    viewPort.getScreenWidth(), viewPort.getScreenHeight());

            if(levelSelectScreen.getMapCollision().contains(new Vector2(pos.x, pos.y)))
            {
                levelSelectScreen.goToLevel();
            }
        }

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
