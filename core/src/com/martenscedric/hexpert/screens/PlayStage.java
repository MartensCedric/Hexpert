package com.martenscedric.hexpert.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.event.Action;
import com.martenscedric.hexpert.event.ActionDialog;
import com.martenscedric.hexpert.event.LevelComplete;
import com.martenscedric.hexpert.event.ObjectiveDialog;
import com.martenscedric.hexpert.event.OptionDialog;
import com.martenscedric.hexpert.map.MapUtils;
import com.martenscedric.hexpert.tile.BuildingType;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.Const.WIDTH;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MENUUI;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_OPTIONS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_UNDO;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_WIND;

/**
 * Created by martens on 5/28/17.
 */

public abstract class PlayStage extends StageScreen {

    private Table table, tableBtn;
    private Image menuImage;
    protected ImageButton btnFarm, btnHouse, btnMine, btnWind, btnFactory, btnMarket, btnBank, btnRocket,
            btnReset, btnUndo, btnBack, btnHelp, btnOptions;

    protected LevelComplete exitDialog;
    protected ObjectiveDialog objectiveDialog;
    private OptionDialog optionDialog;
    protected TextButton objectivesButton;
    private boolean drawerOpen = false;

    public PlayStage(final Hexpert hexpert) {
        super();
        this.objectiveDialog = new ObjectiveDialog(hexpert.getSkin(), hexpert);
        this.exitDialog = new LevelComplete(hexpert.getSkin(), hexpert);
        menuImage = new Image((Texture) hexpert.assetManager.get(TEXTURE_MENUUI));
        menuImage.setX(WIDTH - menuImage.getWidth());
        menuImage.setY(0);
        getStage().addActor(menuImage);

        TextureRegionDrawable drawableFarm = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM)));
        TextureRegionDrawable drawableHouse = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE)));
        TextureRegionDrawable drawableMine = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE)));
        TextureRegionDrawable drawableWind = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND)));
        TextureRegionDrawable drawableFactory = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY)));
        TextureRegionDrawable drawableMarket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET)));
        TextureRegionDrawable drawableBank = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK)));
        TextureRegionDrawable drawableRocket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET)));

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
        table.defaults().width(menuImage.getPrefWidth()).height(menuImage.getPrefHeight()/9).pad(5);

        table.add(btnFarm);
        ImageButton imbNotFarm = new ImageButton(drawableFarm);
        imbNotFarm.getImageCell().expand().fill();
        table.add(imbNotFarm);

        table.row();
        table.add(btnHouse);
        ImageButton imbFarm = new ImageButton(drawableFarm);
        imbFarm.getImageCell().expand().fill();
        table.add(imbFarm);

        table.row();
        table.add(btnMine);

        ImageButton imbHouse1 = new ImageButton(drawableHouse);
        imbHouse1.getImageCell().expand().fill();
        table.add(imbHouse1);

        table.row();
        table.add(btnWind);

        ImageButton imbHouse2 = new ImageButton(drawableHouse);
        imbHouse2.getImageCell().expand().fill();
        table.add(imbHouse2);

        table.row();
        table.add(btnFactory);

        ImageButton imbHouse3 = new ImageButton(drawableHouse);
        imbHouse3.getImageCell().expand().fill();
        table.add(imbHouse3);

        ImageButton imbMine1 = new ImageButton(drawableMine);
        imbMine1.getImageCell().expand().fill();
        table.add(imbMine1);

        ImageButton imbWind1 = new ImageButton(drawableWind);
        imbWind1.getImageCell().expand().fill();
        table.add(imbWind1);

        table.row();
        table.add(btnMarket);

        ImageButton imbHouse4 = new ImageButton(drawableHouse);
        imbHouse4.getImageCell().expand().fill();
        table.add(imbHouse4);

        ImageButton imbWind2 = new ImageButton(drawableWind);
        imbWind2.getImageCell().expand().fill();
        table.add(imbWind2);

        ImageButton imbFactory1 = new ImageButton(drawableFactory);
        imbFactory1.getImageCell().expand().fill();
        table.add(imbFactory1);


        table.row();
        table.add(btnBank);

        ImageButton imbHouse5 = new ImageButton(drawableHouse);
        imbHouse5.getImageCell().expand().fill();
        table.add(imbHouse5);

        ImageButton imbMine2 = new ImageButton(drawableMine);
        imbMine2.getImageCell().expand().fill();
        table.add(imbMine2);

        ImageButton imbWind3 = new ImageButton(drawableWind);
        imbWind3.getImageCell().expand().fill();
        table.add(imbWind3);

        ImageButton imbMarket1 = new ImageButton(drawableMarket);
        imbMarket1.getImageCell().expand().fill();
        table.add(imbMarket1);

        table.row();
        table.add(btnRocket);

        ImageButton imbHouse6 = new ImageButton(drawableHouse);
        imbHouse6.getImageCell().expand().fill();
        table.add(imbHouse6);

        ImageButton imbWind4 = new ImageButton(drawableWind);
        imbWind4.getImageCell().expand().fill();
        table.add(imbWind4);

        ImageButton imbFactory2 = new ImageButton(drawableFactory);
        imbFactory2.getImageCell().expand().fill();
        table.add(imbFactory2);

        ImageButton imbBank1 = new ImageButton(drawableBank);
        imbBank1.getImageCell().expand().fill();
        table.add(imbBank1);

        menuImage.setVisible(false);

        table.setX(WIDTH + menuImage.getWidth()/2 + table.getPrefWidth()/5);
        table.setY(HEIGHT - table.getPrefHeight()/2);

        table.setDebug(false);

        btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BACK))));
        btnBack.setX(5);
        btnBack.setY(HEIGHT - btnBack.getPrefHeight());
        btnBack.addListener(new ClickListener()
                            {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {

                                    Label text = new Label(hexpert.i18NBundle.get("confirm_quit"), hexpert.getSkin());
                                    ActionDialog actionDialog = new ActionDialog(text, new Action() {
                                        @Override
                                        public void doAction() {
                                            hexpert.setScreen(hexpert.levelSelectScreen);
                                        }
                                    }, hexpert.i18NBundle, hexpert.getSkin());

                                    actionDialog.show(getStage());
                                }
                            }
        );

        btnReset = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_RESET))));


        btnUndo = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_UNDO))));

        btnHelp = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HELP))));
        btnHelp.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.setX(table.getX() + (drawerOpen ? 900 : -900));
                drawerOpen = !drawerOpen;
            }
        });

        btnOptions = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_OPTIONS))));
        btnOptions.addListener(new ClickListener()
                               {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       optionDialog = new OptionDialog(hexpert, hexpert.getSkin());
                                       optionDialog.show(getStage());
                                   }
                               }
        );

        tableBtn = new Table();
        tableBtn.setY(HEIGHT - 150);
        tableBtn.setX(375);
        tableBtn.defaults().width(125).height(125).pad(15);

        btnBack.getImageCell().expand().fill();
        btnReset.getImageCell().expand().fill();
        btnUndo.getImageCell().expand().fill();
        btnHelp.getImageCell().expand().fill();
        btnOptions.getImageCell().expand().fill();

        objectivesButton = new TextButton(hexpert.i18NBundle.get("goals"), hexpert.getSkin());

        tableBtn.add(btnBack);
        tableBtn.add(btnReset);
        tableBtn.add(btnUndo);
        tableBtn.add(btnHelp);
        tableBtn.add(btnOptions);
        tableBtn.row();
        tableBtn.add(objectivesButton).colspan(4).width(500);

        getStage().addActor(table);
        getStage().addActor(tableBtn);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
