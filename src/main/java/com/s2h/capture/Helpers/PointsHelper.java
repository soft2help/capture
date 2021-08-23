package com.s2h.capture.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PointsHelper {

    public static Point findCentroid(List<Point> points) {
        int x = 0;
        int y = 0;
        for (Point p : points) {
            x += p.x;
            y += p.y;
        }
        Point center = new Point(0, 0);
        center.x = x / points.size();
        center.y = y / points.size();
        return center;
    }

    public static List<Point> sortVerticies(List<Point> points) {
        // get centroid
        Point center = findCentroid(points);
        Collections.sort(points, (a, b) -> {
            double a1 = (Math.toDegrees(Math.atan2(a.x - center.x, a.y - center.y)) - 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(b.x - center.x, b.y - center.y)) - 360) % 360;
            return (int) (a2 - a1);
        });

        List<Point> reOrder = new ArrayList<Point>();

        reOrder.add(points.get(1));
        reOrder.add(points.get(2));
        reOrder.add(points.get(3));
        reOrder.add(points.get(0));

        return reOrder;
    }

    public static Point findUpperLeftPoint(List<Point> points) {
        double xMin = 200000.0;
        double yMin = 200000.0;
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.x < xMin)
                xMin = point.x;

            if (point.y < yMin)
                yMin = point.y;

        }

        return new Point(xMin, yMin);
    }

    public static Point findDownRightPoint(List<Point> points) {
        double xMax = 0.0;
        double yMax = 0.0;
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.x > xMax)
                xMax = point.x;

            if (point.y > yMax)
                yMax = point.y;

        }

        return new Point(xMax, yMax);
    }

    public static double widthBetweenPoints(Point point1, Point point2) {
        return point2.x - point1.x;
    }

    public static double heigthBetweenPoints(Point point1, Point point2) {
        return point2.y - point1.y;
    }

    public static List<Point> cropPolygon(Point upperLeft, Point rightDown, double scaleX, double scaleY) {
        Point p1 = new Point(upperLeft.x, upperLeft.y);
        Point p2 = new Point(rightDown.x, upperLeft.y);
        Point p3 = new Point(rightDown.x, rightDown.y);
        Point p4 = new Point(upperLeft.x, rightDown.y);

        List<Point> polygon = new ArrayList<Point>();
        polygon.add(p1);
        polygon.add(p2);
        polygon.add(p3);
        polygon.add(p4);

        return polygon;
    }

    public static List<Point> convert(List<Point> cropPolygon, List<Point> selectedPolygon, double scalex, double scaley) {
      
        List<Point> diference = new ArrayList<Point>();

        Point point1 = cropPolygon.get(0);
        Point point2 = selectedPolygon.get(0);
        Point p1 = new Point(Math.abs(point2.x - point1.x) * scalex, Math.abs(point2.y - point1.y) * scaley);
        diference.add(p1);
        

        point1 = cropPolygon.get(0);
        point2 = selectedPolygon.get(1);
        Point p2 = new Point(Math.abs(point2.x - point1.x) * scalex, Math.abs(point2.y - point1.y) * scaley);
        diference.add(p2);        

        point1 = cropPolygon.get(0);
        point2 = selectedPolygon.get(2);
        Point p3 = new Point(Math.abs(point2.x - point1.x) * scalex, Math.abs(point2.y - point1.y) * scaley);
        diference.add(p3);       

        point1 = cropPolygon.get(0);
        point2 = selectedPolygon.get(3);
        Point p4 = new Point(Math.abs(point2.x - point1.x) * scalex, Math.abs(point2.y - point1.y) * scaley);
        diference.add(p4);

        return diference;
    }

    public static List<Point> toPrespective(int width, int height) {
        List<Point> toPrespective = new ArrayList<Point>();
        Point p1 = new Point(0.0, 0.0);
        Point p2 = new Point(Double.valueOf(width), 0.0);
        Point p3 = new Point(Double.valueOf(width), Double.valueOf(height));
        Point p4 = new Point(0.0, Double.valueOf(height));

        toPrespective.add(p1);
        toPrespective.add(p2);
        toPrespective.add(p3);
        toPrespective.add(p4);

        return toPrespective;
    }

    public static void printPoints(List<Point> points, String label) {
        System.out.println(label);
        for (Point p : points)
            System.out.println(p);
    }

}
