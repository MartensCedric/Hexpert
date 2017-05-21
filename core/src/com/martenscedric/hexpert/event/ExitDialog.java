package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by Shawn Martens on 2017-05-17.
 */

public class ExitDialog extends Dialog {

    private boolean shown = false;
    private Hexpert hexpert;
    public ExitDialog(Skin skin, Hexpert hexpert) {
        super("", skin);
        this.hexpert = hexpert;
        I18NBundle bundle = hexpert.i18NBundle;
        Label content = new Label(bundle.get("all_objectives_finished"), skin);
        content.setWidth(900);
        content.setAlignment(Align.center);
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
        content.setFontScale(5);
        content.setWrap(true);
        content.setY(375);
        content.setX(50);

        getContentTable().addActor(content);

        TextButton textButtonYes = new TextButton(bundle.get("yes"), skin);
        textButtonYes.getLabel().setFontScale(5);
        textButtonYes.setX(250);
        textButtonYes.setY(50);
        textButtonYes.setWidth(200);
        textButtonYes.setHeight(120);
        setObject(textButtonYes, 1);
        getButtonTable().addActor(textButtonYes);

        TextButton textButtonNo = new TextButton(bundle.get("no"), skin);
        textButtonNo.getLabel().setFontScale(5);
        textButtonNo.setX(600);
        textButtonNo.setY(50);
        textButtonNo.setWidth(200);
        textButtonNo.setHeight(120);
        setObject(textButtonNo, null);
        getButtonTable().addActor(textButtonNo);
    }

    @Override
    protected void result(Object object) {

        if(object != null && (int)object == 1)
        {
            hexpert.setScreen(hexpert.levelSelectScreen);
        }

        super.result(object);
    }


    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public boolean hasBeenShown() {
        return shown;
    }
}
