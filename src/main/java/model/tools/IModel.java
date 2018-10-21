package model.tools;

import model.UndoBuffer;
import model.PaintOverlay;
import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

public interface IModel {

    void setPixel(int x, int y, PaintColor color);
    PaintColor getPixelColor(int x, int y);
    PaintOverlay getSelectOverlay();
    PaintOverlay getShapeOverlay();
    void setColor(PaintColor color);
    void renderOverlay();
    void openNewUndoBuffer();
    void addToUndoBuffer(Pixel pixel);
    boolean existsInUndoBuffer(Point<Integer> point);
    int getWidth();
    int getHeight();
    void selectArea(Point<Integer> start, Point<Integer> end);

}
