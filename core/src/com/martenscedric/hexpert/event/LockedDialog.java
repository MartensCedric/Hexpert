package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by martens on 5/27/17.
 */

public class LockedDialog extends StandardDialog {
    public LockedDialog(int currentGoals, int goalsNeeded, Hexpert hexpert, Skin skin) {
        super(hexpert, skin);
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(350);

        Label label = new Label(hexpert.i18NBundle.format("locked_left", currentGoals, goalsNeeded), skin);
        getContentTable().add(label).top();

        final TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), skin);

        textButtonOK.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                textButtonOK.setChecked(false);
            }
        });
        setObject(textButtonOK, null);
        getButtonTable().add(textButtonOK).width(200).height(120);


    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }
}
