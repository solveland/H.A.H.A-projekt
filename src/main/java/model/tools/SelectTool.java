package model.tools;

import model.ImageModel;
import model.pixel.Pixel;
import model.pixel.Point;
import model.pixel.PaintColor;

import java.util.List;

public class SelectTool implements ITool{
    private Point<Integer> startPoint;
    private PaintColor paintColor;

    public SelectTool(){
        paintColor = new PaintColor(0,0,0);
    }


    private void selectRectangleArea (Point<Integer> endPoint){
        int startX = startPoint.getX();
        int startY = startPoint.getY();
        int endX = endPoint.getX();
        int endY = endPoint.getY();
        int a;
        int b;

        if(startX < endX){
            a = startX;
        } else {
            a = endX;
        }
        if(startY < endY){
            b = startY;
        } else {
            b = endY;
        }
    }

    public void setStartPoint (Point startPoint){
        this.startPoint = startPoint;
    }

    public void onDrag(int x, int y, IModel imageModel){
        Point<Integer> endPoint = new Point<> (x,y);
        selectRectangleArea(endPoint);
        drawSelectedToolArea(imageModel.getOverlay(),imageModel.getOldOverlay(), endPoint);
    }
    public void onPress(int x, int y, IModel imageModel){
        Point<Integer> startPoint = new Point<>(x,y);
        setStartPoint(startPoint);
    }
    public void onRelease(int x, int y, IModel imageModel){

    }
    public void updateSettings(ToolSettings ts){

    }


    private void drawSelectedToolArea (List<Pixel> arrayList, List<Pixel> oldArrayList, Point<Integer> endPoint){
            oldArrayList.addAll(arrayList);
            arrayList.clear();


            for (int i = startPoint.getX(); i < endPoint.getX(); i++) {
                for (int j = startPoint.getY(); j < endPoint.getY(); j++) {

                    if ((j == startPoint.getY() || j == endPoint.getY() - 1) && (i >= startPoint.getX() && i < endPoint.getX())) {
                        if (i % 5 == 0)
                            arrayList.add(new Pixel(i, j, paintColor));
                    } else if ((i == startPoint.getX() || i == endPoint.getX() - 1) && (j >= startPoint.getY() && j < endPoint.getY())) {
                        if (j % 5 == 0)
                            arrayList.add(new Pixel(i, j, paintColor));
                    }


                }
            }
    }







}
