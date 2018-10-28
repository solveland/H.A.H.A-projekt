package model.tools;

public interface ITool {
    void onDrag(int x, int y, IEditableByTool imageModel, Boolean altDown);
    void onPress(int x, int y, IEditableByTool imageModel, Boolean altDown);
    void onRelease(int x, int y, IEditableByTool imageModel, Boolean altDown);
    void updateSettings(ToolSettings ts);
}
