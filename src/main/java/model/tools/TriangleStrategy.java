package model.tools;

import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;
/**
 * A strategy for drawing a triangle inside a rectangle created by two points
 */
public class TriangleStrategy implements IShapeStrategy{
    /**
     *<p> This method calculates a triangle by taking two points as an input. The point with the highest y value ((0,0) is the top left corner) will decide the base of the triangle.
     * The base stretches between the x values of the two points. The top of the triangle is the point (middle of the base, lowest y).
     * Finally each point creating the base and each point connecting it's edges to the top point is calculated.</p>
     * @param startPoint a corner in the rectangle limiting the triangle
     * @param endPoint a corner in the rectangle limiting the triangle
     * @return sum of all points on the triangle
     */

    @Override
    public List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint, int size) {
        List<Point<Integer>> result = new ArrayList<>();
        int bottomY = (endPoint.getY() < startPoint.getY()) ? endPoint.getY() : startPoint.getY();
        int topY = (endPoint.getY() > startPoint.getY()) ? endPoint.getY() : startPoint.getY();
        int minX = (startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int maxX = (startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX();

        for(int i = minX + 1; i < maxX; i++){
            for(int j = 0; j < size; j++) {
                result.add(new Point<>(i, topY - j));
            }
        }

        int dy2 = 0;
        int dx2 = 1;
        int big = Math.abs(minX - (minX + (maxX -minX)/2));
        int small = Math.abs(bottomY - topY);
        if(!(big > small)){
            int temp = big;
            big = small;
            small = temp;
            dy2 = -1;
            dx2 = 0;
        }
        int numerator = big >> 1;
        int x = minX;
        int y = topY;
        for(int i=0; i<=big; i++){
            if(i != big) {
                for (int j = 0; j < size; j++) {
                    result.add(new Point<>(x + j, y));
                    result.add(new Point<>(maxX - (x - minX) - j, y));
                }
            }else{
                result.add(new Point<>(x, y));
                result.add(new Point<>(maxX - (x - minX), y));
            }
            numerator += small;
            if(!(numerator < big)){
                numerator -= big;
                x += 1;
                y -= 1;
            }else{
                x += dx2;
                y += dy2;

            }

        }

        return result;
    }
}
