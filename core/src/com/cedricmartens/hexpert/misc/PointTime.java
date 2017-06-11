package com.cedricmartens.hexpert.misc;

import com.cedricmartens.hexmap.coordinate.Point;

/**
 * Created by 1544256 on 2017-05-08.
 */

public class PointTime
{
    public double x;
    public double y;
    private float time;

    public PointTime(Point point, float time) {
        this.x = point.x;
        this.y = point.y;
        this.time = time;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
