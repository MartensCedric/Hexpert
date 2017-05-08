package com.martenscedric.hexcity.map;

import com.cedricmartens.hexmap.hexagon.HexGeometry;
import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexBuilder;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;
import com.cedricmartens.hexmap.map.grid.HexGridBuilder;
import com.martenscedric.hexcity.tile.BuildingType;
import com.martenscedric.hexcity.tile.TileData;
import com.martenscedric.hexcity.tile.TileType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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

        List<Hexagon> hexagons = Arrays.asList(grid.getHexs());

        List<Hexagon<TileData>> sortedHexs = new ArrayList<Hexagon<TileData>>();

        while(sortedHexs.size() != grid.getHexs().length)
        {
            double currentBestY = Double.MIN_VALUE;
            int ibest = -1;
            for(int i = 0; i < hexagons.size(); i++)
            {
                double y = hexagons.get(i).getHexGeometry().getMiddlePoint().y;
                if(currentBestY < y)
                {
                    currentBestY = y;
                    ibest = i;
                }
            }

            sortedHexs.add(hexagons.get(ibest));
            hexagons.remove(hexagons.get(ibest));
        }
        grid.setHexs((Hexagon[]) sortedHexs.toArray());
        return grid;
    }
}
