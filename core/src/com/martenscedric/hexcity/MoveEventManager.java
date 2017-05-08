package com.martenscedric.hexcity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    public MoveEventManager(HexCity hexCity) {
        batch = new SpriteBatch();
        this.hexCity = hexCity;
        this.badMoveTexture = hexCity.assetManager.get(TEXTURE_BADMOVE, Texture.class);
        badMoves = new ArrayList<PointTime>();
    }

    private void render(float delta)
    {
        for(int i = 0; i < badMoves.size(); i++)
        {
            PointTime pointTime = badMoves.get(i);
            batch.draw(badMoveTexture, (float)(pointTime.getPoint().x - badMoveTexture.getWidth()/2), (float)(pointTime.getPoint().y - badMoveTexture.getHeight()/2));
        }
    }
}
