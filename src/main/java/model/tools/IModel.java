package model.tools;

import model.PaintLayer;
import model.UndoBuffer;
import model.pixel.OverlayPixel;
import model.pixel.PaintColor;
import model.pixel.Pixel;

import java.util.List;

public interface IModel {

    void setPixel(int x, int y, PaintColor color);
    PaintColor getPixelColor(int x, int y);
    void pushToUndoStack(UndoBuffer undoBuffer);
    OverlayPixel getSelectOverlay();
    OverlayPixel getShapeOverlay();
    PaintLayer getActiveLayer();
    void setColor(PaintColor color);

}
