package com.martenscedric.hexcity;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import static com.martenscedric.hexcity.TextureData.TEXTURE_CLOUD;

/**
 * Created by Shawn Martens on 2017-04-25.
 */

public class AssetLoader
{
    public static AssetManager assetManager = new AssetManager();

    public static void load()
    {
        assetManager.load(TEXTURE_CLOUD, Texture.class);
        assetManager.finishLoading();
    }
}
