package model.tools;

import model.ImageModel;
import model.UndoBuffer;
import model.utils.PaintColor;
import model.utils.Pixel;
import model.utils.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * This tool is used for creating shapes. It uses the strategy pattern to calculate different shapes and then adds it to the image.
 */

public class ShapeTool implements ITool {
    Point startPoint;
    private UndoBuffer undoBuffer;
    PaintColor color = new PaintColor(255, 255, 255);
    IShapeStrategy strategy;


    public ShapeTool(){
        setStraightLineStrategy();
    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.color = ts.getPaintColor();

    }

    @Override
    public void onDrag(int x, int y, ImageModel imageModel) {
        addToOverlay(imageModel.overlay, imageModel.oldOverlay, strategy.lineStrategy(startPoint, new Point(x, y)));

    }

    @Override
    public void onPress(int x, int y, ImageModel imageModel) {
        this.startPoint = new Point(x, y);
    }

    @Override
    public void onRelease(int x, int y, ImageModel imageModel) {
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        addShapeToImage(imageModel, strategy.lineStrategy(startPoint, new Point(x, y)));
        imageModel.pushToUndoStack(undoBuffer);

    }
    private void addToOverlay(List<Pixel> arrayList, List<Pixel> oldArrayList, List<Point> shape){
        oldArrayList.addAll(arrayList);
        arrayList.clear();
        for(Point i : shape){
            arrayList.add(new Pixel(i.getX(), i.getY(), color));
        }

    }



    private void addShapeToImage(ImageModel image, List<Point> shape){
        image.overlay.clear();
        for(Point i : shape){
            undoBuffer.addPixel(i.getX(), i.getY(), image.getActiveLayer().getPixel(i.getX(), i.getY()));
            image.getActiveLayer().setPixel(i.getX(), i.getY(), color);
        }
    }


    public void setStraightLineStrategy(){
        this.strategy = new StraightLineStrategy();
    }



}
