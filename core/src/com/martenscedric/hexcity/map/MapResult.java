package com.martenscedric.hexcity.map;

/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private int mapId;
    private int score;

    public MapResult() {
    }

    public MapResult(int mapId, int score) {
        this.mapId = mapId;
        this.score = score;
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
}
