package com.cedricmartens.hexpert.tile;

/**
 * Created by Cedric on 2017-04-22.
 */
public enum BuildingType
{
    NONE(0, new int[]{}, new int[]{}),
    FARM(-1, new int[]{0, 0, 0, 0, 0, 0, 0, 0}, new int[]{1, 0, 0, 0, 0, 0, 0, 0}),
    HOUSE(-1, new int[]{1, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    MINE(1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    WIND(1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    FACTORY(2, new int[]{0, 1, 1, 1, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    MARKET(3, new int[]{0, 1, 0, 1, 1, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    BANK(5, new int[]{0, 1, 1, 1, 0, 1, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0}),
    ROCKET(8, new int[]{0, 1, 0, 1, 1, 0, 1, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0});

    private int score;
    private int[] required;
    private int[] denied;

    BuildingType(int score, int[] required, int[] denied)
    {
        this.score = score;
        this.required = required;
        this.denied = denied;
    }

    public int getScore() {
        return score;
    }

    public int[] getRequired() {
        return required;
    }

    public int[] getDenied() {
        return denied;
    }
}