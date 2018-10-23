package model.tools;

import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;

public class RectangleStrategy implements IShapeStrategy {
    @Override
    public List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint, int size) {
        List<Point<Integer>> result = new ArrayList<>();
        int minX = (startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int maxX = (startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int minY = (startPoint.getY() < endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        int maxY = (startPoint.getY() > endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        for(int i = minX; i <= maxX; i++){
            for(int j = 0; j < size; j++){
                result.add(new Point<>(i, minY + j));
                result.add(new Point<>(i, maxY - j));
            }
        }

        for(int i = minY + 1; i < maxY; i++){
            for(int j = 0; j < size; j++) {
                result.add(new Point<>(minX + j, i));
                result.add(new Point<>(maxX - j, i));
            }
        }
        return result;
    }
}
