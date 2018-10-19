package model.tools;

import model.ImageModel;
import model.PaintLayer;
import model.pixel.Pixel;
import model.pixel.Point;
import model.pixel.PaintColor;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectTool class for the select feature.
 */

public class SelectTool implements ITool{
    private Point<Integer> startPoint;
    private PaintColor paintColor;


    public SelectTool(){
        paintColor = new PaintColor(0,0,0);
    }


    public void setStartPoint (Point startPoint){
        this.startPoint = startPoint;
    }


    public void onDrag(int x, int y, IModel imageModel){
        Point<Integer> endPoint = new Point<> (x,y);
        drawSelectedToolArea(imageModel.getOverlay(),imageModel.getOldOverlay(), endPoint);
    }
    public void onPress(int x, int y, IModel imageModel){
        Point<Integer> startPoint = new Point<>(x,y);
        setStartPoint(startPoint);
    }

    public void onRelease(int x, int y, IModel imageModel){
        Point endPoint = new Point (x,y);
        imageModel.getActiveLayer().selectArea(startPoint, endPoint);
    }
    public void updateSettings(ToolSettings ts){

    }


    /**
     * This method checks how the user creates the rectangle and adds the pixels to a list. It checks the direction of the drag.
     * @param arrayList a list that contains all pixels when drawing the rectangle for the selected area.
     * @param oldArrayList a copy of arraylist with the same pixels.
     * @param endPoint a point used when creating a rectangle for the selected area.
     */
    private void drawSelectedToolArea (List<Pixel> arrayList, List<Pixel> oldArrayList, Point<Integer> endPoint){
        oldArrayList.addAll(arrayList);
        arrayList.clear();

        int minX = (startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int maxX = (startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX();
        int minY = (startPoint.getY() < endPoint.getY()) ? startPoint.getY() : endPoint.getY();
        int maxY = (startPoint.getY() > endPoint.getY()) ? startPoint.getY() : endPoint.getY();

        for(int i = minX; i <= maxX; i++){
            if (i % 5 == 0){
                arrayList.add(new Pixel(i, minY, paintColor));
                arrayList.add(new Pixel(i, maxY, paintColor));
            }

        }
        for(int j = minY + 1; j < maxY; j++){
            if (j % 5 == 0) {
                arrayList.add(new Pixel(minX, j, paintColor));
                arrayList.add(new Pixel(maxX, j, paintColor));
            }
        }
    }
}
