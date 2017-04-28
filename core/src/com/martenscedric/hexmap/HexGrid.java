package com.martenscedric.hexmap;

/**
 * Created by Shawn Martens on 2017-04-27.
 */

public class HexGrid
{
    private Point origin;
    private HexagonShape shape;
    private int width;
    private int height;

    public HexGrid()
    {
        origin = new Point(0, 0);
        width = 1;
        height = 1;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public void setShape(HexagonShape shape) {
        this.shape = shape;
    }

    public HexGrid build()
    {
        return null;
    }
}
