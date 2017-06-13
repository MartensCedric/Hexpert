package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
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

    private final int PADDING = 15;
    private final int CELL_W = 100;
    private final int CELL_H = 100;

    public BuildingReqDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin);

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

        scrollContent.defaults().pad(PADDING);
        I18NBundle i18n = hexpert.i18NBundle;

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

        Table tableHouse = new Table();
        tableHouse.defaults().width(CELL_W).height(CELL_H).pad(PADDING);
        tableHouse.add(imgHouse);
        Label lblEqHouse = new Label("=", skin);
        tableHouse.add(lblEqHouse).width(lblEqHouse.getPrefWidth());
        tableHouse.add(imgFarm2);
        scrollContent.add(tableHouse);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("house_needs"), skin));
        scrollContent.row();

        Table tableMine = new Table();
        tableMine.defaults().width(CELL_W).height(CELL_H).pad(PADDING);
        tableMine.add(imgMine);
        Label lblEqMine = new Label("=", skin);
        tableMine.add(lblEqMine).width(lblEqMine.getPrefWidth());
        tableMine.add(imgHouse2);
        scrollContent.add(tableMine);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("mine_needs"), skin));
        scrollContent.row();

        Table tableWind = new Table();
        tableWind.defaults().width(CELL_W).height(CELL_H).pad(PADDING);
        tableWind.add(imgWind);
        Label lblEqWind = new Label("=", skin);
        tableWind.add(lblEqWind).width(lblEqWind.getPrefWidth());
        tableWind.add(imgHouse3);
        scrollContent.add(tableWind);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("wind_needs"), skin));
        scrollContent.row();

        Table tableFact = new Table();
        tableFact.defaults().width(CELL_W).height(CELL_H).pad(PADDING);
        tableFact.add(imgFact);
        Label lblEqFact = new Label("=", skin);
        tableFact.add(lblEqFact).width(lblEqFact.getPrefWidth());
        Label lblAddFact = new Label("+", skin);
        Label lblAddFact2 = new Label("+", skin);
        tableFact.add(imgHouse4);
        tableFact.add(lblAddFact).width(lblAddFact.getPrefWidth());
        tableFact.add(imgMine2);
        tableFact.add(lblAddFact2).width(lblAddFact2.getPrefWidth());
        tableFact.add(imgWind2);

        scrollContent.add(tableFact);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("fact_needs"), skin));
        scrollContent.row();

        Table tableMarket = new Table();
        tableMarket.defaults().width(CELL_W).height(CELL_H).pad(PADDING);

        tableMarket.add(imgMarket);
        Label lblEqMarket = new Label("=", skin);
        tableMarket.add(lblEqMarket).width(lblEqMarket.getPrefWidth());
        Label lblAddMarket = new Label("+", skin);
        Label lblAddMarket2 = new Label("+", skin);

        tableMarket.add(imgHouse5);
        tableMarket.add(lblAddMarket).width(lblAddMarket.getPrefWidth());
        tableMarket.add(imgWind3);
        tableMarket.add(lblAddMarket2).width(lblAddMarket2.getPrefWidth());
        tableMarket.add(imgFact2);

        scrollContent.add(tableMarket);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("market_needs"), skin));
        scrollContent.row();

        Table tableBank = new Table();
        tableBank.defaults().width(CELL_W).height(CELL_H).pad(PADDING);

        tableBank.add(imgBank);
        Label lblEqBank = new Label("=", skin);
        tableBank.add(lblEqBank).width(lblEqBank.getPrefWidth());
        tableBank.add(imgHouse6);

        Label lblAddBank = new Label("+", skin);
        Label lblAddBank2 = new Label("+", skin);
        Label lblAddBank3 = new Label("+", skin);
        tableBank.add(lblAddBank).width(lblAddBank.getPrefWidth());
        tableBank.add(imgWind4);
        tableBank.add(lblAddBank2).width(lblAddBank2.getPrefWidth());
        tableBank.add(imgMine3);
        tableBank.add(lblAddBank3).width(lblAddBank3.getPrefWidth());
        tableBank.add(imgMarket2);

        scrollContent.add(tableBank);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("bank_needs"), skin));
        scrollContent.row();

        Table tableRocket = new Table();
        tableRocket.defaults().width(CELL_W).height(CELL_H).pad(PADDING);

        tableRocket.add(imgRocket);
        Label lblEqRocket = new Label("=", skin);
        tableRocket.add(lblEqRocket).width(lblEqRocket.getPrefWidth());
        tableRocket.add(imgHouse7);
        Label lblAddRocket = new Label("+", skin);
        Label lblAddRocket2 = new Label("+", skin);
        Label lblAddRocket3 = new Label("+", skin);
        tableRocket.add(lblAddRocket).width(lblAddRocket.getPrefWidth());
        tableRocket.add(imgWind5);
        tableRocket.add(lblAddRocket2).width(lblAddRocket2.getPrefWidth());
        tableRocket.add(imgFact3);
        tableRocket.add(lblAddRocket3).width(lblAddRocket3.getPrefWidth());
        tableRocket.add(imgBank2);

        scrollContent.add(tableRocket);
        scrollContent.row();
        scrollContent.add(new Label(i18n.get("rocket_needs"), skin)).colspan(5);
        scrollContent.row();
    }

    @Override
    public float getPrefHeight() {
        return 900;
    }
}
