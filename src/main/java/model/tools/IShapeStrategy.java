package model.tools;

import model.pixel.Point;

import java.util.List;

public interface IShapeStrategy {

    List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint, int size);

}
