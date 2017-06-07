package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by martens on 5/22/17.
 */

public class ActionDialog extends StandardDialog {

    private Action action;

    public ActionDialog(Label text, Action action, I18NBundle bundle, Skin skin, Hexpert hexpert) {
        super(hexpert, skin);
        this.action = action;

        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
        text.setWrap(true);
        text.setWidth(900);
        text.setAlignment(Align.center);
        text.setX(50);
        text.setY(375);

        getContentTable().addActor(text);

        TextButton textButtonYes = new TextButton(bundle.get("yes"), skin);
        textButtonYes.setX(250);
        textButtonYes.setY(50);
        textButtonYes.setWidth(200);
        textButtonYes.setHeight(120);
        setObject(textButtonYes, 1);
        getButtonTable().addActor(textButtonYes);

        TextButton textButtonNo = new TextButton(bundle.get("no"), skin);
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
            action.doAction();
        }

        super.result(object);
    }
}
