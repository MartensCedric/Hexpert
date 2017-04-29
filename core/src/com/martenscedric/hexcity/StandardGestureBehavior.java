package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Shawn Martens on 2017-04-29.
 */

public class StandardGestureBehavior implements GestureDetector.GestureListener {

    private OrthographicCamera camera;
    private float BORDER_CONSTRAINT = 4;
    public StandardGestureBehavior(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
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
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        getCamera().translate(-deltaX * camera.zoom, deltaY * camera.zoom);
        if(getCamera().position.x < Gdx.graphics.getWidth()/4)
        {
            getCamera().position.x = Gdx.graphics.getWidth()/BORDER_CONSTRAINT;
        }else if(getCamera().position.x > Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/BORDER_CONSTRAINT)
        {
            getCamera().position.x = Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/BORDER_CONSTRAINT;
        }else if(getCamera().position.y < Gdx.graphics.getHeight()/BORDER_CONSTRAINT)
        {
            getCamera().position.y = Gdx.graphics.getHeight()/BORDER_CONSTRAINT;
        }else if(getCamera().position.y > Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/BORDER_CONSTRAINT)
        {
            getCamera().position.y = Gdx.graphics.getHeight() - Gdx.graphics.getHeight()/BORDER_CONSTRAINT;
        }
            getCamera().update();


        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        float ratio = initialDistance / distance;

        if(ratio < 1)
            ratio = Math.max(ratio, 0.98f);
        else if(ratio > 1)
            ratio = Math.min(ratio, 1.02f);

        getCamera().zoom =  ratio * getCamera().zoom;

        if(getCamera().zoom < 0.25f)
            getCamera().zoom = 0.25f;
        else if(getCamera().zoom > 1.2f)
            getCamera().zoom = 1.2f;

        getCamera().update();
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
