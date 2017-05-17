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

    public static BitmapFont font = null;

    public static Skin getSkin()
    {
        return new Skin(Gdx.files.internal("skins/flat-earth/flat-earth-ui.json"));
    }

    public static BitmapFont getFont() {
        if (font == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/VCROSDMono.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 35;
            parameter.borderWidth = 1;
            parameter.color = Color.BLACK;
            font = generator.generateFont(parameter);
            generator.dispose();
        }
        return font;
    }
}
