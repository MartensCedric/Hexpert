package com.cedricmartens.hexpert.effect;

import com.badlogic.gdx.graphics.Texture;
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

    public BuildingAnimator(float interval, int frames, String spritePath, Hexpert hexpert) {
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
        String beforeExt = spritePath.split("\\.")[0];
        String ext = spritePath.split("\\.")[1];
        return hexpert.assetManager.get(getCurrentFrame() == 1 ?
                spritePath : beforeExt + Integer.toString(getCurrentFrame()) + "." + ext);
    }

    private int getCurrentFrame()
    {
        return (int) (time/interval) + 1;
    }
}
