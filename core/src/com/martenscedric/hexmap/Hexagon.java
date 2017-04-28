package com.martenscedric.hexmap;

/**
 * Created by Shawn Martens on 2017-04-27.
 */

public class Hexagon<T extends HexagonData>
{
    private HexCoord hexCoord;

    public Hexagon(Point center, double radius) {
        hexCoord = new HexCoord(center, radius, HexagonOrientation.POINTY_TOP);
    }
}
