import Model.ModelObserver;
import Model.PaintLayer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.List;

public class PaintView implements ModelObserver {

    private WritableImage image;

    public PaintView(int sizeX, int sizeY) {
        setSize(sizeX,sizeY);
    }

    public WritableImage getImage() {
        return image;
    }

    public void setSize(int x, int y){
        image = new WritableImage(x, y);
    }

    /*
    // Draw the rendered image on update from model.
     */
    @Override
    public void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, String id)
    {
        if (id.equals("imageUpdate")) {
            PixelWriter pw = image.getPixelWriter();
            for (int x = minX; x < maxX; x++) {
                for (int y = minY; y < maxY; y++) {
                    pw.setArgb(x, y, layer.getPixel(x, y).getValue());
                }
            }
        }
    }

}
