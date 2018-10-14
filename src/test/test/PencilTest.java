import model.ImageModel;
import model.PaintLayer;
import model.tools.PencilTool;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.tools.ToolSettings;
import model.pixel.PaintColor;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;

import java.awt.*;


@RunWith(JUnitQuickcheck.class)



public class PencilTest {


    @Property
    public void paintTest(@InRange(minInt=3, maxInt=15) int size, @InRange(minInt=0, maxInt=149) int x,
                          @InRange(minInt=0, maxInt=149) int y, @InRange(minInt=0, maxInt=255) int green,
                          @InRange(minInt=0, maxInt=255) int blue, @InRange(minInt=0, maxInt=255) int red,
                          @InRange(minInt=150, maxInt=200) int limit ) {
        ImageModel im = new ImageModel(limit, limit);
        int alpha = 255;
        PaintColor color = new PaintColor(red, green, blue, alpha);
        PaintColor nocolor = new PaintColor(255, 255,255,255);
        im.activatePencilTool();
        im.setSize(size);
        im.setColor(color);
        im.onPress(x, y);



        for(int xi = 0; xi < limit; xi++){
            for(int yi = 0; yi < limit; yi++) {

                if (Math.sqrt((xi - x) * (xi - x) + (yi - y) * (yi - y)) > size - 0.5){
                    assertTrue(im.getActiveLayer().getPixel(xi, yi).equals(nocolor));
                }
                else {
                    assertTrue(im.getActiveLayer().getPixel(xi, yi).equals(color));
                }
            }
        }


    }


}
