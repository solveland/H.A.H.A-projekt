import javafx.scene.image.WritableImage;

public class View {
    private WritableImage image;

    public View(int sizeX, int sizeY) {
        image = new WritableImage(sizeX, sizeY);
    }
}
