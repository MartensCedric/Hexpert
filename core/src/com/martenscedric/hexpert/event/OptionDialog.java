package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.misc.HexpertConfig;

/**
 * Created by 1544256 on 2017-05-24.
 */

public class OptionDialog extends Dialog
{
    private Hexpert hexpert;
    private HexpertConfig config;
    public OptionDialog(Hexpert hexpert, Skin skin) {
        super("", skin);
        this.hexpert = hexpert;
        this.config = hexpert.config;
        TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), skin);

        textButtonOK.setX(400);
        textButtonOK.setY(50);
        textButtonOK.setWidth(200);
        textButtonOK.setHeight(120);

        getButtonTable().add(textButtonOK);
        setObject(textButtonOK, null);
    }
}
