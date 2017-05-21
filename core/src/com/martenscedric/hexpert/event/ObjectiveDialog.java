package com.martenscedric.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    public ObjectiveDialog(Skin skin, Hexpert hexpert) {
        super("", skin);
        this.hexpert = hexpert;;
        getBackground().setMinWidth(1000);

        final TextButton textButtonOK = new TextButton(hexpert.i18NBundle.get("ok"), skin);
        textButtonOK.getLabel().setFontScale(5);
        textButtonOK.setX(400);
        textButtonOK.setY(50);
        textButtonOK.setWidth(200);
        textButtonOK.setHeight(120);

        textButtonOK.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                textButtonOK.setChecked(false);
            }
        });
        setObject(textButtonOK, null);
        getButtonTable().addActor(textButtonOK);

        getContentTable().defaults().pad(5, 0, 5, 15);
        getContentTable().top();
    }

    public void setObjectives(Objective[] objectives, boolean[] objectiveStatus)
    {
        this.objectives = objectives;
        this.objectiveStatus = objectiveStatus;

        if(objectives.length != objectiveStatus.length)
            throw new IllegalArgumentException();

        getContentTable().clearChildren();
        getBackground().setMinHeight(200 + 100 * objectives.length);
        getContentTable().setDebug(false);
        for(int i = 0; i < objectives.length; i++)
        {
            CheckBox checkBox = new CheckBox("", getSkin());
            checkBox.setChecked(objectiveStatus[i]);
            
            Label labelObjective = new Label(objectives[i].toString(), getSkin());

            labelObjective.setFontScale(5);

            getContentTable().add(checkBox);
            getContentTable().add(labelObjective);
            getContentTable().row();
        }
    }
}
