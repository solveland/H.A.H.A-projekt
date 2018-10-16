package model.tools;

import model.ImageModel;
import model.UndoBuffer;
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
    private PaintColor color = new PaintColor(255, 255, 255);
    private IShapeStrategy strategy;


    public ShapeTool(){
        setTriangleStrategy();
    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.color = ts.getPaintColor();

    }

    @Override
    public void onDrag(int x, int y, IModel imageModel) {
        addToOverlay(imageModel.getOverlay(), imageModel.getOldOverlay(), strategy.shapeStrategy(startPoint, new Point<>(x, y)));

    }

    @Override
    public void onPress(int x, int y, IModel imageModel) {
        this.startPoint = new Point<>(x, y);
    }

    @Override
    public void onRelease(int x, int y, IModel imageModel) {
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        addShapeToImage(imageModel, strategy.shapeStrategy(startPoint, new Point<>(x, y)));
        imageModel.pushToUndoStack(undoBuffer);

    }
    private void addToOverlay(List<Pixel> arrayList, List<Pixel> oldArrayList, List<Point<Integer>> shape){
        oldArrayList.addAll(arrayList);
        arrayList.clear();
        for(Point<Integer> i : shape){
            arrayList.add(new Pixel(i.getX(), i.getY(), color));
        }

    }



    private void addShapeToImage(IModel image, List<Point<Integer>> shape){
        image.getOverlay().clear();
        for(Point<Integer> i : shape){
            undoBuffer.addPixel(i.getX(), i.getY(), image.getActiveLayer().getPixel(i.getX(), i.getY()));
            image.getActiveLayer().setPixel(i.getX(), i.getY(), color);
        }
    }


    public void setStraightLineStrategy(){
        this.strategy = new StraightLineStrategy();
    }
    public void setTriangleStrategy(){this.strategy = new TriangleStrategy(); }
    public void setRectangleStrategy(){this.strategy = new RectangleStrategy(); }



}
