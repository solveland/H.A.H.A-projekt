import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class PaintView implements ModelObserver {

    private WritableImage image;

    public PaintView(int sizeX, int sizeY) {
        image = new WritableImage(sizeX, sizeY);
    }

    public WritableImage getImage() {
        return image;
    }

    @Override
    public void drawOnUpdate(PaintLayer layer,int minX,int maxX,int minY,int maxY)
    {
        PixelWriter pw = image.getPixelWriter();
        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++) {
                pw.setArgb(x, y, layer.getPixel(x,y));
            }
    }
}
