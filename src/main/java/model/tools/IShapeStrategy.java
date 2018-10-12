package model.tools;

import model.utils.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface IShapeStrategy {

    List<Point> lineStrategy(Point startPoint, Point endPoint);

}
