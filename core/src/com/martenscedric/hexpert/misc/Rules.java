package com.martenscedric.hexpert.misc;

import com.cedricmartens.hexmap.hexagon.Hexagon;
import com.martenscedric.hexpert.tile.BuildingType;
import com.martenscedric.hexpert.tile.TileData;

import java.util.List;

import static com.martenscedric.hexpert.tile.BuildingType.ROCKET;

/**
 * Created by martens on 5/7/17.
 */

public class Rules
{
    public static boolean isIndepedent(TileData data)
    {
        if(data.getBuildingType() == BuildingType.NONE)
            return false;

        if(data.getBuildingType() == ROCKET)
            return true;

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

    public static boolean isValid(TileData data, BuildingType selection)
    {
        if(data.getBuildingType() != BuildingType.NONE)
            return false;
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

        switch (selection) {
            case NONE:
                return false;
            case FARM:
                return !farm;
            case HOUSE:
                return farm;
            case MINE:
                return house;
            case WIND:
                return house;
            case FACTORY:
                return house && mine && wind;
            case MARKET:
                return house && wind && factory;
            case BANK:
                return house && wind && mine && market;
            case ROCKET:
                return house && bank && factory && wind;
        }

        return false;
    }
}
