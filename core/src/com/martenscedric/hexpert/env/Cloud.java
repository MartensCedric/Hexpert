package com.martenscedric.hexpert.env;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by 1544256 on 2017-04-25.
 */
public class Cloud
{
    private Vector2 position = new Vector2();

    private float speed;

    public Cloud(Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
