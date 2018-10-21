package model.tools;

import model.UndoBuffer;
import model.pixel.DistanceHelper;
import model.pixel.PaintColor;
import model.pixel.Point;

import java.util.HashMap;

public class BrushTool extends AbstractPaintTool {

    private HashMap<Point<Integer>,Double> closestDistMap;
    private HashMap<Point<Integer>,PaintColor> origColorMap;
    private double hardness = 0.4;

    public BrushTool() {
        closestDistMap = new HashMap<>();
        origColorMap = new HashMap<>();
    }

    @Override
    void paintPixel(int x, int y,Point<Double> newPoint,IModel imageModel,UndoBuffer undoBuffer,Point<Double> oldPoint){
        Point<Integer> pos = new Point<>(x,y);
        double dist = DistanceHelper.distToSegmentSquared(oldPoint, newPoint, new Point<>((double) x, (double) y));
        if (dist < (getSize() - 0.5) * (getSize() - 0.5)) {
            if(undoBuffer.contains(x,y)){
                if (closestDistMap.get(pos) <= dist){
                    return;
                }
            } else {
                undoBuffer.addPixel(x, y, imageModel.getActiveLayer().getPixel(x, y));
                origColorMap.put(pos,imageModel.getActiveLayer().getPixel(x, y));
                imageModel.getActiveLayer().setPixel(x, y, getPixelColor(dist,imageModel.getActiveLayer().getPixel(x,y)));
                closestDistMap.put(pos,dist);
                return;
            }
            imageModel.getActiveLayer().setPixel(x, y, getPixelColor(dist,origColorMap.get(pos)));
            closestDistMap.replace(pos,dist);
        }
    }

    @Override
    public void onPress(int x, int y, IModel imageModel) {
        closestDistMap.clear();
        origColorMap.clear();
        super.onPress(x,y,imageModel);
    }


    @Override
    PaintColor getPixelColor(double dist, PaintColor oldColor) {
        return PaintColor.alphaBlend(new PaintColor(color.getRed(),color.getGreen(),color.getBlue(),(int)(Math.min(((255-((Math.sqrt(dist)/getSize())*255))/hardness),255)* color.getAlphaRatio())),oldColor);
    }
}
