import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.ImageModel;
import model.pixel.PaintColor;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnitQuickcheck.class)

public class SelectTest {

    @Property

    //Test - Select an area, only this area can be edited/drawn on.

    public void selectedAreaCanOnlyBeDrawnOn(@InRange(minInt=1, maxInt=1) int size,
                                             @InRange(minInt = 0, maxInt = 50) int x,
                                             @InRange(minInt = 0, maxInt = 50) int y,
                                             @InRange(minInt = 52, maxInt = 100) int a,
                                             @InRange(minInt = 52, maxInt = 100) int b,
                                             @InRange(minInt = 1, maxInt = 99) int c,
                                             @InRange(minInt = 1, maxInt = 99) int d,
                                             @InRange(minInt = 0, maxInt = 255) int green,
                                             @InRange(minInt = 0, maxInt = 255) int blue,
                                             @InRange(minInt = 0, maxInt = 255) int red,
                                             @InRange(minInt = 150, maxInt = 200) int limit) {

        ImageModel image = new ImageModel(limit, limit);
        PaintColor initColor = new PaintColor(red, blue, green);
        PaintColor white = new PaintColor(255,255,255);

        image.activateSelectTool();
        image.onPress(x,y);
        image.onRelease(a,b);

        //If an area has been selected
        assertTrue(image.getActiveLayer().hasSelectedArea());

        //Draw a pixel on canvas. If inside of selected area then pixel will be drawn.
        // If outside of selected area, then pixel will not be drawn.
        image.activatePencilTool();
        image.setSize(size);
        image.setColor(initColor);
        image.onPress(c,d);

        if ((c > x && d > y) && (c < a && d < b)){
            assertEquals(image.getActiveLayer().getPixel(c,d),initColor);
            } else {
            assertEquals(image.getActiveLayer().getPixel(c,d), white);
        }
    }


    @Property

    //Test - deselectArea

    public void deselectArea(@InRange(minInt=1, maxInt=1) int size,
                                             @InRange(minInt = 0, maxInt = 50) int x,
                                             @InRange(minInt = 0, maxInt = 50) int y,
                                             @InRange(minInt = 52, maxInt = 100) int a,
                                             @InRange(minInt = 52, maxInt = 100) int b,
                                             @InRange(minInt = 1, maxInt = 99) int c,
                                             @InRange(minInt = 1, maxInt = 99) int d,
                                             @InRange(minInt = 0, maxInt = 255) int green,
                                             @InRange(minInt = 0, maxInt = 255) int blue,
                                             @InRange(minInt = 0, maxInt = 255) int red,
                                             @InRange(minInt = 150, maxInt = 200) int limit) {

        ImageModel image = new ImageModel(limit, limit);
        PaintColor initColor = new PaintColor(red, blue, green);
        PaintColor white = new PaintColor(255,255,255);

        image.activateSelectTool();
        image.onPress(x,y);
        image.onRelease(a,b);

        //If an area has been selected
        assertTrue(image.getActiveLayer().hasSelectedArea());

        //Draw a pixel on canvas. If inside of selected area then pixel will be drawn.
        // If outside of selected area, then pixel will not be drawn.
        image.activatePencilTool();
        image.setSize(size);
        image.setColor(initColor);
        image.onPress(c,d);

        if ((c > x && d > y) && (c < a && d < b)){
            assertEquals(image.getActiveLayer().getPixel(c,d),initColor);
        } else {
            assertEquals(image.getActiveLayer().getPixel(c,d), white);
        }

        //deselecting area
        image.deselectArea();

        image.activatePencilTool();
        image.setSize(size);
        image.setColor(initColor);
        image.onPress(c,d);

        //If inside the deleted area, draw pixel. If outside the deleted area, draw pixel. Selected area has been deselected.
        if ((c > x && d > y) && (c < a && d < b)){
            assertEquals(image.getActiveLayer().getPixel(c,d),initColor);
        } else {
            assertEquals(image.getActiveLayer().getPixel(c,d), initColor);
        }
    }
}


