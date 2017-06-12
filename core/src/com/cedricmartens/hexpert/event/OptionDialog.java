package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.Gdx;
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
import com.cedricmartens.hexpert.misc.HexpertConfig;

import flexjson.JSONSerializer;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_CORRECT;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class OptionDialog extends StandardDialog
{
    private HexpertConfig config;
    public OptionDialog(final com.cedricmartens.hexpert.screens.PlayStage playStage, Skin skin) {
        super(playStage.getHexpert(), skin.get("gray", WindowStyle.class));
        this.config = hexpert.config;

        TextButton.TextButtonStyle txtButtonStyle = skin.get("gray", TextButton.TextButtonStyle.class);

        final TextureRegionDrawable txtRgCorrect = new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_CORRECT)));
        final TextureRegionDrawable txtRgBad = new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_BAD)));

        ImageButton.ImageButtonStyle imgStyle = new ImageButton.ImageButtonStyle();
        imgStyle.imageUp = txtRgBad;
        imgStyle.imageChecked = txtRgCorrect;

        I18NBundle i18n = hexpert.i18NBundle;

        getContentTable().pad(10);
        Label lblTileHelp = new Label(i18n.get("option_build_help"), hexpert.getSkin());
        final ImageButton chkTileHelp = new ImageButton(imgStyle);
        chkTileHelp.setChecked(config.isBuildHelp());

        chkTileHelp.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                config.setBuildHelp(!config.isBuildHelp());
                chkTileHelp.setChecked(config.isBuildHelp());
                checkAchievements();
                saveOptions();
            }
        });

        getContentTable().add(chkTileHelp).width(100).height(100);
        chkTileHelp.getImageCell().expand().fill();
        getContentTable().add(lblTileHelp);
        getContentTable().row();
        Label lblDeactivateSelection = new Label(i18n.get("option_keep_selection"), hexpert.getSkin());
        final ImageButton chkDeactivateSelection = new ImageButton(imgStyle);
        chkDeactivateSelection.setChecked(config.isKeepSelection());

        chkDeactivateSelection.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                config.setKeepSelection(!config.isKeepSelection());
                chkDeactivateSelection.setChecked(config.isKeepSelection());
                saveOptions();
            }
        });


        getContentTable().add(chkDeactivateSelection).width(100).height(100);
        chkDeactivateSelection.getImageCell().expand().fill();
        getContentTable().add(lblDeactivateSelection);
        getContentTable().row();

        Label lblShowReq = new Label(i18n.get("option_show_req"), hexpert.getSkin());
        final ImageButton chkShowReq = new ImageButton(imgStyle);
        chkShowReq.setChecked(config.isShowRequirements());

        chkShowReq.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                config.setShowRequirements(!config.isShowRequirements());
                chkShowReq.setChecked(config.isShowRequirements());

                if(!hexpert.config.isShowRequirements())
                {
                    playStage.setBuilding(null);
                }

                checkAchievements();
                saveOptions();
            }
        });

        getContentTable().add(chkShowReq).width(100).height(100);
        chkShowReq.getImageCell().expand().fill();
        getContentTable().add(lblShowReq);

        TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), txtButtonStyle);

        setObject(textButtonOK, null);
        getButtonTable().defaults().pad(25);
        getButtonTable().add(textButtonOK).width(200).height(120);
    }

    private void saveOptions()
    {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Gdx.files.local("options.config").writeString(jsonSerializer.serialize(config), false);
    }

    private void checkAchievements()
    {
        if(!config.isShowRequirements() && !config.isBuildHelp())
            hexpert.playServices.unlockAchievement(com.cedricmartens.hexpert.social.Achievement.HARDCORE_PLAYER);
    }
}
