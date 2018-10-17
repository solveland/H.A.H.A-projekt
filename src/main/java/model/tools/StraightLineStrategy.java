
package model.tools;

import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A strategy for drawing a straight line between two points
 */

public class StraightLineStrategy implements IShapeStrategy {

    /**
     *<p> This method calculates all the points on the line using Bresenham's line algorithm </p>
     * @param startPoint the start of the line
     * @param endPoint the end of the line
     * @return sum of all points on the line
     */
    @Override
    public List<Point<Integer>> shapeStrategy(Point<Integer> startPoint, Point<Integer> endPoint) {
        List<Point<Integer>> result = new ArrayList<>();
        int w = endPoint.getX() - startPoint.getX();
        int h = endPoint.getY() - startPoint.getY();
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
        if(w < 0){
            dx1 = - 1;
            dx2 = - 1;
        }
        else if (w > 0){
            dx1 = 1;
            dx2 = 1;
        }
        if(h < 0){
            dy1 = -1;
        }
        else if (h > 0){
            dy1 = 1;
        }
        int big = Math.abs(w);
        int small = Math.abs(h);
        if(!(big > small)){
            int temp = big;
            big = small;
            small = temp;
            if(h < 0) {
                dy2 = -1;
            }else if( h > 0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = big >> 1;
        int x = startPoint.getX();
        int y = startPoint.getY();
        for(int i=0; i<=big; i++){
            result.add(new Point<>(x, y));
            numerator += small;
            if(!(numerator < big)){
                numerator -= big;
                x += dx1;
                y += dy1;
            }else{
                x += dx2;
                y += dy2;

            }

        }
        return result;
    }
}
