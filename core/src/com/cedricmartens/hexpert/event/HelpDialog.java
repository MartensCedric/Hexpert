package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.event.tutorial.BuildingReqDialog;
import com.cedricmartens.hexpert.event.tutorial.BuildingScoreDialog;
import com.cedricmartens.hexpert.event.tutorial.LockedTutDialog;
import com.cedricmartens.hexpert.event.tutorial.TileEffectDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by martens on 6/11/17.
 */

public class HelpDialog extends StandardDialog {
    public HelpDialog(final Hexpert hexpert, Skin skin) {
        super(hexpert, skin);

        I18NBundle bundle = hexpert.i18NBundle;

        getContentTable().defaults().pad(15);
        TextButton txtBtnReqs = new TextButton(bundle.get("building_req"), skin);
        TextButton txtBtnScore = new TextButton(bundle.get("building_score"), skin);
        TextButton txtBtnSpec = new TextButton(bundle.get("tile_spec"), skin);
        TextButton txtBtnLck = new TextButton(bundle.get("building_lck"), skin);

        txtBtnReqs.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new BuildingReqDialog(hexpert, hexpert.getSkin()).show(getStage());
                hide();
            }
        });

        txtBtnScore.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new BuildingScoreDialog(hexpert, hexpert.getSkin()).show(getStage());
                hide();
            }
        });

        txtBtnSpec.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new TileEffectDialog(hexpert, hexpert.getSkin()).show(getStage());
                hide();
            }
        });

        txtBtnLck.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new LockedTutDialog(hexpert, hexpert.getSkin()).show(getStage());
                hide();
            }
        });

        getContentTable().defaults().height(120);

        int max = 0;

        List<TextButton> txtButtons = Arrays.asList(txtBtnLck, txtBtnReqs, txtBtnScore, txtBtnSpec);
        for(int i = 0; i < txtButtons.size(); i++)
        {
            TextButton txtButton = txtButtons.get(i);
            if(max < txtButton.getPrefWidth())
            {
                max = (int) txtButton.getPrefWidth();
            }
        }

        getContentTable().defaults().width(max + 20);
        getContentTable().add(txtBtnReqs);
        getContentTable().row();

        getContentTable().add(txtBtnScore);
        getContentTable().row();

        getContentTable().add(txtBtnSpec);
        getContentTable().row();

        getContentTable().add(txtBtnLck);
        getContentTable().row();

        TextButton txtBtnOk = new TextButton(bundle.get("ok"), skin);
        getButtonTable().add(txtBtnOk);
        setObject(txtBtnOk, null);
    }

    @Override
    protected void result(Object object) {
        super.result(object);
    }
}
