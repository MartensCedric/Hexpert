package com.martenscedric.hexpert.tile;

/**
 * Created by Cedric on 2017-04-22.
 */
public enum  BuildingType
{
    NONE("None", "You should never see this message.", 0),
    FARM("Farm", "Cannot be placed next to another farm.", -1),
    HOUSE("Worker house", "Requires :\n-Farm", -1),
    MINE("Mine", "Requires :\n-House", 1),
    WIND("Wind turbine", "Requires :\n-House", 1),
    FACTORY("Factory", "Requires :\n-House\n-Wind Turbine\n-Mine", 2),
    MARKET("Market", "Requires :\n-House\n-Wind Turbine\n-Factory", 3),
    BANK("Bank", "Requires :\n-House\n-Wind Turbine\n-Mine\n-Market", 5),
    ROCKET("Rocket", "Requires :\n-House\n-Wind Turbine\n-Factory\n-Bank", 8);

    private String name;
    private String desc;
    private int score;

    BuildingType(String name, String desc, int score)
    {
        this.name = name;
        this.desc = desc;
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