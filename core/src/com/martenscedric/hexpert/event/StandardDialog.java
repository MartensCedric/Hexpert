package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by martens on 6/7/17.
 */

public class StandardDialog extends Dialog
{
    protected Hexpert hexpert;
    public StandardDialog(Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        hexpert.sounds.get("click").play();
    }
}
