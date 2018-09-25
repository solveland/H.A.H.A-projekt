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
    public void drawOnUpdate(PaintLayer layer)
    {
        PixelWriter pw = image.getPixelWriter();
        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++) {
                pw.setArgb(x, y, layer.getPixel(y * layer.getWidth() + x));
            }
    }
}
