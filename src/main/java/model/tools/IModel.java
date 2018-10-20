package model.tools;

import model.PaintLayer;
import model.UndoBuffer;
import model.PaintOverlay;
import model.pixel.PaintColor;

public interface IModel {

    void setPixel(int x, int y, PaintColor color);
    PaintColor getPixelColor(int x, int y);
    void pushToUndoStack(UndoBuffer undoBuffer);
    PaintOverlay getSelectOverlay();
    PaintOverlay getShapeOverlay();
    PaintLayer getActiveLayer();
    void setColor(PaintColor color);
    void renderOverlay();

}
