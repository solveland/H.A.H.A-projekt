package model.tools;

import model.utils.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface IShapeStrategy {

    List<Point<Integer>> lineStrategy(Point<Integer> startPoint, Point<Integer> endPoint);

}
