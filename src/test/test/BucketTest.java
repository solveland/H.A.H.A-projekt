import Model.BucketFillTool;
import Model.PaintLayer;
import org.junit.Test;

import java.awt.*;

import static junit.framework.TestCase.assertTrue;

public class BucketTest {

    int sizeX = 600;
    int sizeY = 600;
    PaintLayer layer = new PaintLayer(sizeX,sizeY,0xFFFFFFFF);


    @Test
    public void bucketToolTest() {
        int blue = new Color(0, 0, 255).getRGB();
        int white = new Color(255,255,255).getRGB();
        BucketFillTool bucketFillTool = new BucketFillTool(blue);
        int x = 50;
        int y = 50;
        // Initial color state on canvas
        assertTrue(layer.getPixel(x,y) == white);
        System.out.println("First bucketToolTest, successful");
        // Using bucketTool
        bucketFillTool.updateColor(blue);
        bucketFillTool.onPress(x,y,layer);
        assertTrue(layer.getPixel(x,y) == blue);
        assertTrue(layer.getPixel(x+50,y+50) == blue);
        assertTrue(layer.getPixel(x-50,y-50) == blue);
        System.out.println("Second bucketToolTest, successful");
    }





















}
