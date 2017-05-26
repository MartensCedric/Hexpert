package com.martenscedric.hexpert.gestures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.WIDTH;

/**
 * Created by Shawn Martens on 2017-04-29.
 */

public class StandardGestureBehavior implements GestureDetector.GestureListener {

    private OrthographicCamera camera;
    private float BORDER_CONSTRAINT = 4;
    private float currentZoom;
    public StandardGestureBehavior(OrthographicCamera camera) {
        this.camera = camera;
        this.currentZoom = getCamera().zoom;
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
        getCamera().position.x += -deltaX * camera.zoom;
        getCamera().position.y += deltaY * camera.zoom;
        getCamera().update();

        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        currentZoom = getCamera().zoom;
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        getCamera().zoom = initialDistance / distance * currentZoom;

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
