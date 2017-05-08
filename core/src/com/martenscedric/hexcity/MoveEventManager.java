package com.martenscedric.hexcity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.cedricmartens.hexmap.coordinate.Point;
import com.martenscedric.hexcity.screens.PlayScreen;

import java.util.ArrayList;
import java.util.List;

import static com.martenscedric.hexcity.misc.TextureData.TEXTURE_BADMOVE;

/**
 * Created by 1544256 on 2017-05-08.
 */

public class MoveEventManager
{
    private HexCity hexCity;
    private Texture badMoveTexture;
    private List<PointTime> badMoves;
    private SpriteBatch batch;
    private final int SCALE = 64;

    public MoveEventManager(PlayScreen screen) {
        batch = screen.getBatch();
        this.hexCity = screen.getHexCity();
        this.badMoveTexture = hexCity.assetManager.get(TEXTURE_BADMOVE, Texture.class);
        badMoves = new ArrayList<PointTime>();
    }

    public void render(float delta)
    {
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
        batch.end();
    }

    public List<PointTime> getBadMoves() {
        return badMoves;
    }
}
