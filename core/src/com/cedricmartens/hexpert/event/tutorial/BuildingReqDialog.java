package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cedricmartens.hexpert.Hexpert;

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
    public BuildingReqDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin);

        scrollContent.setDebug(true);

        Image imgFarm = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));
        Image imgFarm2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));

        Image imgNotFarm = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_NOT_FARM)));

        Image imgHouse = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse3 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse4 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse5 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse6 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        Image imgHouse7 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));

        Image imgMine = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));
        Image imgMine2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));
        Image imgMine3 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));

        Image imgWind = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        Image imgWind2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        Image imgWind3 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        Image imgWind4 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        Image imgWind5 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        Image imgWind6 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));

        Image imgFact = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        Image imgFact2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        Image imgFact3 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));

        Image imgMarket = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));
        Image imgMarket2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));

        Image imgBank = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));
        Image imgBank2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));

        Image imgRocket = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET)));


        scrollContent.add(imgFarm).width(100).height(100);
        scrollContent.add(imgNotFarm).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A farm can't be placed next to another farm", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgHouse).width(100).height(100);
        scrollContent.add(imgFarm2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A house needs a farm", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgMine).width(100).height(100);
        scrollContent.add(imgHouse2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A mine needs a house", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgWind).width(100).height(100);
        scrollContent.add(imgHouse3).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A wind turbine needs a house", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgFact).width(100).height(100);
        scrollContent.add(imgHouse4).width(100).height(100);
        scrollContent.add(imgMine2).width(100).height(100);
        scrollContent.add(imgWind2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A factory needs a house, a mine and a wind turbine", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgMarket).width(100).height(100);
        scrollContent.add(imgHouse5).width(100).height(100);
        scrollContent.add(imgWind3).width(100).height(100);
        scrollContent.add(imgFact2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A market needs a house, a wind turbine and a factory", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgBank).width(100).height(100);
        scrollContent.add(imgHouse6).width(100).height(100);
        scrollContent.add(imgWind4).width(100).height(100);
        scrollContent.add(imgMine3).width(100).height(100);
        scrollContent.add(imgMarket2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A market needs a house, a wind turbine and a factory", skin)).colspan(5);
        scrollContent.row();

        scrollContent.add(imgRocket).width(100).height(100);
        scrollContent.add(imgHouse7).width(100).height(100);
        scrollContent.add(imgWind5).width(100).height(100);
        scrollContent.add(imgFact3).width(100).height(100);
        scrollContent.add(imgBank2).width(100).height(100);
        scrollContent.row();
        scrollContent.add(new Label("A rocket needs a house, a wind turbine, a factory and a bank", skin)).colspan(5);
        scrollContent.row();
    }

    @Override
    public float getPrefWidth() {
        return super.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        return 800;
    }
}
