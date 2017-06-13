package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.tile.BuildingType;

import static com.cedricmartens.hexpert.misc.TextureData.SPRITE_FOLDER;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_NOT_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND;

/**
 * Created by martens on 6/13/17.
 */

public class BuildingScoreDialog extends TutorialDialog {
    public BuildingScoreDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin, false);

        getContentTable().defaults().width(160).height(160).pad(15);
        Label.LabelStyle lblStyle = skin.get("bigger", Label.LabelStyle.class);


        getContentTable().setDebug(true);
        Label lblExplaination = new Label("When you place a building on a tile, your score will be modified by numbers associated to the building", lblStyle);
        lblExplaination.setWrap(true);

        getContentTable().add(lblExplaination).colspan(4).expandX();
        getContentTable().row();

        for(int i = 1; i < BuildingType.values().length; i++)
        {
            BuildingType buildingType = BuildingType.values()[i];
            Image image = new Image(new TextureRegion((Texture)
                    hexpert.assetManager.get(SPRITE_FOLDER + buildingType.name().toLowerCase() + ".png")));

            getContentTable().add(image);
            Label lblScore = new Label(Integer.toString(buildingType.getScore()), lblStyle);

            getContentTable().add(lblScore).expand();
            if(i % 2 == 0)
                getContentTable().row();
        }
    }
}
