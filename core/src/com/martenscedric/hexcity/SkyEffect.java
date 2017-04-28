package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.martenscedric.hexcity.TextureData.TEXTURE_CLOUD;

/**
 * Created by 1544256 on 2017-04-25.
 */

public class SkyEffect implements Tickable, Drawable
{
    private final int START_CLOUD_COUNT = 6;
    private final float CLOUD_SPEED_MIN = 0.275f;
    private final float CLOUD_SPEED_MAX = 0.325f;

    private static Random r = new Random();
    private Texture cloudTexture;

    public List<Cloud> clouds = new ArrayList<Cloud>();

    public SkyEffect(AssetManager assetManager)
    {
        this.cloudTexture = assetManager.get(TEXTURE_CLOUD);
        createClouds();
    }

    private void createClouds()
    {
        for(int i = 0; i < START_CLOUD_COUNT; i++) {
            float x = Math.abs(r.nextInt() % Gdx.graphics.getWidth());
            float y = Math.abs(r.nextInt() % Gdx.graphics.getHeight());
            float speed = Math.max(r.nextFloat() * CLOUD_SPEED_MAX, CLOUD_SPEED_MIN);
            Cloud cloud = new Cloud(new Vector2(x, y), speed);
            clouds.add(cloud);
        }
    }

    private void tickClouds()
    {
        for(int i = 0; i < clouds.size(); i++)
        {
            Cloud c = clouds.get(i);

            if(c.getPosition().x > Gdx.graphics.getWidth())
            {
                c.getPosition().x = -cloudTexture.getWidth()*3;
            }else{
                c.getPosition().x += c.getSpeed();
            }
        }
    }

    @Override
    public void tick() {
        tickClouds();
    }

    @Override
    public void draw(Batch batch) {
        for(Cloud c : clouds)
        {
            batch.draw(cloudTexture, c.getPosition().x, c.getPosition().y, cloudTexture.getWidth()*3, cloudTexture.getHeight()*3);
        }
    }
}


