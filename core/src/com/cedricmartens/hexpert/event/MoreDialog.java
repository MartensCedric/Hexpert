package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.misc.ActionDialog;
import com.cedricmartens.hexpert.misc.Action;
import com.cedricmartens.hexpert.screens.PlayScreen;
import com.cedricmartens.hexpert.screens.PlayStage;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_CORRECT;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_LEADERBOARD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_OPTIONS;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_RESET;

/**
 * Created by martens on 6/10/17.
 */

public class MoreDialog extends StandardDialog {
    private ImageButton btnBack, btnReset, btnOptions, btnLeaderboard, btnGoals;
    private Hexpert hexpert;
    private PlayStage playStage;

    public MoreDialog(final Hexpert hexpert, final PlayStage playStage, Skin skin) {
        super(hexpert, skin);

        this.playStage = playStage;
        this.hexpert = hexpert;
        getContentTable().defaults().width(125).height(125).pad(15);
        btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BACK))));
        btnBack.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                 backToMenu();
                }
            }
        );

        btnGoals = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_CORRECT))));

        btnGoals.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showGoals();
            }
        });

        btnReset = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_RESET))));

        btnOptions = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_OPTIONS))));
        btnOptions.addListener(new ClickListener()
                               {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       options();
                                   }
                               }
        );

        btnLeaderboard = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_LEADERBOARD))));


        btnLeaderboard.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboard();
            }
        });

        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetLevel();
            }
        });


        btnBack.getImageCell().expand().fill();
        btnGoals.getImageCell().expand().fill();
        btnReset.getImageCell().expand().fill();
        btnOptions.getImageCell().expand().fill();
        btnLeaderboard.getImageCell().expand().fill();

        I18NBundle i18N = hexpert.i18NBundle;
        Label lblBack = new Label(i18N.get("backtolvlslct"), hexpert.getSkin());
        Label lblGoals = new Label(i18N.get("show_goals"), hexpert.getSkin());
        Label lblReset = new Label(i18N.get("resetlvl"), hexpert.getSkin());
        Label lblOptions = new Label(i18N.get("options"), hexpert.getSkin());
        Label lblLeaderboard = new Label(i18N.get("leaderboard"), hexpert.getSkin());

        lblBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToMenu();
            }
        });

        lblGoals.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showGoals();
            }
        });

        lblReset.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetLevel();
            }
        });

        lblOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options();
            }
        });

        lblLeaderboard.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboard();
            }
        });

        float extraPadding  = 50;
        getContentTable().add(btnBack).padTop(extraPadding);
        getContentTable().add(lblBack).width(lblBack.getPrefWidth()).padRight(extraPadding);
        getContentTable().row();
        getContentTable().add(btnGoals);
        getContentTable().add(lblGoals).width(lblGoals.getPrefWidth()).padRight(extraPadding);
        getContentTable().row();
        getContentTable().add(btnReset);
        getContentTable().add(lblReset).width(lblReset.getPrefWidth()).padRight(extraPadding);
        getContentTable().row();
        getContentTable().add(btnOptions);
        getContentTable().add(lblOptions).width(lblOptions.getPrefWidth()).padRight(extraPadding);
        getContentTable().row();
        PlayScreen playScreen = (PlayScreen)playStage;

        if(playScreen.map.scoreIsCalculated())
        {
            getContentTable().add(btnLeaderboard);
            getContentTable().add(lblLeaderboard).width(lblLeaderboard.getPrefWidth()).padRight(extraPadding);
        }

        TextButton textButtonClose = new TextButton(i18N.get("close"), hexpert.getSkin());
        getButtonTable().add(textButtonClose).width(325);

        setObject(textButtonClose, null);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }

    private void backToMenu()
    {
        hide();
        hexpert.setScreen(hexpert.levelSelectScreen);
    }

    private void showGoals() {
        ObjectiveDialog objectiveDiag = new ObjectiveDialog(hexpert.getSkin(), hexpert);
        com.cedricmartens.hexpert.screens.PlayScreen playScreen = (com.cedricmartens.hexpert.screens.PlayScreen)playStage;
        objectiveDiag.setObjectives(playScreen.map.getObjectives(), playScreen.mapResult.getObjectivePassed());
        objectiveDiag.show(getStage());
        hide();
    }

    private void resetLevel()
    {
        ((com.cedricmartens.hexpert.screens.PlayScreen)playStage).resetGrid();
        hide();
    }

    private void options()
    {
        new OptionDialog(playStage, hexpert.getSkin()).show(getStage());
        hide();
    }

    private void leaderboard()
    {
        try{
            hexpert.playServices.showLeaderboardUI(((com.cedricmartens.hexpert.screens.PlayScreen)playStage).mapName);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        hide();
    }
}
