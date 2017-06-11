package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

        Image imgFact = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        Image imgFact2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        Image imgFact3 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));

        Image imgMarket = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));
        Image imgMarket2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));

        Image imgBank = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));
        Image imgBank2 = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));

        Image imgRocket = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET)));

        scrollContent.defaults().pad(15);

        Table tableFarm = new Table();
        tableFarm.defaults().width(100).height(100).pad(15);
        tableFarm.add(imgFarm);
        tableFarm.add(new Label("=", skin));
        tableFarm.add(imgNotFarm);
        scrollContent.add(tableFarm);
        scrollContent.row();
        scrollContent.add(new Label("A farm can't be placed next to another farm", skin));
        scrollContent.row();

        Table tableHouse = new Table();
        tableHouse.defaults().width(100).height(100).pad(15);
        tableHouse.add(imgHouse);
        tableHouse.add(new Label("=", skin));
        tableHouse.add(imgFarm2);
        scrollContent.add(tableHouse);
        scrollContent.row();
        scrollContent.add(new Label("A house needs a farm", skin));
        scrollContent.row();

        Table tableMine = new Table();
        tableMine.defaults().width(100).height(100).pad(15);
        tableMine.add(imgMine);
        tableMine.add(new Label("=", skin));
        tableMine.add(imgHouse2);
        scrollContent.add(tableMine);
        scrollContent.row();
        scrollContent.add(new Label("A mine needs a house", skin));
        scrollContent.row();

        Table tableWind = new Table();
        tableWind.defaults().width(100).height(100).pad(15);
        tableWind.add(imgWind);
        tableWind.add(new Label("=", skin));
        tableWind.add(imgHouse3);
        scrollContent.add(tableWind);
        scrollContent.row();
        scrollContent.add(new Label("A wind turbine needs a house", skin));
        scrollContent.row();

        Table tableFact = new Table();
        tableFact.defaults().width(100).height(100).pad(15);
        tableFact.add(imgFact);
        tableFact.add(new Label("=", skin));
        tableFact.add(imgHouse4);
        tableFact.add(new Label("+", skin));
        tableFact.add(imgMine2);
        tableFact.add(new Label("+", skin));
        tableFact.add(imgWind2);

        scrollContent.add(tableFact);
        scrollContent.row();
        scrollContent.add(new Label("A factory needs a house, a mine and a wind turbine", skin));
        scrollContent.row();

        Table tableMarket = new Table();
        tableMarket.defaults().width(100).height(100).pad(15);

        tableMarket.add(imgMarket);
        tableMarket.add(new Label("=", skin));
        tableMarket.add(imgHouse5);
        tableMarket.add(new Label("+", skin));
        tableMarket.add(imgWind3);
        tableMarket.add(new Label("+", skin));
        tableMarket.add(imgFact2);

        scrollContent.add(tableMarket);
        scrollContent.row();
        scrollContent.add(new Label("A market needs a house, a wind turbine and a factory", skin));
        scrollContent.row();

        Table tableBank = new Table();
        tableBank.defaults().width(100).height(100).pad(15);

        tableBank.add(imgBank);
        tableBank.add(new Label("=", skin));
        tableBank.add(imgHouse6);
        tableBank.add(new Label("+", skin));
        tableBank.add(imgWind4);
        tableBank.add(new Label("+", skin));
        tableBank.add(imgMine3);
        tableBank.add(new Label("+", skin));
        tableBank.add(imgMarket2);

        scrollContent.add(tableBank);
        scrollContent.row();
        scrollContent.add(new Label("A market needs a house, a wind turbine and a factory", skin));
        scrollContent.row();

        Table tableRocket = new Table();
        tableRocket.defaults().width(100).height(100).pad(15);

        tableRocket.add(imgRocket);
        tableRocket.add(new Label("=", skin));
        tableRocket.add(imgHouse7);
        tableRocket.add(new Label("+", skin));
        tableRocket.add(imgWind5);
        tableRocket.add(new Label("+", skin));
        tableRocket.add(imgFact3);
        tableRocket.add(new Label("+", skin));
        tableRocket.add(imgBank2);

        scrollContent.add(tableRocket);
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
