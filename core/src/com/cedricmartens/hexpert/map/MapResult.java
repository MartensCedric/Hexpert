package com.cedricmartens.hexpert.map;

/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private int score;
    private boolean[] objectivePassed;
    private com.cedricmartens.hexpert.tile.BuildingType[] buildings;

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

    public com.cedricmartens.hexpert.tile.BuildingType[] getBuildings() {
        return buildings;
    }

    public void setBuildings(com.cedricmartens.hexpert.tile.BuildingType[] buildings) {
        this.buildings = buildings;
    }
}
