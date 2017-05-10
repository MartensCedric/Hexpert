package com.martenscedric.hexpert.map;

import com.cedricmartens.hexmap.map.HexMap;
import com.martenscedric.hexpert.tile.TileData;

/**
 * Created by 1544256 on 2017-05-10.
 */
public class Objective
{
    private int minScore;
    private int[] buildingRequirement;

    public Objective() {
    }

    public Objective(int[] buildingRequirement, int minScore) {

        this.buildingRequirement = buildingRequirement;
        this.minScore = minScore;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public int[] getBuildingRequirement() {
        return buildingRequirement;
    }

    public void setBuildingRequirement(int[] buildingRequirement) {
        this.buildingRequirement = buildingRequirement;
    }


    public static boolean hasPassed(HexMap<TileData> grid, Objective objectives)
    {
        return false;
    }
}
