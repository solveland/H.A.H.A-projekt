package model.tools;

import model.*;
import model.pixel.PaintColor;
import model.pixel.Point;

import java.util.LinkedList;
import java.util.Queue;
/**
 * BucketFillTool class for the fill tool feature.
 */
public class BucketFillTool implements ITool {

    private PaintColor newColor;
    private UndoBuffer undoBuffer;


    public void onPress(int x, int y, IModel imageModel) {
       undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
       flood(x, y, imageModel.getActiveLayer());
       imageModel.pushToUndoStack(undoBuffer);
    }

    public void onDrag(int x, int y, IModel imageModel){
        //flood(x, y, imageModel.getActiveLayer());
    }
    public void onRelease(int x, int y, IModel imageModel) {
        //flood(x, y, imageModel.getActiveLayer());
    }

    /**
     * An algorithm for filling an area while checking the color of the edges.
     * @param x an x-coordinate value of clicked pixel
     * @param y an y-coordinate value of clicked pixel
     * @param layer a layer used to render on
     */

    private void flood(int x, int y, PaintLayer layer) {
       PaintColor oldColor = layer.getPixel(x, y);
       Point<Integer> p = new Point<>(x,y);
       Queue<Point<Integer>> Q = new LinkedList<>();
       if (oldColor.equals(newColor)) {return;}
       if (layer.getPixel(x,y) != oldColor) {return;}
       undoBuffer.addPixel(x,y,oldColor);
       layer.setPixel(x,y, newColor);
       ((LinkedList<Point<Integer>>) Q).addLast(p);
       while (!Q.isEmpty()){
           Point<Integer> n = ((LinkedList<Point<Integer>>) Q).pop();
           if (n.getX() > 0 && layer.getPixel(n.getX() -1, n.getY()).equals(oldColor)){
               undoBuffer.addPixel(n.getX()-1,n.getY(),oldColor);
               layer.setPixel(n.getX()-1, n.getY(), newColor);
               if(layer.getPixel(n.getX()-1, n.getY()).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX()-1, n.getY()));
               }
           }
           if (n.getX() < layer.getWidth() -1 && layer.getPixel(n.getX() +1, n.getY()).equals(oldColor)){
               undoBuffer.addPixel(n.getX()+1,n.getY(),oldColor);
               layer.setPixel(n.getX()+1, n.getY(), newColor);
               if(layer.getPixel(n.getX()+1, n.getY()).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX()+1, n.getY()));
               }
           }
           if (n.getY() < layer.getHeight() -1 && layer.getPixel(n.getX() , n.getY()+1).equals(oldColor)){
               undoBuffer.addPixel(n.getX(),n.getY()+1,oldColor);
               layer.setPixel(n.getX(), n.getY()+1, newColor);
               if(layer.getPixel(n.getX(), n.getY()+1).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX(), n.getY()+1));
               }
           }
           if (n.getY() > 0 && layer.getPixel(n.getX() , n.getY()-1).equals(oldColor)){
               undoBuffer.addPixel(n.getX(),n.getY()-1,oldColor);
               layer.setPixel(n.getX(), n.getY()-1, newColor);
               if(layer.getPixel(n.getX(), n.getY()-1).equals(newColor)){
                   ((LinkedList<Point<Integer>>) Q).addLast(new Point(n.getX(), n.getY()-1));
               }
           }
       }
    }


    public void updateSettings(ToolSettings ts){
        this.newColor = ts.getPaintColor();
    }











}
