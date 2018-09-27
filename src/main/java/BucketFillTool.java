import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class BucketFillTool extends AbstractTool implements ISizeAndColor {

   private int oldColor,newColor;
   private PaintLayer layer;


   public BucketFillTool(int color, PaintLayer layer){
       this.layer = layer;
       newColor = color;
   }

    @Override
    void onRelease(int xPos, int yPos, PaintLayer layer) {
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

    @Override
    public void updateSizeAndColor(int size, int color){
    this.newColor = color;

    }











}
