package com.cedricmartens.hexpert.tile;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martens on 5/7/17.
 */

public class Rules
{
    public static Dependency getDependencyLevel(com.cedricmartens.hexpert.tile.TileData data) {

        if (data.getBuildingType() == BuildingType.NONE)
            return Dependency.DEPENDENT;

        Dependency defaultDep = Dependency.INDEPENDENT;
        int[] neighborStats = getNeighborStats(data);
        for (int i = 1; i < BuildingType.values().length; i++) {
            BuildingType buildingType = BuildingType.values()[i];
            if (buildingType.getRequired().length == com.cedricmartens.hexpert.misc.Const.BUILDING_COUNT) {
                int requiredAmount = buildingType.getRequired()[data.getBuildingType().ordinal() - 1];
                if (requiredAmount > 0) {
                    if (neighborStats[i - 1] > 0)
                    {
                        for(int j = 0; j < data.getParent().getNeighbors().size(); j++) {
                            com.cedricmartens.hexpert.tile.TileData neighbor = data.getParent().getNeighbors().get(j).getHexData();
                            BuildingType neighborBuildingType = neighbor.getBuildingType();

                            if (buildingType == neighborBuildingType) {
                                com.cedricmartens.hexpert.tile.Logistic logistic = getLogisticalLevel(neighbor, data.getBuildingType());

                                if (logistic == com.cedricmartens.hexpert.tile.Logistic.INSUFFICIENT)
                                    throw new IllegalStateException();
                                else if (logistic == com.cedricmartens.hexpert.tile.Logistic.SURPLUS)
                                    defaultDep = Dependency.PARTIALLY;
                                else return Dependency.DEPENDENT;
                            }
                        }
                    }
                }
            }
        }

        return defaultDep;
    }

    public static com.cedricmartens.hexpert.tile.Logistic getLogisticalLevel(com.cedricmartens.hexpert.tile.TileData data, BuildingType buildingType)
    {
        int[] neighborData = getNeighborStats(data);
        int requiredAmount = data.getBuildingType().getRequired()[buildingType.ordinal() - 1];
        int neighborAmount = neighborData[buildingType.ordinal() - 1];

        if(neighborAmount < requiredAmount)
            return com.cedricmartens.hexpert.tile.Logistic.INSUFFICIENT;
        else if(neighborAmount > requiredAmount)
            return com.cedricmartens.hexpert.tile.Logistic.SURPLUS;
        else return com.cedricmartens.hexpert.tile.Logistic.NECESSARY;
    }

    public static List<com.cedricmartens.hexpert.tile.TileData> getValidBuildings(HexMap<com.cedricmartens.hexpert.tile.TileData> grid)
    {
        List<com.cedricmartens.hexpert.tile.TileData> vBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            com.cedricmartens.hexpert.tile.TileData data = (com.cedricmartens.hexpert.tile.TileData) grid.getHexs()[i].getHexData();

            if(isValid(data, data.getBuildingType()))
                vBuildings.add(data);
        }

        return vBuildings;
    }

    public static boolean isValid(com.cedricmartens.hexpert.tile.TileData data, BuildingType selection)
    {
        int[] neighborStats = getNeighborStats(data);

        for(int i = 0; i < selection.getDenied().length; i++)
        {
            if(selection.getDenied()[i] != 0
                    && selection.getDenied()[i] <= neighborStats[i])
            {
                return false;
            }
        }

        for(int i = 0; i < selection.getRequired().length; i++)
        {
            if(neighborStats[i] < selection.getRequired()[i])
                return false;
        }

        return true;
    }

    public static boolean isValidPlacement(com.cedricmartens.hexpert.tile.TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

        return isValid(data, selection);
    }

    private static int neighborCountOf(BuildingType buildingType, com.cedricmartens.hexpert.tile.TileData data)
    {
        if(buildingType == BuildingType.NONE)
            throw new IllegalArgumentException();

        return getNeighborStats(data)[buildingType.ordinal() - 1];
    }

    private static int[] getNeighborStats(com.cedricmartens.hexpert.tile.TileData data)
    {
        List<Hexagon<com.cedricmartens.hexpert.tile.TileData>> neighbors = data.getParent().getNeighbors();
        int[] neighborStats = new int[com.cedricmartens.hexpert.misc.Const.BUILDING_COUNT];

        for(int i = 0; i < neighbors.size(); i++) {
            com.cedricmartens.hexpert.tile.TileData tileData = neighbors.get(i).getHexData();

            if(tileData.getBuildingType() != BuildingType.NONE)
            {
                neighborStats[tileData.getBuildingType().ordinal() - 1]++;
            }
        }

        return neighborStats;
    }
}
