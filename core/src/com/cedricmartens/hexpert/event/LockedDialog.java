package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 5/27/17.
 */

public class LockedDialog extends StandardDialog {
    public LockedDialog(int currentGoals, int goalsNeeded, Hexpert hexpert, Skin skin) {
        super(hexpert, skin.get("gray", WindowStyle.class));
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(350);

        getContentTable().defaults().pad(25);

        TextButton.TextButtonStyle txtButtonStyle = skin.get("gray", TextButton.TextButtonStyle.class);

        Label label = new Label(hexpert.i18NBundle.format("locked_left", currentGoals, goalsNeeded), skin);
        getContentTable().add(label).top();

        final TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), txtButtonStyle);

        textButtonOK.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                textButtonOK.setChecked(false);
            }
        });
        setObject(textButtonOK, null);
        getButtonTable().add(textButtonOK);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }
}
