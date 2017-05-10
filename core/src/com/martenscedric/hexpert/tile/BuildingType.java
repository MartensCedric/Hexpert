package com.martenscedric.hexpert.tile;

/**
 * Created by Cedric on 2017-04-22.
 */
public enum  BuildingType
{
    NONE("None", 0),
    FARM("Farm", -1),
    HOUSE("House", -1),
    MINE("Mine", 1),
    WIND("Wind turbine", 1),
    FACTORY("Factory", 2),
    MARKET("Market", 3),
    BANK("Bank", 5),
    ROCKET("Rocket", 8);

    private String name;
    private String desc;
    private int score;

    BuildingType(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getScore() {
        return score;
    }
}