package model.tools;

import model.UndoBuffer;
import model.pixel.DistanceHelper;
import model.pixel.PaintColor;
import model.pixel.Point;

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

    public void onPress(int x, int y, IModel imageModel) {
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        imageModel.pushToUndoStack(undoBuffer);
        oldPoint = new Point<>((double) x, (double) y);
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
                if(undoBuffer.contains(xc,yc)){
                    continue;
                }
                double dist = DistanceHelper.distToSegmentSquared(oldPoint, newPoint, new Point<>((double) xc, (double) yc));
                if (dist < size * size - 0.5) {
                    undoBuffer.addPixel(xc,yc,imageModel.getActiveLayer().getPixel(xc,yc));
                    imageModel.getActiveLayer().setPixel(xc, yc, getPixelColor(dist,imageModel.getActiveLayer().getPixel(xc,yc)));
                }
            }
        }
        oldPoint = newPoint;
    }

    public void onRelease(int x, int y, IModel imageModel) {

    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.size = ts.getSize();
        this.color = ts.getPaintColor();
        this.shape = ts.getShape();
    }




    abstract PaintColor getPixelColor(double dist, PaintColor oldColor);


}
