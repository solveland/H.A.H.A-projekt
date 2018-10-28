package model.utilities;

import model.pixel.Point;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: A helper class for functions that calculate distances between points/lines
USED BY: BrushTool
USES: Point
 */

public class DistanceHelper {

    private static double distSquared(Point<Double> p1, Point<Double> p2){
        return (p1.getX() - p2.getX())*(p1.getX() - p2.getX()) + (p1.getY() - p2.getY())*(p1.getY() - p2.getY());
    }

    /**
     * Returns the squared distance between the line segment v->w to the point p
     * @param v The startpoint of the line segment
     * @param w The endpoint of the line segment
     * @param p The point outside of the line segment
     * @return the squared distance between the line segment v->w to the point p
     */
    public static double distToSegmentSquared(Point<Double> v, Point<Double> w, Point<Double> p){
        double l2 = distSquared(w, v);
        if (l2 == 0.0) {
            return distSquared(p, v);
        }
        double t = ((p.getX() - v.getX()) * (w.getX() - v.getX()) + (p.getY() - v.getY()) * (w.getY() - v.getY())) / l2;
        t = Math.max(0, Math.min(1, t));
        return distSquared(p,new Point<>(v.getX() + t * (w.getX() - v.getX()),v.getY() + t * (w.getY() - v.getY())));
    }

}
