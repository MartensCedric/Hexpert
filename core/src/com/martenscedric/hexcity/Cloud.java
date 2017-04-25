package com.martenscedric.hexcity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by 1544256 on 2017-04-25.
 */
public class Cloud implements Drawable
{
    private Vector2 position = new Vector2();
    private TextureRegion textureRegion;

    private float speed;

    public Cloud(Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
        textureRegion = AssetLoader.assetManager.get("cloud");
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

    @Override
    public void draw(Batch batch) {
        batch.draw(textureRegion, getPosition().x, getPosition().y);
    }
}
