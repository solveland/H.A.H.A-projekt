import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
public class PencilTest {
    PencilTool pt = new PencilTool(10);

    @Test
    public void paintTest(){
        PaintLayer pl = new PaintLayer(50, 50);
        assertTrue(pl.getPixel(0, 0) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(2, 2) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(7, 7) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(5, 14) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(14, 5) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(16, 16) == 0xFFFFFFFF);
        pt.updateColor(0xFF000000);
        pt.onPress(5, 5, pl);
        assertTrue(pl.getPixel(0, 0) == 0xFF000000);
        assertTrue(pl.getPixel(2, 2) == 0xFF000000);
        assertTrue(pl.getPixel(7, 7) == 0xFF000000);
        assertTrue(pl.getPixel(5, 14) == 0xFF000000);
        assertTrue(pl.getPixel(14, 5) == 0xFF000000);
        assertTrue(pl.getPixel(16, 16) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(49, 5) == 0xFFFFFFFF);
        pt.updateColor(0xFF00FF00);
        pt.onPress(5, 5, pl);
        assertTrue(pl.getPixel(0, 0) == 0xFF00FF00);
        assertTrue(pl.getPixel(2, 2) == 0xFF00FF00);
        assertTrue(pl.getPixel(7, 7) == 0xFF00FF00);
        assertTrue(pl.getPixel(5, 14) == 0xFF00FF00);
        assertTrue(pl.getPixel(14, 5) == 0xFF00FF00);
        assertTrue(pl.getPixel(16, 16) == 0xFFFFFFFF);
        assertTrue(pl.getPixel(49, 5) == 0xFFFFFFFF);
        pt.updateSize(5);
        pt.updateColor(0xFF000000);
        pt.onPress(5,5, pl);
        assertTrue(pl.getPixel(5, 14) == 0xFF00FF00);
        assertTrue(pl.getPixel(14, 5) == 0xFF00FF00);
        assertTrue(pl.getPixel(5, 5) == 0xFF000000);
        assertTrue(pl.getPixel(4, 4) == 0xFF000000);
        assertTrue(pl.getPixel(6, 6) == 0xFF000000);
        System.out.println("PencilTest, successful");

    }



}
