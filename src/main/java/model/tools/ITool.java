package model;

public interface ITool {
    void onDrag(int x, int y, ImageModel imageModel);
    void onPress(int x, int y, ImageModel imageModel);
    void onRelease(int x, int y, ImageModel imageModel);
}
