package com.cedricmartens.hexpert.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.cedricmartens.hexmap.coordinate.Point;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.tile.TileData;

/**
 * Created by martens on 5/19/17.
 */

public class MapUtils
{
    public static void adjustCamera(OrthographicCamera camera, HexMap<TileData> grid)
    {

        double maxHeight = -1, maxWidth = -1;
        double minHeight = Double.MAX_VALUE, minWidth = Double.MAX_VALUE;
        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> hex = grid.getHexs()[i];
            TileData data = hex.getHexData();
            hex.setHexData(data);


            for(Point p : hex.getHexGeometry().getPoints())
            {
                if(p.y < minHeight)
                    minHeight = p.y;

                if(p.y > maxHeight)
                    maxHeight = p.y;

                if(p.x < minWidth)
                    minWidth = p.x;

                if(p.x > maxWidth)
                    maxWidth = p.x;
            }
        }

        double deltaX = maxWidth - minWidth;
        double middleX = minWidth + deltaX/2;

        double deltaY = maxHeight - minHeight;
        double middleY = minHeight + deltaY/2;

        camera.translate((float) middleX, (float) (middleY - 50), 0);

        double biggestDelta = deltaY;

        camera.zoom = (float) (biggestDelta / 500);
        camera.update();
    }


    public static int getLevelIndex(String levelName)
    {
        String[] lvlIndexes = Gdx.files.internal("maps.hexindex").readString().split("\n");

        for(int i = 0; i < lvlIndexes.length; i++)
        {
            if(lvlIndexes[i].equals(levelName))
            {
                return i + 1;
            }
        }

        throw new IllegalStateException();
    }
}
