package com.martenscedric.hexcity;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by 1544256 on 2017-04-26.
 */

public abstract class StageScreen implements Screen
{
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
