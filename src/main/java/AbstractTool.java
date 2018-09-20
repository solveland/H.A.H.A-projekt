import javafx.scene.image.WritableImage;

public abstract class AbstractTool {

    abstract void onPress(int x, int y, WritableImage image);
    abstract void onDrag(int x, int y, WritableImage image);
    abstract void onRelease(int x, int y,WritableImage image);

}
