package com.cedricmartens.hexpert.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.cedricmartens.hexpert.misc.PointTime;

import java.util.ArrayList;
import java.util.List;

import static com.cedricmartens.hexpert.misc.TextureData.TEXTURE_BAD;

/**
 * Created by 1544256 on 2017-05-08.
 */

public class MoveEventManager
{
    private com.cedricmartens.hexpert.Hexpert hexpert;
    private Texture badMoveTexture;
    private List<PointTime> badMoves;
    private List<com.cedricmartens.hexpert.misc.IntPointTime> score;
    private SpriteBatch batch;
    private final int SCALE = 64;

    public MoveEventManager(com.cedricmartens.hexpert.screens.PlayScreen screen) {
        batch = screen.getBatch();
        this.hexpert = screen.getHexpert();
        this.badMoveTexture = hexpert.assetManager.get(TEXTURE_BAD, Texture.class);
        badMoves = new ArrayList<PointTime>();
        score = new ArrayList<com.cedricmartens.hexpert.misc.IntPointTime>();
    }

    public void render(float delta)
    {
        for(int i = 0; i < score.size(); i++)
        {
            PointTime pointTime = score.get(i);
            pointTime.setTime(pointTime.getTime() - delta);

            if(pointTime.getTime() > 0)
            {
                pointTime.y += 20 * delta;
            }else{
                score.remove(i);
                i--;
            }
        }

        for(int i = 0; i < badMoves.size(); i++)
        {
            PointTime pointTime = badMoves.get(i);
            pointTime.setTime(pointTime.getTime() - delta);

            if(pointTime.getTime() > 0)
            {
                pointTime.y += 20 * delta;
            }else{
                badMoves.remove(i);
                i--;
            }
        }


        batch.begin();
        for(int i = 0; i < badMoves.size(); i++)
        {
            PointTime pointTime = badMoves.get(i);
            batch.draw(badMoveTexture, (float)((pointTime.x - SCALE/2) + Math.sin(TimeUtils.millis()/15) * 5), (float)(pointTime.y - SCALE/2), SCALE, SCALE);
        }

        for(int i = 0; i < score.size(); i++)
        {
            com.cedricmartens.hexpert.misc.IntPointTime pointTime = score.get(i);

            BitmapFont f = hexpert.getFont();
            f.draw(batch, pointTime.getN() >=0 ? "+" + Integer.toString(pointTime.getN()) : Integer.toString(pointTime.getN()), (float)pointTime.x, (float)pointTime.y);
        }
        batch.end();
    }

    public List<PointTime> getBadMoves() {
        return badMoves;
    }

    public List<com.cedricmartens.hexpert.misc.IntPointTime> getScoreList() {
        return score;
    }
}
