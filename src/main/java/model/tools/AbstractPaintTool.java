package model.tools;

import model.pixel.Pixel;
import utilities.DistanceHelper;
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
    private Point<Double> oldPoint;
    private Shape shape = Shape.CIRCLE;
    Random rand;

    public void onPress(int x, int y, IModel imageModel) {
        imageModel.openNewUndoBuffer();
        oldPoint = new Point<>((double) x, (double) y);
        rand = new Random();
        onDrag(x,y,imageModel);

    }

    public void onDrag(int x, int y, IModel imageModel) {
        Point<Double> newPoint = new Point<Double>((double) x, (double) y);
        int minX = Math.max(0, Math.min(x, oldPoint.getX().intValue()) - size);
        int minY = Math.max(0, Math.min(y, oldPoint.getY().intValue()) - size);
        int maxX = Math.min(imageModel.getWidth(), Math.max(x, oldPoint.getX().intValue()) + size);
        int maxY = Math.min(imageModel.getHeight(), Math.max(y, oldPoint.getY().intValue()) + size);
        for (int xc = minX; xc < maxX; xc++) {
            for (int yc = minY; yc < maxY; yc++) {
                paintPixel(xc,yc,newPoint,imageModel,oldPoint);
            }
        }
        oldPoint = newPoint;
    }

    void paintPixel(int x, int y,Point<Double> newPoint,IModel imageModel,Point<Double> oldPoint){
        if(imageModel.existsInUndoBuffer(new Point<>(x,y))){
            return;
        }
        double dist = DistanceHelper.distToSegmentSquared(oldPoint, newPoint, new Point<>((double) x, (double) y));
        if (dist < (size - 0.5) * (size - 0.5)) {
            imageModel.addToUndoBuffer(new Pixel(x,y,imageModel.getPixelColor(x,y)));
            imageModel.setPixel(x, y, getPixelColor(dist,imageModel.getPixelColor(x,y)));
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
