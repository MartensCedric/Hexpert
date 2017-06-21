package com.cedricmartens.hexpert.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.tile.TileData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

/**
 * Created by martens on 6/21/17.
 */

public class GridEffect
{
    private HexMap<TileData> grid;
    private List<Hexagon<TileData>> activeTiles = new ArrayList<>();
    private Stack<Hexagon<TileData>> upcomingTiles;
    private float frequency;
    private HashMap<Hexagon<TileData>, Double> newCoords = new HashMap<>();
    private float length;
    private float time = 0;

    private final int DISTANCE = 1000;

    public GridEffect(HexMap<TileData> grid, float frequency, float length) {
        this.grid = grid;
        this.frequency = frequency;
        this.length = length;

        List<Hexagon<TileData>> hexs = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> data = grid.getHexs()[i];
            hexs.add(data);
            newCoords.put(data, data.getHexGeometry().getMiddlePoint().y + DISTANCE);
        }

        upcomingTiles = new Stack<>();

        Random random = new Random();
        int hSize = hexs.size();
        while (upcomingTiles.size() != hSize)
        {
            int n = random.nextInt(hexs.size());
            upcomingTiles.push(hexs.get(n));
            hexs.remove(n);
        }
    }

    public void tick(float delta)
    {
        time += delta;

        if(time >= frequency)
        {
            time -= frequency;

            if(!upcomingTiles.empty())
            {
                Hexagon<TileData> data = upcomingTiles.pop();
                getActiveTiles().add(data);
            }
        }

        float movement = DISTANCE/length * delta;
        for(int i = 0; i < activeTiles.size(); i++)
        {
            Hexagon<TileData> tile = activeTiles.get(i);
            double y = newCoords.get(tile);
            newCoords.remove(tile);
            y-= movement;

            if(y <= tile.getHexGeometry().getMiddlePoint().y)
            {
                activeTiles.remove(i);
                i--;
            }else{
                newCoords.put(tile, y);
            }
        }
    }

    public boolean isActive()
    {
        return !activeTiles.isEmpty();
    }

    public boolean hasFallen(Hexagon<TileData> data)
    {
        return !newCoords.containsKey(data);
    }

    public HashMap<Hexagon<TileData>, Double> getNewCoords() {
        return newCoords;
    }

    public List<Hexagon<TileData>> getActiveTiles() {
        return activeTiles;
    }
}