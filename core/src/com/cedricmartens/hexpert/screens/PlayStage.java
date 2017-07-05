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
import com.badlogic.gdx.utils.Align;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.HelpDialog;
import com.cedricmartens.hexpert.event.MoreDialog;
import com.cedricmartens.hexpert.event.tutorial.BuildingReqDialog;
import com.cedricmartens.hexpert.event.tutorial.BuildingScoreDialog;
import com.cedricmartens.hexpert.event.tutorial.LockedTutDialog;
import com.cedricmartens.hexpert.event.tutorial.TileEffectDialog;
import com.cedricmartens.hexpert.misc.Action;
import com.cedricmartens.hexpert.misc.Const;
import com.cedricmartens.hexpert.tile.BuildingType;

import java.util.Arrays;
import java.util.HashMap;

import static com.cedricmartens.hexpert.misc.Const.HEIGHT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BANK_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FACTORY_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_FARM_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HELP;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_HOUSE_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MARKET_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MINE_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_MORE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_NOT_FARM;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_REMOVE;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ROCKET_MIN;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_WIND_MIN;

/**
 * Created by martens on 5/28/17.
 */

public abstract class PlayStage extends StageScreen {

    protected Table table, tableBtn, tableRequirements, tableScore;
    protected ImageButton btnMore, btnRemove, btnHelp;

    protected Image nextObjectiveImage;

    protected BuildingType selection;
    protected ImageButton selectedButton;
    protected Label lblNextObjective;
    public boolean removeMode = false;
    private Image imgFarm, imgHouse, imgMine, imgWind, imgFactory, imgMarket, imgBank, imgRocket, imgNotFarm;
    protected Hexpert hexpert;

    private HashMap<String, Action> onStartActions = new HashMap<>();;

    public PlayStage(final Hexpert hexpert) {
        super();
        this.hexpert = hexpert;

        onStartActions.put("tutFact", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(),
                        Arrays.asList(BuildingType.FARM, BuildingType.HOUSE,
                                BuildingType.MINE, BuildingType.WIND,
                                BuildingType.FACTORY)).show(getStage());
            }
        });

        onStartActions.put("tutMarket", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(),
                        Arrays.asList(BuildingType.MARKET)).show(getStage());
            }
        });

        onStartActions.put("tutBank", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(), Arrays.asList(BuildingType.BANK)).show(getStage());
            }
        });


        onStartActions.put("tutRocket", new Action() {
            @Override
            public void doAction() {
                new BuildingReqDialog(hexpert, hexpert.getSkin(), Arrays.asList(BuildingType.ROCKET)).show(getStage());
            }
        });


        onStartActions.put("carrot", new Action() {
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

        onStartActions.put("triBankLckd", new Action() {
            @Override
            public void doAction() {
                new LockedTutDialog(hexpert, hexpert.getSkin()).show(getStage());
            }
        });

        TextureRegionDrawable drawableFarm = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FARM_MIN)));
        TextureRegionDrawable drawableHouse = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_HOUSE_MIN)));
        TextureRegionDrawable drawableMine = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MINE_MIN)));
        TextureRegionDrawable drawableWind = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_WIND_MIN)));
        TextureRegionDrawable drawableFactory = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_FACTORY_MIN)));
        TextureRegionDrawable drawableMarket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_MARKET_MIN)));
        TextureRegionDrawable drawableBank = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BANK_MIN)));
        TextureRegionDrawable drawableRocket = new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ROCKET_MIN)));

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
        tableBtn.setY(HEIGHT - 150);
        tableBtn.setX(235);
        tableBtn.defaults().width(125).height(125).pad(15);

        btnMore.getImageCell().expand().fill();
        btnRemove.getImageCell().expand().fill();
        btnHelp.getImageCell().expand().fill();

        tableBtn.add(btnMore);
        tableBtn.add(btnRemove);
        tableBtn.add(btnHelp);
        tableBtn.row();
        lblNextObjective = new Label("", hexpert.getSkin());
        lblNextObjective.setWrap(true);
        nextObjectiveImage = new Image();
        tableBtn.add(nextObjectiveImage).width(48);
        tableBtn.add(lblNextObjective).width(lblNextObjective.getPrefWidth()).left();

        tableRequirements = new Table();
        tableRequirements.defaults().height(150).pad(0, 5, 0, 5);
        tableRequirements.setX(Const.WIDTH/2 + 100);
        tableRequirements.setY(HEIGHT - 105);
        setBuilding(null);

        tableScore = new Table();
        tableScore.setX(180);
        tableScore.setY(125);
        tableScore.defaults().width(350).height(100);

        Label.LabelStyle lblStyle = hexpert.getSkin().get("bigger", Label.LabelStyle.class);
        tableScore.add(new Label("", lblStyle));
        tableScore.row();
        tableScore.add(new Label("", lblStyle));

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
            Label.LabelStyle lblStyle = hexpert.getSkin().get("bigger", Label.LabelStyle.class);
            int width = 150;
            tableRequirements.add(getImgBySelection(selection)).width(width);
            tableRequirements.add(new Label("=", lblStyle));

            switch (selection) {
                case NONE:
                    break;
                case FARM:
                    tableRequirements.add(imgNotFarm).width(width);
                    break;
                case HOUSE:
                    tableRequirements.add(imgFarm).width(width);
                    break;
                case MINE:
                    tableRequirements.add(imgHouse).width(width);
                    break;
                case WIND:
                    tableRequirements.add(imgHouse).width(width);
                    break;
                case FACTORY:
                    tableRequirements.add(imgHouse).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgMine).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgWind).width(width);
                    break;
                case MARKET:
                    tableRequirements.add(imgHouse).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgWind).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgFactory).width(width);
                    break;
                case BANK:
                    tableRequirements.add(imgHouse).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgMine).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgWind).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgMarket).width(width);
                    break;
                case ROCKET:
                    tableRequirements.add(imgHouse).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgWind).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgFactory).width(width);
                    tableRequirements.add(new Label("+", lblStyle));
                    tableRequirements.add(imgBank).width(width);
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

    protected Action getAction(String lvlName)
    {
        if(onStartActions.containsKey(lvlName))
        {
            return onStartActions.get(lvlName);
        }
        return null;
    }

    protected int getBuildingCountByLevel(String mapName)
    {
        if(mapName.equals("tutFact"))
            return 5;
        else if(mapName.equals("tutMarket"))
            return 6;
        else if(mapName.equals("tutBank"))
            return 7;
        else return  Const.BUILDING_COUNT;
    }
}
