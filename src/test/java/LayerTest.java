import model.ImageModel;
import model.PaintLayer;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import model.pixel.PaintColor;

import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;


@RunWith(JUnitQuickcheck.class)

public class LayerTest {

    /*
        Tests if a brushstroke only paints on the active layer and does not affect any other layer.
     */
    @Property
    public void layerTest (@InRange(minInt=3, maxInt=15) int size, @InRange(minInt=0, maxInt=149) int x,
                           @InRange(minInt=0, maxInt=149) int y, @InRange(minInt=0, maxInt=255) int green,
                           @InRange(minInt=0, maxInt=255) int blue, @InRange(minInt=0, maxInt=254) int red,
                           @InRange(minInt=150, maxInt=200) int limit, @InRange(minInt=2, maxInt = 10) int layerCount) {
        ImageModel im = new ImageModel(limit, limit);
        int alpha = 255;
        PaintColor color = new PaintColor(red, green, blue, alpha);
        PaintColor noColor = new PaintColor(255, 255,255,255);
        im.activatePencilTool();
        im.setSize(size);
        im.setColor(color);
        for (int i = 0; i < layerCount; i++) {
            im.createLayer(noColor, "");
        }
        PaintLayer paintedLayer = im.getActiveLayer();
        im.onPress(x, y, );


        for (PaintLayer l : im.getLayerList()) {
            for (int xi = 0; xi < limit; xi++) {
                for (int yi = 0; yi < limit; yi++) {
                    if (l.equals(paintedLayer)) {
                        if (Math.sqrt((xi - x) * (xi - x) + (yi - y) * (yi - y)) > size - 0.5) {
                            assertTrue(l.getPixel(xi, yi).equals(noColor));
                        } else {
                            assertTrue(l.getPixel(xi, yi).equals(color));
                        }
                    } else {
                        assertTrue(l.getPixel(xi, yi).equals(noColor));
                    }
                }
            }
        }

    }

    /*
        Tests if the image is rendered correctly according to layer order. The test first paints somewhere on the background,
        saves the state, then creates a new layer and paints over the painted area. It then checks if the rendered pixels of
        that area has the newly painted color.
        Lastly, it deletes the new layer and checks if the rendered pixels are same as the first state.
     */
    @Property
    public void renderedPixelsTest(@InRange(minInt=3, maxInt=15) int size, @InRange(minInt=0, maxInt=149) int x,
                          @InRange(minInt=0, maxInt=149) int y, @InRange(minInt=0, maxInt=255) int green,
                          @InRange(minInt=0, maxInt=255) int blue, @InRange(minInt=0, maxInt=255) int red,
                          @InRange(minInt=150, maxInt=200) int limit, @InRange(minInt=0, maxInt=255) int newGreen,
                           @InRange(minInt=0, maxInt=255) int newRed, @InRange(minInt=0, maxInt=255) int newBlue) {
        ImageModel im = new ImageModel(limit, limit);
        int alpha = 255;
        PaintColor color = new PaintColor(red, green, blue, alpha);
        PaintColor newColor = new PaintColor(newRed, newGreen, newBlue, alpha);
        PaintLayer oldState;
        PaintLayer currentState;

        im.activatePencilTool();
        im.setSize(size);
        im.setColor(color);
        im.onPress(x, y, );
        oldState = im.getRenderedImage();
        im.createLayer(PaintColor.blank, "");
        im.setColor(newColor);
        im.onPress(x, y, );
        currentState = im.getRenderedImage();

        for(int xi = 0; xi < limit; xi++){
            for(int yi = 0; yi < limit; yi++) {

                PaintColor renderedPixel = currentState.getPixel(xi, yi);
                PaintColor oldPixel = oldState.getPixel(xi, yi);

                if (Math.sqrt((xi - x) * (xi - x) + (yi - y) * (yi - y)) > size - 0.5){
                    assertTrue(renderedPixel.equals(oldPixel));
                }
                else {
                    assertTrue(renderedPixel.equals(newColor));
                }
            }
        }

        im.deleteActiveLayer();
        currentState = im.getRenderedImage();

        for(int xi = 0; xi < limit; xi++){
            for(int yi = 0; yi < limit; yi++) {

                assertTrue(currentState.getPixel(xi, yi).equals(oldState.getPixel(xi, yi)));
            }
        }
    }

    /*
        Tests moving layer method. Saves the initial layer list state. Moves the topmost to the bottom, and to the top again,
        and then compares the old list with the new list.
     */
    @Property
    public void layerMoveTest(@InRange(minInt=2, maxInt=10) int layerAmount) {
        ImageModel im = new ImageModel(300, 300);

        for (int i = 0; i < layerAmount; i++) {
            im.createLayer(PaintColor.blank, "");
        }

        List<PaintLayer> layerList = im.getLayerList();
        List<PaintLayer> listOldState = new LinkedList<>();
        listOldState.addAll(im.getLayerList());

        im.setActiveLayer(layerList.get(0));
        PaintLayer movingLayer = im.getActiveLayer();

        // Move topmost layer by 1 step every iteration til it reaches the bottom of the layer list.
        for (int i = 1; i < layerList.size(); i++) {
            im.moveLayerIndex(i, movingLayer);
            assertTrue(layerList.indexOf(movingLayer) == i);
        }

        // Move the layer to the top again
        for (int i = layerList.size() - 1; i >= 0; i--) {
            im.moveLayerIndex(i, movingLayer);
            assertTrue(layerList.indexOf(movingLayer) == i);
        }

        assertTrue(layerList.equals(listOldState));
    }

}