package com.martenscedric.hexpert.env;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
        text(bundle.get("all_objectives_finished"));
        button(bundle.get("yes"), 1);
        button(bundle.get("no"));
        debug();
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
    }

    @Override
    protected void result(Object object) {

        if(object != null && (int)object == 1)
        {
            hexpert.setScreen(hexpert.levelSelectScreen);
        }

        super.result(object);
    }

    @Override
    public Dialog show(Stage stage) {
        shown = true;
        return super.show(stage);
    }

    public boolean hasBeenShown() {
        return shown;
    }
}
