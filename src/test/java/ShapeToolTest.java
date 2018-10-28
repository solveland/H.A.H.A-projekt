import model.ImageModel;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.pixel.PaintColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import model.pixel.Point;
import model.tools.EllipseStrategy;
import model.tools.RectangleStrategy;
import model.tools.StraightLineStrategy;
import model.tools.TriangleStrategy;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(JUnitQuickcheck.class)
public class ShapeToolTest {

    @Property
    public void drawRectangleTest(@InRange(minInt = 0, maxInt = 149) int startPointX,
                                  @InRange(minInt = 0, maxInt = 149) int startPointY,
                                  @InRange(minInt = 0, maxInt = 149) int endPointX,
                                  @InRange(minInt = 0, maxInt = 149) int endPointY,
                                  @InRange(minInt = 0, maxInt = 255) int green,
                                  @InRange(minInt = 0, maxInt = 255) int blue,
                                  @InRange(minInt = 0, maxInt = 255) int red,
                                  @InRange(minInt = 150, maxInt = 200) int limit){

        ImageModel im = new ImageModel(limit, limit);
        im.activateShapeTool();
        im.setSize(1);
        im.setToolShape("Rectangle");
        PaintColor color = new PaintColor(red, green, blue);
        PaintColor white = new PaintColor(255, 255, 255);
        im.setColor(color);
        im.onPress(startPointX, startPointY, );
        im.onRelease(endPointX, endPointY, );

        int minY = (endPointY < startPointY) ? endPointY : startPointY;
        int maxY = (endPointY > startPointY) ? endPointY : startPointY;
        int minX = (startPointX < endPointX) ? startPointX : endPointX;
        int maxX = (startPointX > endPointX) ? startPointX : endPointX;

        RectangleStrategy rs = new RectangleStrategy();
        List<Point<Integer>> rectangle = rs.shapeStrategy(new Point<>(startPointX, startPointY), new Point<>(endPointX, endPointY), 1);

        //Checks that the generated rectangle is painted on the image
        for(Point p : rectangle){
            assertEquals(im.getPixelColor(p.getX().intValue(), p.getY().intValue()), color);
        }

        //Checks that generated rectangle is painted inside the specified interval and that nothing else is affected
        for(int i = minX; i <= maxX; i++){
            for(int j = minX; j <= maxX; j++) {
                if(!(rectangle.contains(new Point<>(i, j)))){
                    assertEquals(im.getPixelColor(i, j), white);
                }
            }
        }

        //Makes sure that nothing is affected outside the specified interval
        for (int i = 0; i < limit; i++){
            for(int j = 0; j < limit; j++){
                if (im.getPixelColor(i, j) == color){
                    assertTrue(i >= minX && i <= maxX && j >= minY && j <= maxY);
                }
            }
        }
        
    }

    @Property
    public void drawTriangleTest(@InRange(minInt = 0, maxInt = 149) int startPointX,
                                  @InRange(minInt = 0, maxInt = 149) int startPointY,
                                  @InRange(minInt = 0, maxInt = 149) int endPointX,
                                  @InRange(minInt = 0, maxInt = 149) int endPointY,
                                  @InRange(minInt = 0, maxInt = 255) int green,
                                  @InRange(minInt = 0, maxInt = 255) int blue,
                                  @InRange(minInt = 0, maxInt = 255) int red,
                                  @InRange(minInt = 150, maxInt = 200) int limit){

        ImageModel im = new ImageModel(limit, limit);
        im.activateShapeTool();
        im.setSize(1);
        im.setToolShape("Triangle");
        PaintColor color = new PaintColor(red, green, blue);
        PaintColor white = new PaintColor(255, 255, 255);
        im.setColor(color);
        im.onPress(startPointX, startPointY, );
        im.onRelease(endPointX, endPointY, );

        int minY = (endPointY < startPointY) ? endPointY : startPointY;
        int maxY = (endPointY > startPointY) ? endPointY : startPointY;
        int minX = (startPointX < endPointX) ? startPointX : endPointX;
        int maxX = (startPointX > endPointX) ? startPointX : endPointX;

        TriangleStrategy ts = new TriangleStrategy();
        List<Point<Integer>> triangle = ts.shapeStrategy(new Point<>(startPointX, startPointY), new Point<>(endPointX, endPointY), 1);

        //Checks that the generated triangle is painted on the image
        for(Point p : triangle){
            assertEquals(im.getPixelColor(p.getX().intValue(), p.getY().intValue()), color);
        }

        //Checks that generated triangle is painted inside the specified interval and that nothing else is affected
        for(int i = minX; i <= maxX; i++){
            for(int j = minX; j <= maxX; j++) {
                if(!(triangle.contains(new Point<>(i, j)))) {
                    assertEquals(im.getPixelColor(i, j), white);
                }
            }
        }

        //Makes sure that nothing is affected outside the specified interval
        for (int i = 0; i < limit; i++){
            for(int j = 0; j < limit; j++){
                if (im.getPixelColor(i, j) == color){
                    assertTrue(i >= minX && i <= maxX && j >= minY && j <= maxY);
                }
            }
        }


    }

    @Property
    public void drawLineTest(@InRange(minInt = 0, maxInt = 149) int startPointX,
                                 @InRange(minInt = 0, maxInt = 149) int startPointY,
                                 @InRange(minInt = 0, maxInt = 149) int endPointX,
                                 @InRange(minInt = 0, maxInt = 149) int endPointY,
                                 @InRange(minInt = 0, maxInt = 255) int green,
                                 @InRange(minInt = 0, maxInt = 255) int blue,
                                 @InRange(minInt = 0, maxInt = 255) int red,
                                 @InRange(minInt = 150, maxInt = 200) int limit){

        ImageModel im = new ImageModel(limit, limit);
        im.activateShapeTool();
        im.setSize(1);
        im.setToolShape("Line");
        PaintColor color = new PaintColor(red, green, blue);
        PaintColor white = new PaintColor(255, 255, 255);
        im.setColor(color);
        im.onPress(startPointX, startPointY, );
        im.onRelease(endPointX, endPointY, );

        int minY = (endPointY < startPointY) ? endPointY : startPointY;
        int maxY = (endPointY > startPointY) ? endPointY : startPointY;
        int minX = (startPointX < endPointX) ? startPointX : endPointX;
        int maxX = (startPointX > endPointX) ? startPointX : endPointX;

        StraightLineStrategy sts = new StraightLineStrategy();
        List<Point<Integer>> line = sts.shapeStrategy(new Point<>(startPointX, startPointY), new Point<>(endPointX, endPointY), 1);

        //Checks that the generated line is painted on the image
        for(Point p : line){
           assertEquals(im.getPixelColor(p.getX().intValue(), p.getY().intValue()), color);
        }

        //Checks that nothing else is affected inside interval
        for(int i = minX; i <= maxX; i++){
            for(int j = minX; j <= maxX; j++) {
                if(!(line.contains(new Point<>(i, j)))) {
                    assertEquals(im.getPixelColor(i, j), white);
                }
            }
        }

        //Makes sure that nothing is affected outside the specified interval
        for (int i = 0; i < limit; i++){
            for(int j = 0; j < limit; j++){
                if (im.getPixelColor(i, j) == color){
                    assertTrue(i >= minX && i <= maxX && j >= minY && j <= maxY);
                }
            }
        }

    }
    @Property
    public void drawEllipseTest(@InRange(minInt = 0, maxInt = 149) int startPointX,
                             @InRange(minInt = 0, maxInt = 149) int startPointY,
                             @InRange(minInt = 0, maxInt = 149) int endPointX,
                             @InRange(minInt = 0, maxInt = 149) int endPointY,
                             @InRange(minInt = 0, maxInt = 255) int green,
                             @InRange(minInt = 0, maxInt = 255) int blue,
                             @InRange(minInt = 0, maxInt = 255) int red,
                             @InRange(minInt = 150, maxInt = 200) int limit){

        ImageModel im = new ImageModel(limit, limit);
        im.activateShapeTool();
        im.setSize(1);
        im.setToolShape("Circle");
        PaintColor color = new PaintColor(red, green, blue);
        PaintColor white = new PaintColor(255, 255, 255);
        im.setColor(color);
        im.onPress(startPointX, startPointY, );
        im.onRelease(endPointX, endPointY, );

        int minY = (endPointY < startPointY) ? endPointY : startPointY;
        int maxY = (endPointY > startPointY) ? endPointY : startPointY;
        int minX = (startPointX < endPointX) ? startPointX : endPointX;
        int maxX = (startPointX > endPointX) ? startPointX : endPointX;

        EllipseStrategy es = new EllipseStrategy();
        List<Point<Integer>> ellipse = es.shapeStrategy(new Point<>(startPointX, startPointY), new Point<>(endPointX, endPointY), 1);

        //Checks that the generated ellipse is painted on the image
        for(Point p : ellipse){
            assertEquals(im.getPixelColor(p.getX().intValue(), p.getY().intValue()), color);
        }

        //Checks that nothing else is affected inside interval
        for(int i = minX; i <= maxX; i++){
            for(int j = minX; j <= maxX; j++) {
                if(!(ellipse.contains(new Point<>(i, j)))) {
                    assertEquals(im.getPixelColor(i, j), white);
                }
            }
        }

        //Makes sure that nothing is affected outside the specified interval
        for (int i = 0; i < limit; i++){
            for(int j = 0; j < limit; j++){
                if (im.getPixelColor(i, j) == color){
                    assertTrue(i >= minX && i <= maxX && j >= minY && j <= maxY);
                }
            }
        }

    }
}
