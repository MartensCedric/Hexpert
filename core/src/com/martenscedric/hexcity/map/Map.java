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
 * Created by Cedric Martens on 2017-04-30.
 */

public class Map
{
    private HexFreeShapeBuilder<TileData> builder;
    private TileType[] tileTypes;
    private BuildingType[] buildingTypes;
    private boolean calculateScore;
    private Objective[] objectives;

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

    public Objective[] getObjectives() {
        return objectives;
    }

    public boolean isCalculateScore() {
        return calculateScore;
    }

    public boolean scoreIsCalculated() {
        return calculateScore;
    }

    public void setCalculateScore(boolean calculateScore) {
        this.calculateScore = calculateScore;
    }

    public void setBuildingTypes(BuildingType[] buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public void setObjectives(Objective[] objectives) {
        this.objectives = objectives;
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

        Hexagon<TileData>[] hexagons = grid.getHexs();

        List<Hexagon<TileData>> sortedHexs = new ArrayList<Hexagon<TileData>>();
        List<BuildingType> bTypes = new ArrayList<BuildingType>();
        List<TileType> tTypes = new ArrayList<TileType>();

        boolean nulls = false;
        while (!nulls)
        {
            nulls = true;
            int iBest = -1;
            Hexagon<TileData> best = null;
            for(int i = 0; i < hexagons.length; i++)
            {
                if(hexagons[i] != null)
                {
                    nulls = false;
                    if(best == null || best.getHexGeometry().getMiddlePoint().y <= hexagons[i].getHexGeometry().getMiddlePoint().y)
                    {
                        best = hexagons[i];
                        iBest = i;
                    }
                }
            }

            if(best != null)
            {
                sortedHexs.add(best);
                bTypes.add(best.getHexData().getBuildingType());
                tTypes.add(best.getHexData().getTileType());
                hexagons[iBest] = null;
            }
        }

        Hexagon<TileData>[] res = new Hexagon[sortedHexs.size()];

        for(int i = 0; i < res.length; i++)
        {
            res[i] = sortedHexs.get(i);
            buildingTypes[i] = bTypes.get(i);
            tileTypes[i] = tTypes.get(i);
        }

        grid.setHexs(res);
        return grid;
    }
}
