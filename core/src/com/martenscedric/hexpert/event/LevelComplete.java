package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.martenscedric.hexpert.Hexpert;

/**
 * Created by Shawn Martens on 2017-05-17.
 */

public class LevelComplete extends StandardDialog {

    private boolean shown = false;

    public LevelComplete(Skin skin, Hexpert hexpert) {
        super(hexpert, skin);

        I18NBundle bundle = hexpert.i18NBundle;
        Label content = new Label(bundle.get("all_objectives_finished"), skin);

        content.setAlignment(Align.center);
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
        content.setWrap(true);
        getContentTable().add(content).width(900);

        getButtonTable().defaults().width(200).height(120).pad(15);

        TextButton textButtonYes = new TextButton(bundle.get("yes"), skin);
        setObject(textButtonYes, 1);
        getButtonTable().add(textButtonYes);

        TextButton textButtonNo = new TextButton(bundle.get("no"), skin);
        setObject(textButtonNo, null);
        getButtonTable().add(textButtonNo);

        TextButton textButtonShare = new TextButton(bundle.get("share"), skin);
        setObject(textButtonShare, 2);

        Cell cellShare = getButtonTable().add(textButtonShare);
        cellShare.width(cellShare.getPrefWidth() + 30);
    }

    @Override
    protected void result(Object object) {

        if(object != null)
        {
            int res = (int)object;
            switch (res)
            {
                case 1 :
                    hexpert.setScreen(hexpert.levelSelectScreen);
                    break;
                case 2:
                    hexpert.sharing.shareText("This is a test message for sharing hexpert");
                    break;
            }
        }

        super.result(object);
    }


    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public boolean hasBeenShown() {
        return shown;
    }

    @Override
    public Dialog show(Stage stage, Action action) {
        hexpert.sounds.get("win").play();
        return super.show(stage, action);
    }
}
