package com.martenscedric.hexmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cedric Martens on 2017-04-27.
 */

public class HexCoord
{
    private HexagonOrientation orientation;
    private Point middlePoint;
    private List<Point> points;
    private double radius;

    public HexCoord(Point middlePoint, double radius, HexagonOrientation orientation)
    {
        if(radius <= 0)
            throw new IllegalArgumentException();

        this.middlePoint = middlePoint;
        this.radius = radius;
        this.points = new ArrayList<Point>();
        this.orientation = orientation;

        for(int i = 0; i < 6; i++)
        {
            double degrees = i * 60;
            degrees += orientation == HexagonOrientation.POINTY_TOP ? 30 : 0;
            double rad = Math.PI/180 * degrees;
            points.add(i, new Point(
                middlePoint.x + radius * Math.cos(rad),
                middlePoint.y + radius * Math.sin(rad))
            );
        }
    }

    public List<Point> getPoints()
    {
        return points;
    }

    private double getHeight()
    {
        return radius * 2;
    }

    private double getWidth()
    {
        return Math.sqrt(3)/2 * getHeight();
    }
}
