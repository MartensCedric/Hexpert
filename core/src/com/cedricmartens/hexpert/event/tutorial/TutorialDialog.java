package com.cedricmartens.hexpert.event.tutorial;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.StandardDialog;

/**
 * Created by martens on 6/11/17.
 */

public abstract class TutorialDialog extends StandardDialog {
    protected Table scrollContent;

    public TutorialDialog(Hexpert hexpert, Skin skin) {
        super(hexpert, skin);
        scrollContent = new Table();
        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        getContentTable().add(scrollPane);

        getButtonTable().defaults().pad(15);
        I18NBundle i18N = hexpert.i18NBundle;

        TextButton textButtonOk = new TextButton(i18N.get("ok"), skin);
        getButtonTable().add(textButtonOk);

        setObject(textButtonOk, null);
    }
}
