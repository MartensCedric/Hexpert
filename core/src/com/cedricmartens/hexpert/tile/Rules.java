package com.cedricmartens.hexpert.tile;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.misc.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martens on 5/7/17.
 */

public class Rules
{
    public static Dependency getDependencyLevel(TileData data, HexMap<TileData> grid) {

        if (data.getBuildingType() == BuildingType.NONE)
            return Dependency.DEPENDENT;

        List<TileData> validBuildings = getValidBuildings(grid);

        Dependency defaultDep = Dependency.INDEPENDENT;
        int[] neighborStats = getNeighborStats(data);
        for (int i = 1; i < BuildingType.values().length; i++) {
            BuildingType buildingType = BuildingType.values()[i];
            if (buildingType.getRequired().length == Const.BUILDING_COUNT) {
                int requiredAmount = buildingType.getRequired()[data.getBuildingType().ordinal() - 1];
                if (requiredAmount > 0) {
                    if (neighborStats[i - 1] > 0)
                    {
                        for(int j = 0; j < data.getParent().getNeighbors().size(); j++) {
                            TileData neighbor = data.getParent().getNeighbors().get(j).getHexData();
                            BuildingType neighborBuildingType = neighbor.getBuildingType();

                            if (buildingType == neighborBuildingType) {
                                Logistic logistic = getLogisticalLevel(neighbor, data.getBuildingType(), validBuildings);

                                if (logistic == Logistic.INSUFFICIENT)
                                    return Dependency.INDEPENDENT;
                                else if (logistic == Logistic.SURPLUS)
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

    public static Logistic getLogisticalLevel(TileData data, BuildingType buildingType, List<TileData> validBuildings)
    {
        if(!validBuildings.contains(data))
            return Logistic.INSUFFICIENT;

        int[] neighborData = getNeighborStats(data);
        int requiredAmount = data.getBuildingType().getRequired()[buildingType.ordinal() - 1];
        int neighborAmount = neighborData[buildingType.ordinal() - 1];

        if(neighborAmount < requiredAmount)
            return Logistic.INSUFFICIENT;
        else if(neighborAmount > requiredAmount)
            return Logistic.SURPLUS;
        else return Logistic.NECESSARY;
    }

    public static List<TileData> getValidBuildings(HexMap<TileData> grid)
    {
        List<TileData> vBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();

            if(isValid(data, data.getBuildingType()))
                vBuildings.add(data);
        }

        return vBuildings;
    }

    public static boolean isValid(TileData data, BuildingType selection)
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

    public static boolean isValidPlacement(TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

        return isValid(data, selection);
    }

    private static int neighborCountOf(BuildingType buildingType, TileData data)
    {
        if(buildingType == BuildingType.NONE)
            throw new IllegalArgumentException();

        return getNeighborStats(data)[buildingType.ordinal() - 1];
    }

    private static int[] getNeighborStats(TileData data)
    {
        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();
        int[] neighborStats = new int[com.cedricmartens.hexpert.misc.Const.BUILDING_COUNT];

        for(int i = 0; i < neighbors.size(); i++) {
            TileData tileData = neighbors.get(i).getHexData();

            if(tileData.getBuildingType() != BuildingType.NONE)
            {
                neighborStats[tileData.getBuildingType().ordinal() - 1]++;
            }
        }

        return neighborStats;
    }
}
