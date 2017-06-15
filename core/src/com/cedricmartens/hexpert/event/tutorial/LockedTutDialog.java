package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;

/**
 * Created by martens on 6/15/17.
 */

public class LockedTutDialog extends TutorialDialog {
    public LockedTutDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin, false);
        I18NBundle i18n = hexpert.i18NBundle;
        Label lblContent = new Label(i18n.get("lck_diag"), skin);
        lblContent.setAlignment(Align.center);
        lblContent.setWrap(true);
        getContentTable().defaults().pad(25);
        getContentTable().add(lblContent).width(800);
    }
}
