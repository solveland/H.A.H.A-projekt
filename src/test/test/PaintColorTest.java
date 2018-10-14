import model.pixel.PaintColor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PaintColorTest {

    @Test
    public void colorTest(){
        assertTrue(new PaintColor(0,0,0).getValue() == 0xFF000000);
        assertTrue(new PaintColor(255,0,0).getValue() == 0xFFFF0000);
        assertTrue(new PaintColor(0,255,0).getValue() == 0xFF00FF00);
        assertTrue(new PaintColor(0,0,255).getValue() == 0xFF0000FF);
        assertTrue(new PaintColor(255,255,255).getValue() == 0xFFFFFFFF);
        assertTrue(new PaintColor(0xFF00FF00).getGreen() == 255);
        assertTrue(new PaintColor(0xFF00FF00).getBlue() == 0);
        assertTrue(new PaintColor(123,26,78,10).getValue() == 0x0A7B1A4E);
        assertTrue(new PaintColor(123,26,78,10).getAlpha() == 10);
        assertTrue(new PaintColor(51231,12351,23123,123123).getBlue() == 255);
        assertTrue(new PaintColor(51231,12351,23123,123123).getRed() == 255);
        assertTrue(new PaintColor(51231,12351,23123,123123).getGreen() == 255);
        assertTrue(new PaintColor(51231,12351,23123,123123).getAlpha() == 255);
        assertTrue(new PaintColor(213,51,3,232).getAlpha() == 232);
        assertTrue(new PaintColor(0.5,0.7,0.2,0.4).getRed() == 127);
    }

}
