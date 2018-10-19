package model.tools;

import model.ImageModel;
import model.PaintLayer;
import model.pixel.Pixel;
import model.pixel.Point;
import model.pixel.PaintColor;

import java.util.ArrayList;
import java.util.List;

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
        drawSelectedToolArea(imageModel.getOverlay(),imageModel.getOldOverlay(), endPoint,imageModel.getActiveLayer().getWidth(),imageModel.getActiveLayer().getHeight());
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


    private void drawSelectedToolArea (List<Pixel> arrayList, List<Pixel> oldArrayList, Point<Integer> endPoint, int maxWidth, int maxHeight){
        oldArrayList.addAll(arrayList);
        arrayList.clear();

        int minX = Math.max((startPoint.getX() < endPoint.getX()) ? startPoint.getX() : endPoint.getX(),0);
        int maxX = Math.min((startPoint.getX() > endPoint.getX()) ? startPoint.getX() : endPoint.getX(),maxWidth - 1);
        int minY = Math.max((startPoint.getY() < endPoint.getY()) ? startPoint.getY() : endPoint.getY(),0);
        int maxY = Math.min((startPoint.getY() > endPoint.getY()) ? startPoint.getY() : endPoint.getY(),maxHeight - 1);

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
