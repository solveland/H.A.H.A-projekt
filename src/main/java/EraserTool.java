import javafx.scene.image.WritableImage;

public class EraserTool extends AbstractTool implements  ISizeAndColor {
    private int size;
    private int[] brushBuffer;

    public EraserTool(int size){
        this.size = size;
    }

    @Override
    void onPress(int x, int y, WritableImage image) {

    }

    @Override
    void onDrag(int x, int y, WritableImage image) {

    }

    @Override
    void onRelease(int x, int y, WritableImage image) {

    }

    @Override
    public void updateSizeAndColor(int size, int color) {

    }
}