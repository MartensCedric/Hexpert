package com.martenscedric.hexpert.map;

import com.martenscedric.hexpert.tile.BuildingType;

/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private int score;
    private boolean[] objectivePassed;
    private BuildingType[] buildings;

    public MapResult() {
    }

    public MapResult(int score, boolean[] objectivePassed) {
        this.score = score;
        this.objectivePassed = objectivePassed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean[] getObjectivePassed() {
        return objectivePassed;
    }

    public void setObjectivePassed(boolean[] objectivePassed) {
        this.objectivePassed = objectivePassed;
    }

    public BuildingType[] getBuildings() {
        return buildings;
    }

    public void setBuildings(BuildingType[] buildings) {
        this.buildings = buildings;
    }
}
