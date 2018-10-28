import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.ImageModel;
import model.PaintLayer;
import model.pixel.PaintColor;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnitQuickcheck.class)

public class EyedropperTest {

    @Property
    public void colorExtractTest (@InRange(minInt = 0, maxInt = 149) int x,
                                  @InRange(minInt = 0, maxInt = 149) int y,
                                  @InRange(minInt = 0, maxInt = 149) int a,
                                  @InRange(minInt = 0, maxInt = 149) int b,
                                  @InRange(minInt = 0, maxInt = 255) int green,
                                  @InRange(minInt = 0, maxInt = 255) int blue,
                                  @InRange(minInt = 0, maxInt = 255) int red,
                                  @InRange(minInt = 0, maxInt = 255) int alpha,
                                  @InRange(minInt = 150, maxInt = 200) int limit
    ){
        ImageModel image = new ImageModel(limit, limit);

        PaintColor white = new PaintColor(255,255,255, 255);
        PaintColor redColor = new PaintColor(255,0,0,255);
        PaintColor initColor = new PaintColor(red, blue, green, alpha);
        PaintLayer paintLayer = new PaintLayer(limit, limit, new PaintColor(255, 255, 255), "test");
        image.setActiveLayer(paintLayer);

        // white background
        image.activateFillTool();
        image.setColor(white);
        image.onPress(x,y, );

        // draw two pixels with different color. A red pixel and a initcolored pixel.
        image.activatePencilTool();
        image.setSize(1);
        image.setColor(redColor);
        image.onPress(a,b, );
        image.setColor(initColor);
        image.onPress(x,y, );

        // use eyedropper tool on these pixels and check if current color has changed to the color of the pixels.
        image.activateEyedropperTool();
        image.onRelease(a,b, );
        assertEquals(image.getActiveLayer().getPixel(a,b), image.getTs().getPaintColor());
        image.onRelease(x,y, );
        assertEquals(image.getActiveLayer().getPixel(x,y), image.getTs().getPaintColor());
    }
}
