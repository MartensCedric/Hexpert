package com.martenscedric.hexcity;

import com.cedricmartens.hexmap.coordinate.Point;

/**
 * Created by 1544256 on 2017-05-08.
 */

public class PointTime
{
    private Point point;
    private float time;

    public PointTime(Point point, float time) {
        this.point = point;
        this.time = time;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
