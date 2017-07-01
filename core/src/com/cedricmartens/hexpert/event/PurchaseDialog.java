package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.social.Purchasing;

/**
 * Created by martens on 7/1/17.
 */

public class PurchaseDialog extends StandardDialog {

    private Label lblPrice;

    public PurchaseDialog(final Hexpert hexpert, final Skin skin) {
        super(hexpert, skin.get("gold", WindowStyle.class));

        getBackground().setMinWidth(1600);

        I18NBundle i18N = hexpert.i18NBundle;
        Label.LabelStyle lblStyleBigger = skin.get("bigger", Label.LabelStyle.class);

        Label lblExplaination = new Label(i18N.get("pwyw"), lblStyleBigger);
        lblExplaination.setWrap(true);
        lblExplaination.setAlignment(Align.center);
        getContentTable().add(lblExplaination).width(1400);
        getContentTable().row();
        int defaultPrice = 2;
        lblPrice = new Label(String.format("$%d", defaultPrice), lblStyleBigger);

        getContentTable().add(lblPrice).padTop(150);
        getContentTable().row();
        final Slider slider = new Slider(0, 10, 1, false, skin);
        slider.setValue(defaultPrice);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lblPrice.setText(String.format("$%d", (int)slider.getValue()));
            }
        });

        getContentTable().add(slider).width(1200).height(100);

        TextButton textButtonOk = new TextButton(i18N.get("ok"), skin.get("gold", TextButton.TextButtonStyle.class));
        getButtonTable().add(textButtonOk);
        textButtonOk.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Purchasing.Amount amount = Purchasing.Amount.values()[(int)slider.getValue()];

                if(amount == Purchasing.Amount.ZERO)
                {
                   new FreeridingDialog(hexpert, skin).show(getStage());
                }

                hide();
            }
        });

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
