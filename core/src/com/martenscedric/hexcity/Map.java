package com.martenscedric.hexcity;

import com.cedricmartens.hexpert.grid.HexGrid;
import com.cedricmartens.hexpert.grid.HexGridBuilder;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class Map
{
    private HexGrid<TileData> grid;

    public Map() {

    }

    public void setGrid(HexGridBuilder<TileData> gridBuilder) {
        this.grid = gridBuilder.build();
    }

    public HexGrid<TileData> getGrid() {
        return grid;
    }
}
