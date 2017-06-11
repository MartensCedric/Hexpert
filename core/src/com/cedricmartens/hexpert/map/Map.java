package com.cedricmartens.hexpert.map;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexmap.map.freeshape.HexFreeShapeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric Martens on 2017-04-30.
 */

public class Map
{
    private HexFreeShapeBuilder<com.cedricmartens.hexpert.tile.TileData> builder;
    private com.cedricmartens.hexpert.tile.TileType[] tileTypes;
    private com.cedricmartens.hexpert.tile.BuildingType[] buildingTypes;
    private boolean calculateScore;
    private Objective[] objectives;

    public void setBuilder(HexFreeShapeBuilder<com.cedricmartens.hexpert.tile.TileData> builder) {
        this.builder = builder;
    }

    public void setTileTypes(com.cedricmartens.hexpert.tile.TileType[] tileTypes) {
        this.tileTypes = tileTypes;
    }

    public void setBuildingType(com.cedricmartens.hexpert.tile.BuildingType[] buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public com.cedricmartens.hexpert.tile.BuildingType[] getBuildingTypes() {
        return buildingTypes;
    }

    public com.cedricmartens.hexpert.tile.TileType[] getTileTypes() {
        return tileTypes;
    }

    public HexFreeShapeBuilder<com.cedricmartens.hexpert.tile.TileData> getBuilder() {
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

    public void setBuildingTypes(com.cedricmartens.hexpert.tile.BuildingType[] buildingTypes) {
        this.buildingTypes = buildingTypes;
    }

    public void setObjectives(Objective[] objectives) {
        this.objectives = objectives;
    }

    public HexMap<com.cedricmartens.hexpert.tile.TileData> build()
    {
        HexMap<com.cedricmartens.hexpert.tile.TileData> grid = builder.build();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            Hexagon<com.cedricmartens.hexpert.tile.TileData> dataHexagon = grid.getHexs()[i];
            com.cedricmartens.hexpert.tile.TileData tileData = new com.cedricmartens.hexpert.tile.TileData(dataHexagon);
            tileData.setBuildingType(buildingTypes[i]);
            tileData.setTileType(tileTypes[i]);
            dataHexagon.setHexData(tileData);
        }

        Hexagon<com.cedricmartens.hexpert.tile.TileData>[] hexagons = grid.getHexs();

        List<Hexagon<com.cedricmartens.hexpert.tile.TileData>> sortedHexs = new ArrayList<Hexagon<com.cedricmartens.hexpert.tile.TileData>>();
        List<com.cedricmartens.hexpert.tile.BuildingType> bTypes = new ArrayList<com.cedricmartens.hexpert.tile.BuildingType>();
        List<com.cedricmartens.hexpert.tile.TileType> tTypes = new ArrayList<com.cedricmartens.hexpert.tile.TileType>();

        boolean nulls = false;
        while (!nulls)
        {
            nulls = true;
            int iBest = -1;
            Hexagon<com.cedricmartens.hexpert.tile.TileData> best = null;
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

        Hexagon<com.cedricmartens.hexpert.tile.TileData>[] res = new Hexagon[sortedHexs.size()];

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
