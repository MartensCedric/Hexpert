package com.martenscedric.hexcity.tile;

/**
 * Created by Cedric on 2017-04-21.
 */
public enum TileType
{
    GRASS("Grass", "A normal tile", 1, 0x11FF38FF),
    WATER("Water", "You cannot build on water", 1, 0x4286F4FF),
    SAND("Sand", "Score on a sand tile is not calculated", 0, 0xe8d17fFF),
    FOREST("Forest", "Score on a forest tile is doubled",2, 0x284919FF);


    private String name;
    private String desc;
    private int multiplier;
    private int color;

    TileType(String name, String desc, int multiplier, int color)
    {
        this.name = name;
        this.multiplier = multiplier;
        this.color = color;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getColor() {
        return color;
    }

    public String getDesc(){return desc;}
}
