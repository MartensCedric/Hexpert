package com.cedricmartens.hexpert.tile;

/**
 * Created by Cedric on 2017-04-22.
 */
public enum BuildingType
{
    NONE(0, new int[]{}, new int[]{}),
    FARM(-1, new int[]{}, new int[]{1, 0, 0, 0, 0, 0, 0, 0}),
    HOUSE(-1, new int[]{1, 0, 0, 0, 0, 0, 0, 0}, new int[]{}),
    MINE(1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{}),
    WIND(1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{}),
    FACTORY(2, new int[]{0, 1, 1, 1, 0, 0, 0, 0}, new int[]{}),
    MARKET(3, new int[]{0, 1, 0, 1, 1, 0, 0, 0}, new int[]{}),
    BANK(5, new int[]{0, 1, 1, 1, 0, 1, 0, 0}, new int[]{}),
    ROCKET(8, new int[]{0, 1, 0, 1, 1, 0, 1, 0}, new int[]{});

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