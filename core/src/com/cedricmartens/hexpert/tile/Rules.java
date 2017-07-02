package com.cedricmartens.hexpert.tile;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.cedricmartens.hexmap.map.HexMap;
import com.cedricmartens.hexpert.misc.Const;

import java.util.ArrayList;
import java.util.List;

import static com.cedricmartens.hexpert.misc.Const.BUILDING_COUNT;

/**
 * Created by martens on 5/7/17.
 */

public class Rules
{
    public static Dependency getDependencyLevel(TileData data, HexMap<TileData> grid,
                                                List<TileData> validBuildings, List<TileData> lockedBuildings) {

        if (data.getBuildingType() == BuildingType.NONE)
            return Dependency.DEPENDENT;

        Dependency defaultDep = Dependency.INDEPENDENT;
        int[] neighborStats = getNeighborStats(data);
        for (int i = 1; i < BuildingType.values().length; i++) {
            BuildingType buildingType = BuildingType.values()[i];
            if (buildingType.getRequired().length == BUILDING_COUNT) {
                int requiredAmount = buildingType.getRequired()[data.getBuildingType().ordinal() - 1];
                if (requiredAmount > 0) {
                    if (neighborStats[i - 1] > 0)
                    {
                        for(int j = 0; j < data.getParent().getNeighbors().size(); j++) {
                            TileData neighbor = data.getParent().getNeighbors().get(j).getHexData();
                            BuildingType neighborBuildingType = neighbor.getBuildingType();

                            if (buildingType == neighborBuildingType) {
                                Logistic logistic = getLogisticalLevel(neighbor, data.getBuildingType(),
                                        validBuildings, lockedBuildings);

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

    public static Logistic getLogisticalLevel(TileData data, BuildingType buildingType,
                                              List<TileData> validBuildings, List<TileData> lockedBuildings)
    {
        if(!validBuildings.contains(data))
            return Logistic.INSUFFICIENT;

        int[] neighborData = getNeighborStats(data, lockedBuildings);
        int requiredAmount = data.getBuildingType().getRequired()[buildingType.ordinal() - 1];
        int neighborAmount = neighborData[buildingType.ordinal() - 1];

        if(neighborAmount < requiredAmount)
            return Logistic.INSUFFICIENT;
        else if(neighborAmount > requiredAmount)
            return Logistic.SURPLUS;
        else return Logistic.NECESSARY;
    }

    public static List<TileData> getValidBuildings(HexMap<TileData> grid, List<TileData> lockedBuildings)
    {
        List<TileData> vBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();

            if(isValid(data, data.getBuildingType(),lockedBuildings))
                vBuildings.add(data);
        }

        return vBuildings;
    }

    public static List<TileData> getValidBuildings(HexMap<TileData> grid)
    {
        return getValidBuildings(grid, new ArrayList<TileData>());
    }

    public static boolean isValid(TileData data, BuildingType selection)
    {
        return isValid(data, selection, new ArrayList<TileData>());
    }

    public static boolean isValid(TileData data, BuildingType selection, List<TileData> lockedBuildings)
    {

        int[] neighborStats = getNeighborStats(data, lockedBuildings);

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

    public static boolean isValidPlacement(TileData data, BuildingType selection, List<TileData> lockedBuildings)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

        return isValid(data, selection, lockedBuildings);
    }

    private static int neighborCountOf(BuildingType buildingType, TileData data)
    {
        if(buildingType == BuildingType.NONE)
            throw new IllegalArgumentException();

        return getNeighborStats(data)[buildingType.ordinal() - 1];
    }

    private static int[] getNeighborStats(TileData data)
    {
        return getNeighborStats(data, new ArrayList<TileData>());
    }

    private static int[] getNeighborStats(TileData data, List<TileData> lockedBuildings)
    {
        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();
        int[] neighborStats = new int[BUILDING_COUNT];

        for(int i = 0; i < neighbors.size(); i++) {
            TileData tileData = neighbors.get(i).getHexData();

            if(tileData.getBuildingType() != BuildingType.NONE)
            {
                if(lockedBuildings.contains(tileData)){

                    if(isValid(tileData, tileData.getBuildingType()))
                    {
                        neighborStats[tileData.getBuildingType().ordinal() - 1]++;
                    }
                }else{
                    neighborStats[tileData.getBuildingType().ordinal() - 1]++;
                }
            }
        }

        return neighborStats;
    }
}
