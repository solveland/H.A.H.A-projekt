import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BucketFillTool extends AbstractTool implements ISizeAndColor {

   private int oldColor,newColor;
   //private WritableImage image;
   private PaintLayer layer;
   private Queue q;


   public BucketFillTool(int color, PaintLayer layer){
       this.layer = layer;
       newColor = color;
   }

    @Override
    void onRelease(int xPos, int yPos, PaintLayer layer) {
       // oldColor= image.getPixelReader().getArgb(xPos,yPos);
       // oldColor= layer.getPixel(yPos * layer.getWidth() + xPos);
      // flood(xPos,yPos);
   }

    @Override
    void onDrag(int xPos, int yPos, PaintLayer layer) {

    }

    @Override
    void onPress(int xPos, int yPos, PaintLayer layer) {
        oldColor= layer.getPixel(xPos, yPos);
        flood(xPos,yPos);
    }

    public void flood(int x, int y) {
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



/*
    public void flood(int x, int y) {
       Point p = new Point(x,y);
       Queue<Point> Q = new LinkedList<Point>();
        List<Point> list = new ArrayList<Point>();
       if (oldColor == newColor) return;
       if (layer.getPixel(x,y) != oldColor) return;
       Q.add(p);
       for (Point N : Q){
           Point w = N;
           Point e = N;
           while( layer.getPixel(w.x-1,w.y) != oldColor){
               double a = w.x - 1;
              w.setLocation(a , w.y);
              Q.add(w);
           }
           while( layer.getPixel(e.x+1,e.y) != oldColor){
               double a = e.x + 1;
               w.setLocation(a , e.y);
               Q.add(e);
           }
           for (Point n : Q ){
               layer.setPixel(n.x, n.y, newColor);
               if (layer.getPixel(n.x, n.y+1) == oldColor) {
                   Q.add(n);
               }
               if (layer.getPixel(n.x, n.y-1) == oldColor) {
                   Q.add(n);
               }
           }
       }
        return;
    }

*/



/*
    public void flood(int x, int y){
       // check bounds
        if (x<0) return;
        if (y<0) return;
        if (x >= layer.getWidth()) return;
        if (y >= layer.getHeight()) return;

        // check old color
        if (layer.getPixel(x,y) != oldColor) return;

        // switch to new color
        layer.setPixel(x,y, newColor);
        // flood surrounding pixels

        flood(x-1, y);
        flood(x+1,y);
        flood(x,y-1);
        flood(x,y+1);

    }
*/

    @Override
    public void updateSizeAndColor(int size, int color){
    this.newColor = color;

    }











}
