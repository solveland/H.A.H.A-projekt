package model.tools;

import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

import java.util.List;

public interface IEditableByTool {

    void setPixel(int x, int y, PaintColor color);
    PaintColor getPixelColor(int x, int y);
    void addToShapeOverlay(List<Pixel> pixelList);
    void addToSelectOverlay(List<Pixel> pixelList);
    void setColor(PaintColor color);
    void renderOverlay();
    void openNewUndoBuffer();
    void addToUndoBuffer(Pixel pixel);
    boolean existsInUndoBuffer(Point<Integer> point);
    int getWidth();
    int getHeight();
    void selectArea(Point<Integer> start, Point<Integer> end);
    void setZoomScale(double zoomValue);
    double getZoomScale();

}
