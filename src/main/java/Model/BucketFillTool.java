package Model;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class BucketFillTool implements IColor, ITool {

    private int newColor;

   public BucketFillTool(int color){
       newColor = color;
   }

    public void onPress(int x, int y, ImageModel imageModel) {
        flood(x, y, imageModel.getActiveLayer());
    }
    public void onDrag(int x, int y, ImageModel imageModel){
        flood(x, y, imageModel.getActiveLayer());
    }
    public void onRelease(int x, int y, ImageModel imageModel){
        flood(x, y, imageModel.getActiveLayer());
    }



    private void flood(int x, int y, PaintLayer layer) {

       int oldColor = layer.getPixel(x, y);
       Point p = new Point(x,y);
       Queue<Point> Q = new LinkedList<Point>();
       if (oldColor == newColor) return;
       if (layer.getPixel(x,y) != oldColor) return;
       layer.setPixel(x,y, newColor);
       ((LinkedList<Point>) Q).addLast(p);
       while (!Q.isEmpty()){
           Point n = ((LinkedList<Point>) Q).pop();
           if (n.x > 0 && layer.getPixel(n.x -1, n.y) == oldColor){
               layer.setPixel(n.x-1, n.y, newColor);
               ((LinkedList<Point>) Q).addLast(new Point(n.x-1, n.y));
           }
           if (n.x < layer.getWidth() -1 && layer.getPixel(n.x +1, n.y) == oldColor){
               layer.setPixel(n.x+1, n.y, newColor);
               ((LinkedList<Point>) Q).addLast(new Point(n.x+1, n.y));
           }
           if (n.y < layer.getHeight() -1 && layer.getPixel(n.x , n.y+1) == oldColor){
               layer.setPixel(n.x, n.y+1, newColor);
               ((LinkedList<Point>) Q).addLast(new Point(n.x, n.y+1));
           }
           if (n.y > 0 && layer.getPixel(n.x , n.y-1) == oldColor){
               layer.setPixel(n.x, n.y-1, newColor);
               ((LinkedList<Point>) Q).addLast(new Point(n.x, n.y-1));
           }

       }

    }

    public void updateColor(int color){
        this.newColor = color;
    }











}
