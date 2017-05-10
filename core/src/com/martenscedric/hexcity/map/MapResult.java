package com.martenscedric.hexcity.map;

/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private int mapId;
    private int score;
    private int objectivePassed;

    public MapResult() {
    }

    public MapResult(int mapId, int score, int objectivePassed) {
        this.mapId = mapId;
        this.score = score;
        this.objectivePassed = objectivePassed;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getObjectivePassed() {
        return objectivePassed;
    }

    public void setObjectivePassed(int objectivePassed) {
        this.objectivePassed = objectivePassed;
    }
}
