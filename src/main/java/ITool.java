public interface ITool {
    void onDrag(int x, int y, PaintLayer layer);
    void onPress(int x, int y, PaintLayer layer);
    void onRelease(int x, int y, PaintLayer layer);


}
