package model.tools;

import model.ImageModel;
import model.UndoBuffer;
import model.pixel.OverlayPixel;
import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

import java.util.List;

/**
 * This tool is used for creating shapes. It uses the strategy pattern to calculate different shapes and then adds it to the image.
 */

public class ShapeTool implements ITool {
    private Point<Integer> startPoint;
    private UndoBuffer undoBuffer;
    private PaintColor color;
    private IShapeStrategy strategy;


    public ShapeTool() {
        color = new PaintColor(255, 255, 255);
        setEllipseStrategy();

    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.color = ts.getPaintColor();
        if(ts.getShape() == Shape.TRIANGLE){
            setTriangleStrategy();
        }else if(ts.getShape() == Shape.RECTANGLE){
            setRectangleStrategy();
        }else if (ts.getShape() == Shape.LINE){
            setStraightLineStrategy();
        }else if(ts.getShape() == Shape.CIRCLE){

        }

    }

    @Override
    public void onDrag(int x, int y, IModel imageModel) {
        List<Point<Integer>> pointList = strategy.shapeStrategy(startPoint, new Point<>(x, y));
        removeOutsidePoints(pointList,imageModel);
        addToOverlay(imageModel.getShapeOverlay(), pointList);
    }

    @Override
    public void onPress(int x, int y, IModel imageModel) {
        this.startPoint = new Point<>(x, y);
    }

    @Override
    public void onRelease(int x, int y, IModel imageModel) {
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        List<Point<Integer>> pointList = strategy.shapeStrategy(startPoint, new Point<>(x, y));
        removeOutsidePoints(pointList,imageModel);
        addShapeToImage(imageModel, pointList);
        imageModel.pushToUndoStack(undoBuffer);
        imageModel.getShapeOverlay().getOldOverlay().addAll(imageModel.getShapeOverlay().getOverlay());
        imageModel.getShapeOverlay().getOverlay().clear();

    }

    private void addToOverlay(OverlayPixel shapeOverlay, List<Point<Integer>> shape) {
        List<Pixel> arrayList = shapeOverlay.getOverlay();
        List<Pixel> oldArrayList = shapeOverlay.getOldOverlay();
        oldArrayList.addAll(arrayList);
        arrayList.clear();
        for (Point<Integer> i : shape) {
            arrayList.add(new Pixel(i.getX(), i.getY(), color));
        }
        shapeOverlay.setChanged(true);

    }


    private void addShapeToImage(IModel image, List<Point<Integer>> shape) {
        image.getShapeOverlay().getOverlay().clear();
        for (Point<Integer> i : shape) {
            if (undoBuffer.contains(i.getX(), i.getY())) {
                continue;
                //Don't need to paint twice in the same spot
            }
            undoBuffer.addPixel(i.getX(), i.getY(), image.getActiveLayer().getPixel(i.getX(), i.getY()));
            image.getActiveLayer().setPixel(i.getX(), i.getY(), color);
        }
    }

    private void removeOutsidePoints(List<Point<Integer>> pointList, IModel imageModel){
        pointList.removeIf(p -> (p.getX() < 0 || p.getY() < 0 || p.getX() >= imageModel.getActiveLayer().getWidth() || p.getY() >= imageModel.getActiveLayer().getHeight())
        );
    }


    private void setStraightLineStrategy() {
        this.strategy = new StraightLineStrategy();
    }

    private void setTriangleStrategy() {
        this.strategy = new TriangleStrategy();
    }

    private void setRectangleStrategy() {
        this.strategy = new RectangleStrategy();
    }

    private void setEllipseStrategy() {
        this.strategy = new EllipseStrategy();
    }

}
