package com.martenscedric.hexcity;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Shawn Martens on 2017-04-25.
 */

public class AssetLoader
{
    public static AssetManager assetManager = new AssetManager();

    public static void load()
    {
        assetManager.load("sprites/cloud.png", Texture.class);
        assetManager.finishLoading();
    }
}
