package com.martenscedric.hexpert.event;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
                    close();
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
                                       close();
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
                close();
            }
        });

        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((PlayScreen)playStage).resetGrid();
                close();
            }
        });


        btnBack.getImageCell().expand().fill();
        btnReset.getImageCell().expand().fill();
        btnOptions.getImageCell().expand().fill();
        btnLeaderboard.getImageCell().expand().fill();

        getContentTable().add(btnBack);
        getContentTable().row();
        getContentTable().add(btnReset);
        getContentTable().row();
        getContentTable().add(btnOptions);
        getContentTable().row();
        getContentTable().add(btnLeaderboard);
        getContentTable().row();
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }
}
