package model.tools;

import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

import java.util.LinkedList;
import java.util.Queue;
/**
 * BucketFillTool class for the fill tool feature.
 */
public class BucketFillTool implements ITool {

    private PaintColor newColor;


    public void onPress(int x, int y, IModel imageModel) {
       imageModel.openNewUndoBuffer();
       flood(x, y, imageModel);
    }

    public void onDrag(int x, int y, IModel imageModel){
        //flood(x, y, imageModel.getActiveLayer());
    }
    public void onRelease(int x, int y, IModel imageModel) {
        //flood(x, y, imageModel.getActiveLayer());
    }

    /**
     * An algorithm for filling an area with the same color while checking the color of the edges.
     * @param x an x-coordinate value of clicked pixel
     * @param y an y-coordinate value of clicked pixel
     * @param imageModel an interface to the image model
     */

    private void flood(int x, int y, IModel imageModel) {
       PaintColor oldColor = imageModel.getPixelColor(x, y);
       Point<Integer> p = new Point<>(x,y);
       Queue<Point<Integer>> Q = new LinkedList<>();
       if (oldColor.equals(newColor)) {return;}
       if (imageModel.getPixelColor(x,y) != oldColor) {return;}
       imageModel.addToUndoBuffer(new Pixel(x,y,oldColor));
        imageModel.setPixel(x,y, newColor);
       ((LinkedList<Point<Integer>>) Q).addLast(p);
       while (!Q.isEmpty()){
           Point<Integer> n = ((LinkedList<Point<Integer>>) Q).pop();
           if (n.getX() > 0 && imageModel.getPixelColor(n.getX() -1, n.getY()).equals(oldColor)){
               imageModel.addToUndoBuffer(new Pixel(n.getX()-1,n.getY(),oldColor));
               imageModel.setPixel(n.getX()-1, n.getY(), newColor);
               if(imageModel.getPixelColor(n.getX()-1, n.getY()).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX()-1, n.getY()));
               }
           }
           if (n.getX() < imageModel.getWidth() -1 && imageModel.getPixelColor(n.getX() +1, n.getY()).equals(oldColor)){
               imageModel.addToUndoBuffer(new Pixel(n.getX()+1,n.getY(),oldColor));
               imageModel.setPixel(n.getX()+1, n.getY(), newColor);
               if(imageModel.getPixelColor(n.getX()+1, n.getY()).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX()+1, n.getY()));
               }
           }
           if (n.getY() < imageModel.getHeight() -1 && imageModel.getPixelColor(n.getX() , n.getY()+1).equals(oldColor)){
               imageModel.addToUndoBuffer(new Pixel(n.getX(),n.getY()+1,oldColor));
               imageModel.setPixel(n.getX(), n.getY()+1, newColor);
               if(imageModel.getPixelColor(n.getX(), n.getY()+1).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX(), n.getY()+1));
               }
           }
           if (n.getY() > 0 && imageModel.getPixelColor(n.getX() , n.getY()-1).equals(oldColor)){
               imageModel.addToUndoBuffer(new Pixel(n.getX(),n.getY()-1,oldColor));
               imageModel.setPixel(n.getX(), n.getY()-1, newColor);
               if(imageModel.getPixelColor(n.getX(), n.getY()-1).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX(), n.getY()-1));
               }
           }
       }
    }

    public void updateSettings(ToolSettings ts){
        this.newColor = ts.getPaintColor();
    }

}
