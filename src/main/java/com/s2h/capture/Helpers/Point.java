package com.s2h.capture.Helpers;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x=" + x + " y=" + y;
    }
}
