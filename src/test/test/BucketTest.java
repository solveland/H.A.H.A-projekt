import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.ImageModel;
import model.PaintLayer;
import model.pixel.PaintColor;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;



@RunWith(JUnitQuickcheck.class)

public class BucketTest {

    @Property

    //Test - FillTool on a canvas with one color.

    public void bucketToolOnlyBackground(@InRange(minInt = 0, maxInt = 149) int x,
                                         @InRange(minInt = 0, maxInt = 149) int y,
                                         @InRange(minInt = 0, maxInt = 255) int green,
                                         @InRange(minInt = 0, maxInt = 255) int blue,
                                         @InRange(minInt = 0, maxInt = 255) int red,
                                         @InRange(minInt = 150, maxInt = 200) int limit) {

        ImageModel image = new ImageModel(limit, limit);

        PaintColor initColor = new PaintColor(red, blue, green);
        PaintLayer paintLayer = new PaintLayer(limit, limit, new PaintColor(255, 255, 255), "test");

        image.setActiveLayer(paintLayer);
        image.activateFillTool();
        image.setColor(initColor);
        image.onPress(x, y);

        for (int xi = 0; xi < limit; xi++) {
            for (int yi = 0; yi < limit; yi++) {

                assertTrue(image.getActiveLayer().getPixel(xi, yi).equals(initColor));

            }
        }
    }

    @Property

    //Test - FillTool on a black pixel with white background.

    public void bucketToolWithOneBlackPixel(@InRange(minInt = 0, maxInt = 149) int x,
                                            @InRange(minInt = 0, maxInt = 149) int y,
                                            @InRange(minInt = 0, maxInt = 255) int green,
                                            @InRange(minInt = 0, maxInt = 255) int blue,
                                            @InRange(minInt = 0, maxInt = 255) int red,
                                            @InRange(minInt = 150, maxInt = 200) int limit) {

        ImageModel image = new ImageModel(limit, limit);
        PaintColor white = new PaintColor(255, 255, 255);
        PaintColor black = new PaintColor(0, 0, 0);

        PaintColor initColor = new PaintColor(red, blue, green);
        PaintLayer paintLayer = new PaintLayer(limit, limit, new PaintColor(255, 255, 255), "test");

        image.setActiveLayer(paintLayer);
        image.activateFillTool();

        image.setColor(white);
        image.onPress(x, y);
        image.getActiveLayer().setPixel(x, y, black);
        image.setColor(initColor);
        image.onPress(x, y);

        for (int xi = 0; xi < limit; xi++) {
            for (int yi = 0; yi < limit; yi++) {
                if (xi == x && yi == y) {
                    assertEquals(image.getActiveLayer().getPixel(xi, yi), initColor);
                } else {
                    assertEquals(image.getActiveLayer().getPixel(xi, yi), white);
                }

            }
        }
    }


    @Property

    //Test - FillTool on a rectangle with black edges with white background.

    public void bucketToolWithARectangle(
                                         @InRange(minInt = 51, maxInt = 99) int c,
                                         @InRange(minInt = 51, maxInt = 99) int d,
                                         @InRange(minInt = 0, maxInt = 255) int green,
                                         @InRange(minInt = 0, maxInt = 255) int blue,
                                         @InRange(minInt = 0, maxInt = 255) int red,
                                         @InRange(minInt = 150, maxInt = 200) int limit) {
        ImageModel image = new ImageModel(limit, limit);
        PaintColor white = new PaintColor(255, 255, 255);
        PaintColor black = new PaintColor(0, 0, 0);

        int x = 50;
        int y = 50;
        int a = 120;
        int b = 120;

        PaintColor initColor = new PaintColor(red, blue, green);
        PaintLayer paintLayer = new PaintLayer(limit, limit, new PaintColor(255, 255, 255), "test");
        image.setActiveLayer(paintLayer);

        image.activateFillTool();
        image.setColor(white);
        image.onPress(x, y);

        image.activateShapeTool();
        image.getShapeTool().setRectangleStrategy();
        image.setColor(black);
        image.onPress(x, y);
        image.onRelease(a, b);

        image.activateFillTool();
        image.setColor(initColor);
        image.onPress(c,d);


        for (int xi = 0; xi < limit; xi++) {
            for (int yi = 0; yi < limit; yi++) {
                if((xi > x && yi > y) && (xi < a && yi < b)){
                    assertEquals(image.getActiveLayer().getPixel(xi, yi), initColor);
                } else if ((xi < x || yi < y) || (xi > a || yi > b)){
                    assertEquals(image.getActiveLayer().getPixel(xi, yi), white);
                }

            }
        }
    }
}































