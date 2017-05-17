package com.martenscedric.hexpert.env;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Created by Shawn Martens on 2017-05-17.
 */

public class ExitDialog extends Dialog {

    private boolean shown = false;
    public ExitDialog(I18NBundle bundle, Skin skin) {
        super("", skin);
        text(bundle.get("all_objectives_finished"));
        button(bundle.get("yes"));
        button(bundle.get("no"));
    }

    @Override
    protected void result(Object object) {
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
