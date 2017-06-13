package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.tile.BuildingType;

import static com.cedricmartens.hexpert.misc.TextureData.SPRITE_FOLDER;

/**
 * Created by martens on 6/13/17.
 */

public class BuildingScoreDialog extends TutorialDialog {
    public BuildingScoreDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin, false);
        
        getContentTable().defaults().pad(15);
        Label.LabelStyle lblStyle = skin.get("bigger", Label.LabelStyle.class);
        Label lblExplaination = new Label("When you place a building on a tile, your score will be modified by numbers associated to the building", skin);
        lblExplaination.setWrap(true);
        lblExplaination.setAlignment(Align.center);
        getContentTable().add(lblExplaination).colspan(8).width(8*160);
        lblExplaination.setWrap(true);
        getContentTable().row();

        for (int i = 1; i < BuildingType.values().length; i++) {
            BuildingType buildingType = BuildingType.values()[i];
            Image image = new Image(new TextureRegion((Texture)
                    hexpert.assetManager.get(SPRITE_FOLDER + buildingType.name().toLowerCase() + ".png")));

            getContentTable().add(image).width(160).height(160);
            Label lblScore = new Label(Integer.toString(buildingType.getScore()), lblStyle);
            lblScore.setAlignment(Align.center);
            getContentTable().add(lblScore).expand();
            if (i % 4 == 0)
                getContentTable().row();
        }
    }
}
