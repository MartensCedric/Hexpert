package com.martenscedric.hexpert.misc;

import com.cedricmartens.hexmap.coordinate.Point;

/**
 * Created by Shawn Martens on 2017-05-08.
 */

public class IntPointTime extends PointTime {
    private int n;
    public IntPointTime(int n, Point point, float time) {
        super(point, time);
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
