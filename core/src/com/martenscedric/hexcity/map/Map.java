package com.martenscedric.hexcity.map;

import com.cedricmartens.hexpert.Hexagon;
import com.cedricmartens.hexpert.grid.HexGrid;
import com.cedricmartens.hexpert.grid.HexGridBuilder;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class Map
{
    private HexGridBuilder<TileData> gridBuilder;
    private TileType[] tileTypes;
    private BuildingType[] buildingTypes;

    public Map() {

    }

    public void setGridBuilder(HexGridBuilder<TileData> gridBuilder) {
        this.gridBuilder = gridBuilder;
    }

    public void setTileTypes(TileType[] tileTypes) {
        this.tileTypes = tileTypes;
    }

    public void setBuildingType(BuildingType[] buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public HexGrid<TileData> getGrid()
    {
        HexGrid<TileData> grid = gridBuilder.build();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<TileData> dataHexagon = grid.getHexs()[i];
            TileData tileData = new TileData(dataHexagon);
            tileData.setBuildingType(buildingTypes[i]);
            tileData.setTileType(tileTypes[i]);
            dataHexagon.setHexData(tileData);
        }

        return grid;
    }
}
