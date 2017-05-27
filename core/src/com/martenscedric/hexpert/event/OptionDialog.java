package com.martenscedric.hexpert.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.google.Achievement;
import com.martenscedric.hexpert.misc.HexpertConfig;

import flexjson.JSONSerializer;

import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_BAD;
import static com.martenscedric.hexpert.misc.TextureData.TEXTURE_CORRECT;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class OptionDialog extends Dialog
{
    private Hexpert hexpert;
    private HexpertConfig config;
    public OptionDialog(final Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;
        this.config = hexpert.config;

        final TextureRegionDrawable txtRgCorrect = new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_CORRECT)));
        final TextureRegionDrawable txtRgBad = new TextureRegionDrawable(new TextureRegion((Texture)hexpert.assetManager.get(TEXTURE_BAD)));

        ImageButton.ImageButtonStyle imgStyle = new ImageButton.ImageButtonStyle();
        imgStyle.imageUp = txtRgBad;
        imgStyle.imageChecked = txtRgCorrect;

        I18NBundle i18n = hexpert.i18NBundle;

        Label lblTileHelp = new Label(i18n.get("option_build_help"), hexpert.getSkin());
        final ImageButton chkTileHelp = new ImageButton(imgStyle);
        chkTileHelp.setChecked(config.isBuildHelp());

        chkTileHelp.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                config.setBuildHelp(!config.isBuildHelp());
                chkTileHelp.setChecked(config.isBuildHelp());

                if(!config.isBuildHelp())
                    hexpert.playServices.unlockAchievement(Achievement.HARDCORE_PLAYER);
                saveOptions();
            }
        });

        getContentTable().add(chkTileHelp).width(100).height(100);
        chkTileHelp.getImageCell().expand().fill();
        getContentTable().add(lblTileHelp);
        getContentTable().row();
        getContentTable().pad(10);
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

        TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), skin);

        setObject(textButtonOK, null);
        getButtonTable().add(textButtonOK).width(200).height(120);
    }

    private void saveOptions()
    {
        JSONSerializer jsonSerializer = new JSONSerializer();
        Gdx.files.local("options.config").writeString(jsonSerializer.serialize(config), false);
    }
}
