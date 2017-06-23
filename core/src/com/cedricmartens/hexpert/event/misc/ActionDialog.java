package com.cedricmartens.hexpert.event.misc;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.StandardDialog;
import com.cedricmartens.hexpert.misc.Action;

/**
 * Created by martens on 5/22/17.
 */

public class ActionDialog extends StandardDialog {

    private Action action;

    public ActionDialog(Label text, Action action, I18NBundle bundle, Skin skin, Hexpert hexpert) {
        super(hexpert, skin);
        this.action = action;

        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(400);
        text.setWrap(true);
        text.setAlignment(Align.center);

        getContentTable().add(text).width(getBackground().getMinWidth()).expandX();

        getButtonTable().defaults().width(200).height(120).pad(15);

        TextButton textButtonYes = new TextButton(bundle.get("yes"), skin);
        getButtonTable().add(textButtonYes);
        setObject(textButtonYes, 1);
        TextButton textButtonNo = new TextButton(bundle.get("no"), skin);
        getButtonTable().add(textButtonNo);
        setObject(textButtonNo, null);
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
