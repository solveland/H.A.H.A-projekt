package model.tools;

import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;

public class RectangleStrategy implements IShapeStrategy {
    @Override
    public List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint) {
        List<Point<Integer>> result = new ArrayList<>();
        int minX = (startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int maxX = (startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int minY = (startPoint.getY() < endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        int maxY = (startPoint.getY() > endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        for(int i = minX; i <= maxX; i++){
            result.add(new Point<>(i, minY));
            result.add(new Point<>(i, maxY));
        }
        for(int i = minY + 1; i < maxY; i++){
            result.add(new Point<>(minX, i));
            result.add(new Point<>(maxX, i));
        }
        return result;
    }
}
