package model.tools;

import model.PaintLayer;
import model.UndoBuffer;
import model.pixel.PaintColor;
import model.pixel.Pixel;

import java.util.List;

public interface IModel {

    void setPixel(int x, int y, PaintColor color);
    PaintColor getPixelColor(int x, int y);
    void pushToUndoStack(UndoBuffer undoBuffer);
    List<Pixel> getOverlay();
    List<Pixel> getOldOverlay();
    PaintLayer getActiveLayer();

}
