package model.tools;

public interface ITool {
    void onDrag(int x, int y, IEditableByTool imageModel);
    void onPress(int x, int y, IEditableByTool imageModel);
    void onRelease(int x, int y, IEditableByTool imageModel);
    void updateSettings(ToolSettings ts);
}
