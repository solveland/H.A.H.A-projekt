public class EraserTool extends AbstractTool implements  ISizeAndColor {
    private int size;
    private int[] brushBuffer;

    public EraserTool(int size){
        this.size = size;
    }

    @Override
    void onPress(int x, int y, PaintLayer layer) {

    }

    @Override
    void onDrag(int x, int y, PaintLayer layer) {

    }

    @Override
    void onRelease(int x, int y, PaintLayer layer) {

    }

    @Override
    public void updateSizeAndColor(int size, int color) {

    }
}