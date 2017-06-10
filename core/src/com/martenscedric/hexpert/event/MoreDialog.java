package com.martenscedric.hexpert.event;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.screens.PlayScreen;
import com.martenscedric.hexpert.screens.PlayStage;

import static com.martenscedric.hexpert.misc.Const.HEIGHT;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BACK;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_LEADERBOARD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_OPTIONS;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_RESET;

/**
 * Created by martens on 6/10/17.
 */

public class MoreDialog extends StandardDialog {
    private ImageButton btnBack, btnReset, btnOptions, btnLeaderboard;
    private Hexpert hexpert;
    private PlayStage playStage;

    public MoreDialog(final Hexpert hexpert, final PlayStage playStage, Skin skin) {
        super(hexpert, skin);

        this.hexpert = hexpert;
        getContentTable().defaults().width(125).height(125).pad(15);
        btnBack = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_BACK))));
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
                    }, hexpert.i18NBundle, hexpert.getSkin(), hexpert);

                    actionDialog.show(getStage());
                }
            }
        );



        btnReset = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_RESET))));

        btnOptions = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_OPTIONS))));
        btnOptions.addListener(new ClickListener()
                               {
                                   @Override
                                   public void clicked(InputEvent event, float x, float y) {
                                       new OptionDialog(playStage, hexpert.getSkin()).show(getStage());
                                   }
                               }
        );

        btnLeaderboard = new ImageButton(new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_LEADERBOARD))));


        btnLeaderboard.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try{
                    hexpert.playServices.showLeaderboardUI(((PlayScreen)playStage).mapName);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((PlayScreen)playStage).resetGrid();
            }
        });


        btnBack.getImageCell().expand().fill();
        btnReset.getImageCell().expand().fill();
        btnOptions.getImageCell().expand().fill();
        btnLeaderboard.getImageCell().expand().fill();

        I18NBundle i18N = hexpert.i18NBundle;
        Label lblBack = new Label(i18N.get("backtolvlslct"), hexpert.getSkin());
        Label lblReset = new Label(i18N.get("resetlvl"), hexpert.getSkin());
        Label lblOptions = new Label(i18N.get("options"), hexpert.getSkin());
        Label lblLeaderboard = new Label(i18N.get("leaderboard"), hexpert.getSkin());

        getContentTable().add(btnBack);
        getContentTable().add(lblBack).width(lblBack.getPrefWidth());;
        getContentTable().row();
        getContentTable().add(btnReset);
        getContentTable().add(lblReset).width(lblReset.getPrefWidth());;
        getContentTable().row();
        getContentTable().add(btnOptions);
        getContentTable().add(lblOptions).width(lblOptions.getPrefWidth());;
        getContentTable().row();
        getContentTable().add(btnLeaderboard);
        getContentTable().add(lblLeaderboard).width(lblLeaderboard.getPrefWidth());;
        
        setObject(btnBack, null);
        setObject(btnReset, null);
        setObject(btnOptions, null);
        setObject(btnLeaderboard, null);
    }

    @Override
    protected void result(Object object) {
        if(object == null)
            return;
        super.result(object);
    }
}
