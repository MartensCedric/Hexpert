package com.martenscedric.hexmap;

/**
 * Created by Cedric Martens on 2017-04-27.
 */

public class Point
{
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o)
    {
        Point p = (Point)o;
        return p.x == this.x && p.y == this.y;
    }
}
