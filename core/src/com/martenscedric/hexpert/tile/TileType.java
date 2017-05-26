package com.martenscedric.hexpert.tile;

/**
 * Created by Cedric on 2017-04-21.
 */
public enum TileType
{
    GRASS("Grass", "A normal tile", 1),
    WATER("Water", "You cannot build on water", 1),
    SAND("Sand", "Score on a sand tile is not calculated", 0),
    FOREST("Forest", "Score on a forest tile is doubled",2);


    private String name;
    private String desc;
    private int multiplier;
    TileType(String name, String desc, int multiplier)
    {
        this.name = name;
        this.multiplier = multiplier;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public String getDesc(){return desc;}
}
