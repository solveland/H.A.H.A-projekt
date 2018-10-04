import Model.PaintLayer;
import Model.PencilTool;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
public class PencilTest {
    PencilTool pt = new PencilTool(10);

    @Test
    public void paintTest(){
        PaintLayer pl = new PaintLayer(50, 50,0xFFFFFFFF);
        assertTrue(pl.getPixel(0, 0) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(2, 2) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(7, 7) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(5, 14) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(14, 5) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(16, 16) == 0xFFFFFFFF);
        pt.updateColor(0xFF000000);
        int x = (int)(Math.random() * 49);
        int y = (int)(Math.random() * 49);
        pt.onPress(x, y, pl);
        if((x - 5 >= 0) && (y - 5 >= 0)) {
            assertTrue(pl.getPixel(x - 5, y - 5) == 0xFF000000);
            assertTrue(pl.getPixel(x - 3, y - 3) == 0xFF000000);
        }
        if((x + 10 <= 49) && (y + 10 <= 49)) {
            assertTrue(pl.getPixel(x + 2, y + 2) == 0xFF000000);
            assertTrue(pl.getPixel(x, y + 9) == 0xFF000000);
            assertTrue(pl.getPixel(x + 9, y) == 0xFF000000);
        }
        if((x + 11 <= 49) && (y + 11 <= 49)) {
            assertTrue(pl.getPixel(x + 10, y + 10) == 0xFFFFFFFF);
            assertTrue(pl.getPixel(49, y) == 0xFFFFFFFF);
            assertTrue(pl.getPixel(x, 49) == 0xFFFFFFFF);
        }
        pt.updateColor(0xFF00FF00);
        pt.onPress(x, y, pl);
        if((x - 5 >= 0) && (y - 5 >= 0)) {
            assertTrue(pl.getPixel(x - 5, y - 5) == 0xFF00FF00);
            assertTrue(pl.getPixel(x - 3, y - 3) == 0xFF00FF00);
        }
        if((x + 10 <= 49) && (y + 10 <= 49)) {
            assertTrue(pl.getPixel(x + 2, y + 2) == 0xFF00FF00);
            assertTrue(pl.getPixel(x, y + 9) == 0xFF00FF00);
            assertTrue(pl.getPixel(x + 9, y) == 0xFF00FF00);
        }
        if((x + 11 <= 49) && (y + 11 <= 49)) {
            assertTrue(pl.getPixel(x + 10, y + 10) == 0xFFFFFFFF);
            assertTrue(pl.getPixel(49, y) == 0xFFFFFFFF);
            assertTrue(pl.getPixel(x, 49) == 0xFFFFFFFF);
        }
        pt.updateSize(5);
        pt.updateColor(0xFF000000);
        pt.onPress(x,y, pl);
        if((x + 10 <= 49) && (y + 10 <= 49)) {
            assertTrue(pl.getPixel(x, y + 9) == 0xFF00FF00);
            assertTrue(pl.getPixel(x + 9, y) == 0xFF00FF00);
        }
        if((x - 5 >= 0) && (y - 5 >= 0) && (x + 5 <= 49) && (y + 5 <= 49)) {
            assertTrue(pl.getPixel(x , y ) == 0xFF000000);
            assertTrue(pl.getPixel(x - 3, y - 3) == 0xFF000000);
            assertTrue(pl.getPixel(x + 3, y + 3) == 0xFF000000);
        }

    }



}
