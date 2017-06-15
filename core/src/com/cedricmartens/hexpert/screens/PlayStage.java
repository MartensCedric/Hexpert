package com.cedricmartens.hexpert.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cedricmartens.hexpert.event.HelpDialog;
import com.cedricmartens.hexpert.event.tutorial.BuildingReqDialog;
import com.cedricmartens.hexpert.event.tutorial.BuildingScoreDialog;
import com.cedricmartens.hexpert.event.tutorial.TileEffectDialog;
import com.cedricmartens.hexpert.misc.Action;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.MoreDialog;
import com.cedricmartens.hexpert.tile.BuildingType;

import java.util.HashMap;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MORE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_NOT_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_REMOVE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND;

/**
 * Created by martens on 5/28/17.
 */

public abstract class PlayStage extends StageScreen {

    protected Table table, tableBtn, tableRequirements, tableScore;
    protected ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket,
            btnMore, btnRemove, btnHelp;

    protected BuildingType selection;
    protected ImageButton selectedButton;
    public boolean removeMode = false;
    private Image imgFarm, imgHouse, imgMine, imgWind, imgFactory, imgMarket, imgBank, imgRocket, imgNotFarm;
    protected Hexpert hexpert;

    private HashMap<String, Action> onStartActions = new HashMap<>();

    public PlayStage(final Hexpert hexpert) {
        super();
        this.hexpert = hexpert;

        onStartActions.put("tutFact", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(), 5).show(getStage());
            }
        });

        onStartActions.put("tutMarket", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(), 6).show(getStage());
            }
        });

        onStartActions.put("tutBank", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(), 7).show(getStage());
            }
        });

        onStartActions.put("handball", new Action() {
            @Override
            public void doAction() {
                new BuildingScoreDialog(hexpert, hexpert.getSkin()).show(getStage());
            }
        });

        onStartActions.put("holywater", new Action() {
            @Override
            public void doAction() {
                new TileEffectDialog(hexpert, hexpert.getSkin()).show(getStage());
            }
        });

        TextureRegionDrawable drawableFarm = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));
        TextureRegionDrawable drawableHouse = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        TextureRegionDrawable drawableMine = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));
        TextureRegionDrawable drawableWind = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        TextureRegionDrawable drawableFactory = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        TextureRegionDrawable drawableMarket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));
        TextureRegionDrawable drawableBank = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));
        TextureRegionDrawable drawableRocket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET)));

        TextureRegionDrawable drawableNotFarm = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_NOT_FARM)));

        imgFarm = new Image(drawableFarm);
        imgHouse = new Image(drawableHouse);
        imgMine = new Image(drawableMine);
        imgWind = new Image(drawableWind);
        imgFactory = new Image(drawableFactory);
        imgMarket = new Image(drawableMarket);
        imgBank = new Image(drawableBank);
        imgRocket = new Image(drawableRocket);
        imgNotFarm = new Image(drawableNotFarm);

        btnFarm = new ImageButton(drawableFarm);
        btnFarm.getImageCell().expand().fill();

        btnHouse = new ImageButton(drawableHouse);
        btnHouse.getImageCell().expand().fill();

        btnMine = new ImageButton(drawableMine);
        btnMine.getImageCell().expand().fill();

        btnWind = new ImageButton(drawableWind);
        btnWind.getImageCell().expand().fill();

        btnFactory = new ImageButton(drawableFactory);
        btnFactory.getImageCell().expand().fill();

        btnMarket = new ImageButton(drawableMarket);
        btnMarket.getImageCell().expand().fill();

        btnBank = new ImageButton(drawableBank);
        btnBank.getImageCell().expand().fill();

        btnRocket = new ImageButton(drawableRocket);
        btnRocket.getImageCell().expand().fill();

        table = new Table();
        table.defaults().width(200).height(Const.HEIGHT/9).pad(5);

        table.add(btnFarm);
        table.row();

        table.add(btnHouse);
        table.row();

        table.add(btnMine);
        table.row();

        table.add(btnWind);
        table.row();

        table.add(btnFactory);
        table.row();

        table.add(btnMarket);
        table.row();

        table.add(btnBank);
        table.row();

        table.add(btnRocket);

        table.setX(Const.WIDTH - 100);
        table.setY(Const.HEIGHT - table.getPrefHeight()/2 - 5);

        table.setDebug(false);

        btnMore = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MORE))));

        btnMore.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new MoreDialog(hexpert, PlayStage.this, hexpert.getSkin()).show(getStage());
            }
        });


        btnRemove = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_REMOVE))));

        btnRemove.addListener(new ClickListener()
          {
              @Override
              public void clicked(InputEvent event, float x, float y) {
                  removeMode = !removeMode;
                  setSelection(null);
              }
          }
        );

        btnHelp = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HELP))));
        btnHelp.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new HelpDialog(hexpert, hexpert.getSkin()).show(getStage());
            }
        });

        tableBtn = new Table();
        tableBtn.setY(Const.HEIGHT - 85);
        tableBtn.setX(235);
        tableBtn.defaults().width(125).height(125).pad(15);

        btnMore.getImageCell().expand().fill();
        btnRemove.getImageCell().expand().fill();
        btnHelp.getImageCell().expand().fill();

        tableBtn.add(btnMore);
        tableBtn.add(btnRemove);
        tableBtn.add(btnHelp);

        tableRequirements = new Table();
        tableRequirements.defaults().height(100).pad(0, 5, 0, 5);
        tableRequirements.setHeight(105);
        tableRequirements.setX(Const.WIDTH/2);
        setBuilding(null);

        tableScore = new Table();
        tableScore.setX(180);
        tableScore.setY(125);
        tableScore.defaults().width(350).height(100);

        Label.LabelStyle lblStyle = hexpert.getSkin().get("bigger", Label.LabelStyle.class);
        tableScore.add(new Label("", lblStyle));
        tableScore.row();
        tableScore.add(new Label("", lblStyle));

        getStage().addActor(table);
        getStage().addActor(tableBtn);
        getStage().addActor(tableRequirements);
        getStage().addActor(tableScore);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    public void setBuilding(BuildingType selection)
    {
        tableRequirements.clearChildren();

        if(selection != null)
        {
            tableRequirements.add(getImgBySelection(selection)).width(100);
            tableRequirements.add(new Label("=", hexpert.getSkin()));

            switch (selection) {
                case NONE:
                    break;
                case FARM:
                    tableRequirements.add(imgNotFarm).width(100);
                    break;
                case HOUSE:
                    tableRequirements.add(imgFarm).width(100);
                    break;
                case MINE:
                    tableRequirements.add(imgHouse).width(100);
                    break;
                case WIND:
                    tableRequirements.add(imgHouse).width(100);
                    break;
                case FACTORY:
                    tableRequirements.add(imgHouse).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgMine).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgWind).width(100);
                    break;
                case MARKET:
                    tableRequirements.add(imgHouse).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgWind).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgFactory).width(100);
                    break;
                case BANK:
                    tableRequirements.add(imgHouse).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgMine).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgWind).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgMarket).width(100);
                    break;
                case ROCKET:
                    tableRequirements.add(imgHouse).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgWind).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgFactory).width(100);
                    tableRequirements.add(new Label("+", hexpert.getSkin()));
                    tableRequirements.add(imgBank).width(100);
                    break;
            }
        }
    }

    private Image getImgBySelection(BuildingType selection)
    {
        switch (selection) {
            case NONE:
                break;
            case FARM:
                return imgFarm;
            case HOUSE:
                return imgHouse;
            case MINE:
                return imgMine;
            case WIND:
                return imgWind;
            case FACTORY:
                return imgFactory;
            case MARKET:
                return imgMarket;
            case BANK:
                return imgBank;
            case ROCKET:
                return imgRocket;
        }
        return null;
    }

    public void setSelection(BuildingType selection) {


        this.selection = selection;
        if(selection == null)
            selectedButton = null;
        else removeMode = false;

        if(hexpert.config.isShowRequirements())
            setBuilding(selection);

    }

    public Hexpert getHexpert() {
        return hexpert;
    }

    protected void doAction(String lvlName)
    {
        if(onStartActions.containsKey(lvlName))
        {
            onStartActions.get(lvlName).doAction();
        }
    }
}
