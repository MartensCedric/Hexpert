package com.martenscedric.hexpert.map;

/**
 * Created by 1544256 on 2017-05-09.
 */

public class MapResult
{
    private int score;
    private boolean[] objectivePassed;

    public MapResult() {
    }

    public MapResult(int mapId, int score, boolean[] objectivePassed) {
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
}
