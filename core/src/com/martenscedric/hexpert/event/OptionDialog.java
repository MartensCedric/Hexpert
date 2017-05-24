package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class OptionDialog extends Dialog
{
    private Hexpert hexpert;
    public OptionDialog(Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;

        getButtonTable().add(new TextButton(hexpert.i18NBundle.get("ok"), skin));
    }


}
