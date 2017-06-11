package com.cedricmartens.hexpert.tile;

/**
 * Created by Cedric on 2017-04-22.
 */
public enum  BuildingType
{
    NONE("None", 0, new int[]{}, new int[]{}),
    FARM("Farm", -1, new int[]{}, new int[]{1, 0, 0, 0, 0, 0, 0, 0}),
    HOUSE("House", -1, new int[]{1, 0, 0, 0, 0, 0, 0, 0}, new int[]{}),
    MINE("Mine", 1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{}),
    WIND("Wind turbine", 1, new int[]{0, 1, 0, 0, 0, 0, 0, 0}, new int[]{}),
    FACTORY("Factory", 2, new int[]{0, 1, 1, 1, 0, 0, 0, 0}, new int[]{}),
    MARKET("Market", 3, new int[]{0, 1, 0, 1, 1, 0, 0, 0}, new int[]{}),
    BANK("Bank", 5, new int[]{0, 1, 1, 1, 0, 1, 0, 0}, new int[]{}),
    ROCKET("Rocket", 8, new int[]{0, 1, 0, 1, 1, 0, 1, 0}, new int[]{});


    private String name;
    private int score;
    private int[] required;
    private int[] denied;

    BuildingType(String name, int score, int[] required, int[] denied)
    {
        this.name = name;
        this.score = score;
        this.required = required;
        this.denied = denied;
    }

    public String getName() {
        return name;
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