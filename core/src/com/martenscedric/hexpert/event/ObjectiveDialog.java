package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.martenscedric.hexpert.Hexpert;
import com.martenscedric.hexpert.map.Objective;

/**
 * Created by martens on 5/20/17.
 */

public class ObjectiveDialog extends Dialog {

    private Hexpert hexpert;
    private Objective[] objectives;
    private boolean[] objectiveStatus;
    private Label content;

    public ObjectiveDialog(Skin skin, Hexpert hexpert) {
        super(hexpert.i18NBundle.get("goals"), skin);
        this.hexpert = hexpert;

        content = new Label("", skin);
        content.setWidth(900);
        content.setAlignment(Align.center);
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
        content.setFontScale(5);
        content.setWrap(true);
        content.setY(375);
        content.setX(50);

        getContentTable().addActor(content);

        TextButton textButtonNo = new TextButton(hexpert.i18NBundle.get("ok"), skin);
        textButtonNo.getLabel().setFontScale(5);
        textButtonNo.setX(500);
        textButtonNo.setY(50);
        textButtonNo.setWidth(200);
        textButtonNo.setHeight(120);
        setObject(textButtonNo, null);
        getButtonTable().addActor(textButtonNo);
    }

    public void setObjectives(Objective[] objectives, boolean[] objectiveStatus)
    {
        this.objectives = objectives;
        this.objectiveStatus = objectiveStatus;

        if(objectives.length != objectiveStatus.length)
            throw new IllegalArgumentException();

        String str = "";

        for(int i = 0; i < objectives.length; i++)
        {
            str+=objectiveStatus[i] ? "[DONE] " : "";
            str+=objectives[i].toString() + "\n";
        }

        content.setText(str);
    }

}
