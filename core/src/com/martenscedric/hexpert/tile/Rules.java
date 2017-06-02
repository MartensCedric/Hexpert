package com.martenscedric.hexpert.tile;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.martenscedric.hexpert.misc.Const;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;

import java.util.HashMap;
import java.util.List;

import static com.martenscedric.hexpert.tile.BuildingType.ROCKET;

/**
 * Created by martens on 5/7/17.
 */

public class Rules
{
    public static Dependency getDependencyLevel(TileData data) {
        Dependency status = Dependency.DEPENDENT;

        if (data.getBuildingType() == BuildingType.NONE)
            return status;


        int[] neighborStats = getNeighborStats(data);
        for (int i = 1; i < BuildingType.values().length; i++) {
            BuildingType buildingType = BuildingType.values()[i];
            if (buildingType.getRequired().length == Const.BUILDING_COUNT) {
                int requiredAmount = buildingType.getRequired()[data.getBuildingType().ordinal() - 1];
                if (requiredAmount > 0) {
                    if (neighborStats[i - 1] > 0)
                        return Dependency.DEPENDENT;
                }
            }
        }

        status = Dependency.INDEPENDENT;
        return status;
    }

    public static boolean isValid(TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

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

    private static int neighborCountOf(BuildingType buildingType, TileData data)
    {
        if(buildingType == BuildingType.NONE)
            throw new IllegalArgumentException();

        return getNeighborStats(data)[buildingType.ordinal() - 1];
    }

    private static int[] getNeighborStats(TileData data)
    {
        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();
        int[] neighborStats = new int[Const.BUILDING_COUNT];

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
