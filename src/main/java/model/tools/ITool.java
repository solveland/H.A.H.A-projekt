package model.tools;


public interface ITool {
    void onDrag(int x, int y, IModel imageModel);
    void onPress(int x, int y, IModel imageModel);
    void onRelease(int x, int y, IModel imageModel);
    void updateSettings(ToolSettings ts);
}
