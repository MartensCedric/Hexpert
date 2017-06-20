package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.tile.BuildingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Created by martens on 6/11/17.
 */

public class BuildingReqDialog extends TutorialDialog {

    private final int PADDING = 15;
    private final int CELL_W = 100;
    private final int CELL_H = 100;

    public BuildingReqDialog(Hexpert hexpert, Skin skin, List<BuildingType> buildingsToShow)
    {
        super(hexpert, skin);

        scrollContent.defaults().pad(PADDING);

        I18NBundle i18n = hexpert.i18NBundle;

        Label lblExplaination = new Label(i18n.get("req_diag"), skin);
        lblExplaination.setAlignment(Align.center);
        lblExplaination.setWrap(true);

        scrollContent.add(lblExplaination).width(900);
        scrollContent.row();

        if(buildingsToShow.contains(BuildingType.FARM)) {

            Image imgFarm = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));
            Image imgNotFarm = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_NOT_FARM)));

            Table tableFarm = new Table();
            tableFarm.defaults().width(CELL_W).height(CELL_H).pad(PADDING);
            tableFarm.add(imgFarm);
            Label lblEqFarm = new Label("=", skin);
            tableFarm.add(lblEqFarm).width(lblEqFarm.getPrefWidth());
            tableFarm.add(imgNotFarm);
            scrollContent.add(tableFarm);
            scrollContent.row();
            scrollContent.add(new Label(i18n.get("farm_needs"), skin));
            scrollContent.row();

        }

        for(BuildingType buildingType : buildingsToShow)
        {

            if(buildingType == BuildingType.FARM)
                continue;

            Table table = new Table();
            table.defaults().width(CELL_W).height(CELL_H).pad(PADDING);

            String buildingName = buildingType.name().toLowerCase();
            Image imageBuilding = new Image((Texture) hexpert.assetManager.get(SPRITE_FOLDER + buildingName + ".png"));
            table.add(imageBuilding);
            Label lblEq = new Label("=", skin);
            table.add(lblEq).width(lblEq.getPrefWidth());

            int[] requirements = buildingType.getRequired();

            int numReq = 0;

            for(int j = 0; j < requirements.length; j++)
            {
                if(requirements[j] > 0)
                    numReq++;
            }

            int numDone = 0;
            for(int j = 0; j < requirements.length; j++)
            {
                if(requirements[j] > 0)
                {
                    BuildingType typeReq = BuildingType.values()[j + 1];

                    Image imageReq = new Image((Texture)hexpert.assetManager.get(SPRITE_FOLDER + typeReq.name().toLowerCase() + ".png"));

                    table.add(imageReq);
                    if(numDone + 1 < numReq)
                    {
                        Label lblPlus = new Label("+", skin);
                        table.add(lblPlus).width(lblPlus.getPrefWidth());
                    }
                    numDone++;
                }
            }

            scrollContent.add(table);
            scrollContent.row();
            scrollContent.add(new Label(i18n.get(buildingName + "_needs"), skin));
            scrollContent.row();
        }
    }

    public BuildingReqDialog(Hexpert hexpert, Skin skin) {
        this(hexpert, skin, Arrays.asList(
                BuildingType.FARM, BuildingType.HOUSE, BuildingType.MINE, BuildingType.WIND,
                BuildingType.FACTORY, BuildingType.MARKET, BuildingType.BANK, BuildingType.ROCKET));
    }

    @Override
    public float getPrefHeight() {

        int max = 900;
        if(super.getPrefHeight() < max)
            return super.getPrefHeight();

        return max;
    }
}
