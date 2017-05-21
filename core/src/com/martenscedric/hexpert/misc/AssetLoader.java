package com.martenscedric.hexpert.misc;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Shawn Martens on 2017-04-25.
 */

public class AssetLoader
{
    public static Skin getSkin()
    {
        return new Skin(Gdx.files.internal("skins/hexpert/hexpert.json"));
    }
}
