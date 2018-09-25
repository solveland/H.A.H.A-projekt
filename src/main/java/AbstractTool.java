public abstract class AbstractTool {

    /* TODO: When we have a broader understanding of what the tools will require we have to move functionality to this class
     */
    abstract void onPress(int x, int y, PaintLayer layer);
    abstract void onDrag(int x, int y, PaintLayer layer);
    abstract void onRelease(int x, int y, PaintLayer layer);

}
