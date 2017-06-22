package com.cedricmartens.hexpert.event;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.cedricmartens.hexpert.Hexpert;
import com.cedricmartens.hexpert.map.MapUtils;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_ACHIEVEMENTS;

/**
 * Created by Shawn Martens on 2017-05-17.
 */

public class LevelCompleteDialog extends StandardDialog {

    private boolean shown = false;
    private int score;
    private int lvlIndex;

    public LevelCompleteDialog(int score, String mapName, Skin skin, Hexpert hexpert) {
        super(hexpert, skin.get("gold", WindowStyle.class));

        this.score = score;
        lvlIndex = MapUtils.getLevelIndex(mapName);

        Image imageTrophy = new Image(new TextureRegion((Texture) hexpert.assetManager.get(TEXTURE_ACHIEVEMENTS)));
        TextButton.TextButtonStyle txtButtonStyle = skin.get("gold", TextButton.TextButtonStyle.class);
        I18NBundle bundle = hexpert.i18NBundle;
        Label content = new Label(bundle.get("all_objectives_finished"), skin);

        content.setAlignment(Align.center);
        getBackground().setMinWidth(1000);
        getBackground().setMinHeight(600);
        content.setWrap(true);

        getContentTable().defaults().pad(25);

        getContentTable().add(imageTrophy).width(192).height(192);
        getContentTable().row();
        getContentTable().add(content).width(900);

        getButtonTable().defaults().pad(15);

        TextButton textButtonYes = new TextButton(bundle.get("yes"), txtButtonStyle);
        setObject(textButtonYes, 1);
        getButtonTable().add(textButtonYes);

        TextButton textButtonNo = new TextButton(bundle.get("no"), txtButtonStyle);
        setObject(textButtonNo, null);
        getButtonTable().add(textButtonNo);

        if(lvlIndex > 3) {
            TextButton textButtonShare = new TextButton(bundle.get("share"), txtButtonStyle);
            setObject(textButtonShare, 2);
            getButtonTable().add(textButtonShare).width(textButtonShare.getPrefWidth() + 30);
        }
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
                    hexpert.sharing.shareText(hexpert.i18NBundle.format("share_message_score", score, lvlIndex));
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
        hexpert.sounds.get("win").play(hexpert.masterVolume);
        return super.show(stage, action);
    }
}
