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
    public static List<TileData> getValidBuildings(HexMap<TileData> grid)
    {
        List<TileData> vBuildings = new ArrayList<>();

        for(int i = 0; i < grid.getHexs().length; i++)
        {
            TileData data = (TileData) grid.getHexs()[i].getHexData();
            if(data.getBuildingType() == BuildingType.NONE) continue;

            if(isValid(data))
                vBuildings.add(data);
        }

        return vBuildings;
    }

    public static boolean isValidPlacement(TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

        return isValid(data, selection);
    }

    public static boolean isValid(TileData data)
    {
        return isValid(data, data.getBuildingType());
    }

    public static boolean isValid(TileData data, BuildingType selection)
    {
        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();
        BuildingType buildingTypeTarget = selection;

        int[] requirements = new int[buildingTypeTarget.getRequired().length];
        int[] denied = new int[buildingTypeTarget.getDenied().length];

        for(int i = 0; i < neighbors.size(); i++)
        {
            BuildingType buildingTypeNeed = neighbors.get(i).getHexData().getBuildingType();
            if(buildingTypeNeed == BuildingType.NONE) continue;

            if(isRequired(buildingTypeNeed, buildingTypeTarget) && isValid(neighbors.get(i).getHexData()))
            {
                requirements[buildingTypeNeed.ordinal() - 1]++;
            }
        }

        for(int i = 0; i < neighbors.size(); i++)
        {
            BuildingType buildingTypeNeed = neighbors.get(i).getHexData().getBuildingType();
            if(buildingTypeNeed == BuildingType.NONE) continue;

            if(isDenied(buildingTypeNeed, buildingTypeTarget) && isValid(neighbors.get(i).getHexData()))
            {
                denied[buildingTypeNeed.ordinal() - 1]++;

                int numberOfDenied = denied[buildingTypeNeed.ordinal() - 1];
                int numberIfReachedInvalid = buildingTypeTarget.getDenied()[buildingTypeNeed.ordinal() - 1];

                if(numberOfDenied >= numberIfReachedInvalid)
                {
                    return false;
                }
            }
        }

        for(int i = 0; i < buildingTypeTarget.getRequired().length; i++)
        {
            int numberItHas = requirements[i];

            if(!hasRequiredAmount(buildingTypeTarget, BuildingType.values()[i + 1], numberItHas))
                return false;
        }

        return true;
    }

    public static boolean isRequired(BuildingType buildingNeed, BuildingType buildingTarget)
    {
        if(buildingNeed == BuildingType.NONE || buildingNeed == BuildingType.NONE)
            throw new NoBuildingException();
        return buildingTarget.getRequired()[buildingNeed.ordinal() - 1] > 0;
    }

    public static boolean isDenied(BuildingType buildingNeed, BuildingType buildingTarget)
    {
        if(buildingNeed == BuildingType.NONE || buildingNeed == BuildingType.NONE)
            throw new NoBuildingException();
        return buildingTarget.getDenied()[buildingNeed.ordinal() - 1] > 0;
    }

    public static boolean isANeed(TileData data, List<TileData> lockedBuildings)
    {
        if(data.getBuildingType() == BuildingType.NONE)
            throw new NoBuildingException();

        BuildingType buildingTypeNeed = data.getBuildingType();
        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();
        for(int i = 0; i < neighbors.size(); i++)
        {
            TileData neighborData = neighbors.get(i).getHexData();
            BuildingType buildingTypeTarget = neighborData.getBuildingType();
            if(buildingTypeTarget == BuildingType.NONE) continue;
            int neighborCount = neighborCountOf(buildingTypeNeed, neighborData);

            if(hasExactlyRequiredAmount(buildingTypeTarget, buildingTypeNeed, neighborCount))
            {
                if(lockedBuildings.contains(neighborData))
                {
                    if(isANeed(neighborData, lockedBuildings))
                        return true;
                }else{
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasRequiredAmount(BuildingType buildingTypeTarget, BuildingType buildingTypeNeed, int count)
    {
        return count >= buildingTypeTarget.getRequired()[buildingTypeNeed.ordinal() - 1];
    }

    public static boolean hasMoreThanRequiredAmount(BuildingType buildingTypeTarget, BuildingType buildingTypeNeed, int count)
    {
        return count > buildingTypeTarget.getRequired()[buildingTypeNeed.ordinal() - 1];
    }

    public static boolean hasExactlyRequiredAmount(BuildingType buildingTypeTarget, BuildingType buildingTypeNeed, int count)
    {
        return count == buildingTypeTarget.getRequired()[buildingTypeNeed.ordinal() - 1];
    }

    public static int neighborCountOf(BuildingType buildingType, TileData data)
    {
        int n = 0;

        for(int i = 0; i < data.getParent().getNeighbors().size(); i++)
        {
            Hexagon<TileData> neighbor = data.getParent().getNeighbors().get(i);

            if(buildingType == neighbor.getHexData().getBuildingType())
                n++;
        }

        return n;
    }
}
