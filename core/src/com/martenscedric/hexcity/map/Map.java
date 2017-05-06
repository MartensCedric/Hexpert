package com.martenscedric.hexcity.map;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexBuilder;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.cedricmartens.hexmap.map.grid.HexGridBuilder;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

/**
 * Created by Shawn Martens on 2017-04-30.
 */

public class Map
{
    private HexFreeShapeBuilder<TileData> builder;
    private TileType[] tileTypes;
    private BuildingType[] buildingTypes;

    public void setBuilder(HexFreeShapeBuilder<TileData> builder) {
        this.builder = builder;
    }

    public void setTileTypes(TileType[] tileTypes) {
        this.tileTypes = tileTypes;
    }

    public void setBuildingType(BuildingType[] buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public BuildingType[] getBuildingTypes() {
        return buildingTypes;
    }

    public TileType[] getTileTypes() {
        return tileTypes;
    }

    public HexFreeShapeBuilder<TileData> getBuilder() {
        return builder;
    }

    public HexMap<TileData> build()
    {
        HexMap<TileData> grid = builder.build();

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
