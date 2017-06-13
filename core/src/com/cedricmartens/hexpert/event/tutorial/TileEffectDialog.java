package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.tile.TileType;

import static com.cedricmartens.hexpert.misc.TextureData.SPRITE_FOLDER;

/**
 * Created by martens on 6/13/17.
 */

public class TileEffectDialog extends TutorialDialog {
    public TileEffectDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin, false);
        getContentTable().defaults().pad(15);

        for(int i = 0; i < TileType.values().length; i++)
        {
            TileType tileType = TileType.values()[i];
            Image tileImage = new Image(new TextureRegion((Texture) hexpert.assetManager.get(SPRITE_FOLDER + tileType.name().toLowerCase() + "_tile.png")));
            getContentTable().add(tileImage).width(96).height(96);

            Label lblDesc = new Label(hexpert.i18NBundle.get(tileType.name().toLowerCase() + "_effect"), skin);
            lblDesc.setAlignment(Align.center);
            getContentTable().add(lblDesc);
            getContentTable().row();
        }
    }
}
