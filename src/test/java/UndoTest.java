import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.ImageModel;
import model.pixel.PaintColor;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitQuickcheck.class)

public class UndoTest {

    @Property

    //Test to make sure that undoing everything resets the image to its starting state


    public void undoTest(@InRange(minInt = 0, maxInt = 149) int x1,
                                         @InRange(minInt = 0, maxInt = 149) int y1,
                                         @InRange(minInt = 0, maxInt = 149) int x2,
                                         @InRange(minInt = 0, maxInt = 149) int y2,
                                         @InRange(minInt = 0, maxInt = 149) int x3,
                                         @InRange(minInt = 0, maxInt = 149) int y3,
                                         @InRange(minInt = 0, maxInt = 149) int x4,
                                         @InRange(minInt = 0, maxInt = 149) int y4,
                                         @InRange(minInt = 0, maxInt = 149) int x5,
                                         @InRange(minInt = 0, maxInt = 149) int y5,
                                         @InRange(minInt = 0, maxInt = 255) int green1,
                                         @InRange(minInt = 0, maxInt = 255) int blue1,
                                         @InRange(minInt = 0, maxInt = 255) int red1,
                                         @InRange(minInt = 0, maxInt = 255) int green2,
                                         @InRange(minInt = 0, maxInt = 255) int blue2,
                                         @InRange(minInt = 0, maxInt = 255) int red2,
                                         @InRange(minInt = 0, maxInt = 255) int bggreen,
                                         @InRange(minInt = 0, maxInt = 255) int bgblue,
                                         @InRange(minInt = 0, maxInt = 255) int bgred,
                                         @InRange(minInt = 150, maxInt = 200) int limit) {

        ImageModel image = new ImageModel(limit, limit);

        PaintColor color1 = new PaintColor(red1, green1, blue1);
        PaintColor color2 = new PaintColor(red2,green2,blue2);


        PaintColor bgColor = new PaintColor(bgred,bggreen,bgblue);

        // Fill layer with the background color
        for (int xi = 0; xi < limit; xi++) {
            for (int yi = 0; yi < limit; yi++) {
                image.setPixel(xi,yi,bgColor);
            }
        }

        //Use some different tools
        image.activateBrushTool();
        image.setColor(color1);
        image.onPress(x1, y1, false);
        image.onDrag(x2,y2, false);
        image.onRelease(x2,y2, false);
        image.activateShapeTool();
        image.setColor(color2);
        image.onPress(x3,y3, false);
        image.onDrag(x4,y4, false);
        image.onRelease(x4,y4, false);
        image.activatePencilTool();
        image.onPress(x5,y5, false);
        image.onDrag(x3,y1, false);
        image.onRelease(x3,y1, false);
        image.activateFillTool();
        image.setColor(color1);
        image.onPress(x5,y2, false);
        image.onDrag(x2,y4, false);
        image.onRelease(x2,y4, false);

        //Undoing 4 times should clear everything


        for(int i = 0; i < 4;i++){
            image.undo();
        }

        //Test every pixel and see if it is the background color

        for (int xi = 0; xi < limit; xi++) {
            for (int yi = 0; yi < limit; yi++) {
                assertTrue(image.getActiveLayer().getPixel(xi, yi).equals(bgColor));
            }
        }
    }
}
