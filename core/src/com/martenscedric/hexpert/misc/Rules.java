package com.martenscedric.hexpert.misc;

import com.cedricmartens.hexmap.hexagon.Hexagon;
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
    public static final int DEPENDENT = 0;
    public static final int INDEPENDENT = 1;
    public static final int PARTIALLY_INDEPENDENT = 2;

    public static boolean isIndependent(TileData data)
    {

        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();

        boolean farm = false;
        boolean house = false;
        boolean mine = false;
        boolean wind = false;
        boolean factory = false;
        boolean market = false;
        boolean bank = false;
        boolean rocket = false;


        for(int i = 0; i < neighbors.size(); i++)
        {
            TileData tileData = neighbors.get(i).getHexData();

            switch (tileData.getBuildingType()) {
                case NONE:
                    break;
                case FARM:
                    farm = true;
                    break;
                case HOUSE:
                    house = true;
                    break;
                case MINE:
                    mine = true;
                    break;
                case WIND:
                    wind = true;
                    break;
                case FACTORY:
                    factory = true;
                    break;
                case MARKET:
                    market = true;
                    break;
                case BANK:
                    bank = true;
                    break;
                case ROCKET:
                    rocket = true;
                    break;
            }
        }

        switch (data.getBuildingType()) {
            case FARM:
                return !house;
            case HOUSE:
                return !mine && !wind && !factory && !market && !bank && !rocket;
            case MINE:
                return !factory && !bank;
            case WIND:
                return !factory && !market && !bank && !rocket;
            case FACTORY:
                return !market && !rocket;
            case MARKET:
                return !bank;
            case BANK:
                return !rocket;
        }

        return false;
    }

    public static boolean isPartiallyIndependant()
    {

        return false;
    }

    public static boolean isValid(TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;

        List<Hexagon<TileData>> neighbors = data.getParent().getNeighbors();

        int[] neighborStats = new int[Const.BUILDING_COUNT];

        for(int i = 0; i < neighbors.size(); i++) {
            TileData tileData = neighbors.get(i).getHexData();

            if(tileData.getBuildingType() != BuildingType.NONE)
            {
                neighborStats[tileData.getBuildingType().ordinal() - 1]++;
            }
        }

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
        int n = 0;
        for(int i = 0; i < data.getParent().getNeighbors().size(); i++)
        {
            BuildingType neighborType = data.getParent().getNeighbors().get(i).getHexData().getBuildingType();
            if(buildingType == neighborType)
                n++;
        }

        return n;
    }
}
