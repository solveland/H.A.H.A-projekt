package model.tools;

import model.UndoBuffer;
import model.pixel.DistanceHelper;
import model.pixel.PaintColor;
import model.pixel.Point;

import java.util.Random;

/**
 * This class is an abstraction of a pencil/eraser.
 * It contains the shared logic used for both drawing and erasing pixels on a canvas.
 */
public abstract class AbstractPaintTool implements ITool {
    private int size;
    protected PaintColor color;
    private UndoBuffer undoBuffer;
    private Point<Double> oldPoint;
    private Shape shape = Shape.CIRCLE;
    Random rand;

    public void onPress(int x, int y, IModel imageModel) {
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        imageModel.pushToUndoStack(undoBuffer);
        oldPoint = new Point<>((double) x, (double) y);
        rand = new Random();
        onDrag(x,y,imageModel);

    }

    public void onDrag(int x, int y, IModel imageModel) {
        Point<Double> newPoint = new Point<Double>((double) x, (double) y);
        int minX = Math.max(0, Math.min(x, oldPoint.getX().intValue()) - size);
        int minY = Math.max(0, Math.min(y, oldPoint.getY().intValue()) - size);
        int maxX = Math.min(imageModel.getActiveLayer().getWidth(), Math.max(x, oldPoint.getX().intValue()) + size);
        int maxY = Math.min(imageModel.getActiveLayer().getHeight(), Math.max(y, oldPoint.getY().intValue()) + size);
        for (int xc = minX; xc < maxX; xc++) {
            for (int yc = minY; yc < maxY; yc++) {
                paintPixel(xc,yc,newPoint,imageModel,undoBuffer,oldPoint);
            }
        }
        oldPoint = newPoint;
    }

    void paintPixel(int x, int y,Point<Double> newPoint,IModel imageModel,UndoBuffer undoBuffer,Point<Double> oldPoint){
        if(undoBuffer.contains(x,y)){
            return;
        }
        double dist = DistanceHelper.distToSegmentSquared(oldPoint, newPoint, new Point<>((double) x, (double) y));
        if (dist < (size - 0.5) * (size - 0.5)) {
            undoBuffer.addPixel(x,y,imageModel.getActiveLayer().getPixel(x,y));
            imageModel.getActiveLayer().setPixel(x, y, getPixelColor(dist,imageModel.getActiveLayer().getPixel(x,y)));
        }
    }

    public void onRelease(int x, int y, IModel imageModel) {

    }

    public int getSize(){
        return size;
    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.size = ts.getSize();
        this.color = ts.getPaintColor();
        this.shape = ts.getShape();
    }




    abstract PaintColor getPixelColor(double dist, PaintColor oldColor);


}
