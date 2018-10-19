package model.pixel;

public class DistanceHelper {

    public static double dist2(Point<Double> p1, Point<Double> p2){
        return (p1.getX() - p2.getX())*(p1.getX() - p2.getX()) + (p1.getY() - p2.getY())*(p1.getY() - p2.getY());
    }

    public static double distToSegmentSquared(Point<Double> v, Point<Double> w, Point<Double> p){
        double l2 = dist2(w, v);
        if (l2 == 0.0) return dist2(p, v);
        double t = ((p.getX() - v.getX()) * (w.getX() - v.getX()) + (p.getY() - v.getY()) * (w.getY() - v.getY())) / l2;
        t = Math.max(0, Math.min(1, t));
        return dist2(p,new Point<>(v.getX() + t * (w.getX() - v.getX()),v.getY() + t * (w.getY() - v.getY())));
    }

}
