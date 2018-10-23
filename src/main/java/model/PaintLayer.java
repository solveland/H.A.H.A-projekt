package model;

import model.pixel.PaintColor;
import model.pixel.Point;

/*
AUTHOR: Anthony Tao, Henrik Tao, Hampus Ekberg, August Sölveland
RESPONSIBILITY: Defines a layer of an image
USED BY: ImageModel, ImageSaveLoad, LayerItem, controller.LayerListController, LayerObserver, controller.PaintController, PaintView
USES: PaintColor, Point
 */

public class PaintLayer {

    private PaintColor[] pixelArray;
    private int height;
    private int width;
    private int changedMinX, changedMinY, changedMaxX, changedMaxY;
    private boolean changed = false;
    private boolean visible;
    private PaintColor bgColor;
    private String name;

    private Point<Integer> selectedStartPoint;
    private Point<Integer> selectedEndPoint;



    private Boolean selection;

    public PaintLayer(int sizeX, int sizeY, PaintColor bgColor, String name) {
        height = sizeY;
        width = sizeX;
        pixelArray = new PaintColor[sizeX * sizeY];
        this.bgColor = bgColor;
        visible = true;
        this.name = name;

        for (int i = 0; i < sizeX * sizeY; i++) {
            pixelArray[i] = bgColor;
        }
        changedMaxX = width;
        changedMinX = 0;
        changedMinY = 0;
        changedMaxY = height;
        changed = true;

        selection = false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixel(int x, int y, PaintColor rgb) {
        if(selection){
            int minX = (selectedStartPoint.getX() < selectedEndPoint.getX()) ? selectedStartPoint.getX() : selectedEndPoint.getX();
            int maxX = (selectedStartPoint.getX() > selectedEndPoint.getX()) ? selectedStartPoint.getX() : selectedEndPoint.getX();
            int minY = (selectedStartPoint.getY() < selectedEndPoint.getY()) ? selectedStartPoint.getY() : selectedEndPoint.getY();
            int maxY = (selectedStartPoint.getY() > selectedEndPoint.getY()) ? selectedStartPoint.getY() : selectedEndPoint.getY();

            if ((x <= minX || x >= maxX) || (y <= minY || y >= maxY) ){
                return;
            }
        }

        pixelArray[y * width + x] = rgb;
        if (!changed) {
            changedMinX = x;
            changedMaxX = x + 1;
            changedMinY = y;
            changedMaxY = y + 1;
            changed = true;
        } else {
            if (x < changedMinX) {
                changedMinX = x;
            } else if (x >= changedMaxX) {
                changedMaxX = x + 1;
            }

            if (y < changedMinY) {
                changedMinY = y;
            } else if (y >= changedMaxY) {
                changedMaxY = y + 1;
            }
        }
    }

    public Point<Integer> getSelectedStartPoint() {
        return selectedStartPoint;
    }

    public Point<Integer> getSelectedEndPoint() {
        return selectedEndPoint;
    }

    public PaintColor getPixel(int x, int y) {
        return pixelArray[y * width + x];
    }

    // Temporary clear
    public void clearLayer() {
        for (int i = 0; i < height * width; i++) {
            pixelArray[i] = bgColor;
        }
        changed = true;
        changedMinX = 0;
        changedMinY = 0;
        changedMaxY = height;
        changedMaxX = width;
    }

    public void resetChangeTracker() {
        changedMinX = 0;
        changedMaxX = 0;
        changedMinY = 0;
        changedMaxY = 0;
        changed = false;
    }

    public void selectArea(Point<Integer> startPoint, Point<Integer> endPoint){
        selectedStartPoint = startPoint;
        selectedEndPoint = endPoint;
        selection = true;
    }

    public Boolean hasSelection() {
        return selection;
    }

    public void setSelection(Boolean selection) {
        this.selection = selection;
    }

    public int getChangedMinX() {
        return changedMinX;
    }

    public int getChangedMinY() {
        return changedMinY;
    }

    public int getChangedMaxX() {
        return changedMaxX;
    }

    public int getChangedMaxY() {
        return changedMaxY;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void toggleVisible() {
        visible = !visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PaintColor getBgColor() {
        return bgColor;
    }
}
