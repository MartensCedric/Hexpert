package com.cedricmartens.hexpert.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 6/19/17.
 */

public class BuildingAnimator
{
    private int currentTick = 0;
    private float time;
    private float interval;
    private int frames;
    private String spritePath;
    private Hexpert hexpert;

    public BuildingAnimator(int interval, int frames, String spritePath, Hexpert hexpert) {
        this.hexpert = hexpert;
        this.interval = interval;
        this.frames = frames;
        this.spritePath = spritePath;
        time = 0;
    }

    public void tick(float delta)
    {
        time += delta;

        if(time >= frames * interval)
        {
            time -= frames * interval;
        }
    }

    public Texture getTexture()
    {
        return hexpert.assetManager.get(getCurrentFrame() == 1 ?
                spritePath : spritePath + Integer.toString(getCurrentFrame()));
    }

    private int getCurrentFrame()
    {
        return (int) (time/interval) + 1;
    }
}
