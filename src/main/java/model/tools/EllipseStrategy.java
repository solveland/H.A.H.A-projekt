package model.tools;

import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;

public class EllipseStrategy implements IShapeStrategy{
    @Override
    public List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint, int size) {
        List<Point<Integer>> result = new ArrayList<>();
        int minX = (startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int maxX = (startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int minY = (startPoint.getY() < endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        int maxY = (startPoint.getY() > endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        double h = maxX - ((maxX - minX) / 2);
        double k = maxY - ((maxY - minY) / 2);
        //double a = ((maxX - minX) >= (maxY - minY)) ? (maxX - minX) : (maxY - minY);
        //double b = ((maxX - minX) <= (maxY - minY)) ? (maxX - minX) : (maxY - minY);
        double a = (maxX - minX);
        double b = (maxY - minY);
        a = a / 2;
        b = b / 2;
        for(int i = minX; i <= maxX; i++){
            for(int j = minY; j <= maxY; j++){
                for(int s = 0; s < size; s++) {
                    double sizedA = a - 2*s;
                    double sizedB = b - 2*s;
                    double small = (sizedA < sizedB) ? sizedA : sizedB;
                    if (((i - h) * (i - h)) / (sizedA * sizedA) + ((j - k) * (j - k) / (sizedB * sizedB)) <= 1 + 1 / small &&
                            ((i - h) * (i - h)) / (sizedA * sizedA) + ((j - k) * (j - k) / (sizedB * sizedB)) >= 1 - 1 / small) {
                        result.add(new Point<>(i, j));
                    }
                }
            }
        }
        return result;
    }
}
