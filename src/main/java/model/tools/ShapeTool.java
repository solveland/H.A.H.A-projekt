package model.tools;

import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

import java.util.ArrayList;
import java.util.List;

/*
AUTHOR: August Sölveland
RESPONSIBILITY: Tool for drawing a shape defined by different strategies
USED BY:
USES: TriangleStrategy, RectangleStrategy, StraightLineStrategy, EllipseStrategy
 */

/**
 * This tool is used for creating shapes. It uses the strategy pattern to calculate different shapes and then adds it to the image.
 */

public class ShapeTool implements ITool {
    private Point<Integer> startPoint;
    private PaintColor color;
    private IShapeStrategy strategy;
    private int size;


    public ShapeTool() {
        color = new PaintColor(255, 255, 255);
        setEllipseStrategy();
        this.size = 1;

    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.size = ts.getShapeSize();
        this.color = ts.getPaintColor();
        if(ts.getShape() == Shape.TRIANGLE){
            setTriangleStrategy();
        }else if(ts.getShape() == Shape.RECTANGLE){
            setRectangleStrategy();
        }else if (ts.getShape() == Shape.LINE){
            setStraightLineStrategy();
        }else if(ts.getShape() == Shape.ELLIPSE){
            setEllipseStrategy();
        }

    }

    @Override
    public void onDrag(int x, int y, IEditableByTool imageModel) {
        List<Point<Integer>> pointList = strategy.shapeStrategy(startPoint, new Point<>(x, y), this.size);
        removeOutsidePoints(pointList,imageModel);
        addToOverlay(imageModel, pointList);
        imageModel.renderOverlay();
    }

    @Override
    public void onPress(int x, int y, IEditableByTool imageModel) {
        this.startPoint = new Point<>(x, y);
    }

    @Override
    public void onRelease(int x, int y, IEditableByTool imageModel) {
        imageModel.openNewUndoBuffer();
        List<Point<Integer>> pointList = strategy.shapeStrategy(startPoint, new Point<>(x, y), this.size);
        removeOutsidePoints(pointList,imageModel);
        addShapeToImage(imageModel, pointList);
        imageModel.renderOverlay();
    }

    private void addToOverlay(IEditableByTool imageModel, List<Point<Integer>> shape) {
        List<Pixel> arrayList = new ArrayList<>();
        arrayList.clear();
        for (Point<Integer> i : shape) {
            arrayList.add(new Pixel(i.getX(), i.getY(), color));
        }

        imageModel.addToShapeOverlay(arrayList);

    }


    private void addShapeToImage(IEditableByTool image, List<Point<Integer>> shape) {
        //Empty the overlay
        image.addToShapeOverlay(new ArrayList<>());
        for (Point<Integer> i : shape) {
            if (image.existsInUndoBuffer(new Point<>(i.getX(),i.getY()))) {
                continue;
                //Don't need to paint twice in the same spot
            }
            image.addToUndoBuffer(new Pixel(i.getX(), i.getY(), image.getPixelColor(i.getX(), i.getY())));
            image.setPixel(i.getX(), i.getY(), color);
        }
    }

    private void removeOutsidePoints(List<Point<Integer>> pointList, IEditableByTool imageModel){
        pointList.removeIf(p -> (p.getX() < 0 || p.getY() < 0 || p.getX() >= imageModel.getWidth() || p.getY() >= imageModel.getHeight())
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
