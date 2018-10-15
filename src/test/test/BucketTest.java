import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.ImageModel;
import model.PaintLayer;
import model.tools.BucketFillTool;
import model.utils.PaintColor;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;



@RunWith(JUnitQuickcheck.class)

public class BucketTest {

    @Property

    public void bucketToolTest(@InRange(minInt=0, maxInt=149) int x,
                               @InRange(minInt=0, maxInt=149) int y,
                               @InRange(minInt=0, maxInt=255) int green,
                               @InRange(minInt=0, maxInt=255) int blue,
                               @InRange(minInt=0, maxInt=255) int red,
                               @InRange(minInt=150, maxInt=200) int limit ) {

        ImageModel image = new ImageModel(limit, limit);
        PaintColor white = new PaintColor(255,255,255);
        PaintColor black = new PaintColor(0,0,0);

        PaintColor initColor = new PaintColor(red, blue, green);
        PaintLayer paintLayer = new PaintLayer(limit, limit, new PaintColor(255,255,255), "test");

        image.setActiveLayer(paintLayer);
        image.activateFillTool();
        image.setColor(initColor);
        image.onPress(x,y);

        //Test - FillTool on a canvas with one color.
        for(int xi = 0; xi < limit; xi++){
            for(int yi = 0; yi < limit; yi++) {

                    assertTrue(image.getActiveLayer().getPixel(xi,yi).equals(initColor));

            }
        }

        //Test - FillTool on a black pixel with white background.
        image.setColor(white);
        image.onPress(x,y);
        image.getActiveLayer().setPixel(x,y, black);
        image.onPress(x,y);

        for(int xi = 0; xi < limit; xi++){
            for(int yi = 0; yi < limit; yi++) {

                    assertTrue(image.getActiveLayer().getPixel(x,y).equals(black));
                    assertTrue(image.getActiveLayer().getPixel(x+1,y+1).equals(white));
            }
        }








    }












        /*
        int blue = new PaintColor(0, 0, 255);
        int white = new PaintColor(255,255,255);
        BucketFillTool bucketFillTool = new BucketFillTool(blue);
        image.setActiveLayer(layer);
        int x = 50;
        int y = 50;
        // Initial color state on canvas
        assertTrue(layer.getPixel(x,y) == white);
        System.out.println("First bucketToolTest, successful");
        // Using bucketTool
        bucketFillTool.updateColor(blue);
        bucketFillTool.onPress(x,y,image);
        assertTrue(layer.getPixel(x,y) == blue);
        assertTrue(layer.getPixel(x+50,y+50) == blue);
        assertTrue(layer.getPixel(x-50,y-50) == blue);
        System.out.println("Second bucketToolTest, successful");
        */
    }






















