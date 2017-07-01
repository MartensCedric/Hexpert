package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 7/1/17.
 */

public class PurchaseDialog extends StandardDialog {

    private Label lblPrice;

    public PurchaseDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin);

        
        int defaultPrice = 2;
        I18NBundle i18N = hexpert.i18NBundle;
        lblPrice = new Label(String.format("$%d", defaultPrice), skin.get("bigger", Label.LabelStyle.class));
        getBackground().setMinWidth(1600);
        getContentTable().add(lblPrice);
        getContentTable().row();
        final Slider slider = new Slider(0, 10, 1, false, skin);
        slider.setValue(defaultPrice);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lblPrice.setText(String.format("$%d", (int)slider.getValue()));
            }
        });
        slider.setDebug(true);
        getContentTable().add(slider).width(1200).height(200);
        getContentTable().setDebug(true);
        getButtonTable().defaults().pad(15);

        TextButton textButtonOk = new TextButton(i18N.get("ok"), skin);
        getButtonTable().add(textButtonOk);

        setObject(textButtonOk, null);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }

    @Override
    public float getPrefHeight() {
        return 900;
    }
}
