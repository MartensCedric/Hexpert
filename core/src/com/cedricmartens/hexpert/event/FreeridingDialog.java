package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 7/1/17.
 */

public class FreeridingDialog extends StandardDialog {
    public FreeridingDialog(final Hexpert hexpert, Skin skin)
    {
        super(hexpert, skin.get("gold", WindowStyle.class));

        getBackground().setMinWidth(1400);
        I18NBundle i18N = hexpert.i18NBundle;
        Label lblRate = new Label(i18N.get("freerider"), skin.get("bigger", Label.LabelStyle.class));
        lblRate.setAlignment(Align.center);
        lblRate.setWrap(true);
        getContentTable().add(lblRate).width(1200);

        TextButton.TextButtonStyle goldenStyle = skin.get("gold", TextButton.TextButtonStyle.class);


        TextButton textButtonRate = new TextButton(i18N.get("rate"), goldenStyle);
        getButtonTable().add(textButtonRate);


        TextButton textButtonNo = new TextButton(i18N.get("no"), goldenStyle);
        getButtonTable().add(textButtonNo);
        setObject(textButtonNo, null);

        textButtonRate.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hexpert.playServices.rateGame();
                hide();
            }
        });

        setObject(textButtonRate, null);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }

    @Override
    public float getPrefHeight() {
        return 600;
    }
}
