package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 6/7/17.
 */

public abstract class StandardDialog extends Dialog
{
    protected Hexpert hexpert;
    public StandardDialog(Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;
    }

    public StandardDialog(Hexpert hexpert, WindowStyle windowStyle) {
        super("", windowStyle);
        this.hexpert = hexpert;
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        hexpert.sounds.get("click").play();
    }
}
