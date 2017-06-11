package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by martens on 6/7/17.
 */

public abstract class StandardDialog extends Dialog
{
    protected com.cedricmartens.hexpert.Hexpert hexpert;
    public StandardDialog(com.cedricmartens.hexpert.Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        hexpert.sounds.get("click").play();
    }
}
