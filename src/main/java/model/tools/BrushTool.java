package model.tools;

import model.pixel.Pixel;
import model.utilities.DistanceHelper;
import model.pixel.PaintColor;
import model.pixel.Point;

import java.util.HashMap;
import java.util.Map;

/*
AUTHOR: Hampus Ekberg
RESPONSIBILITY: Tool for drawing with soft edges
USED BY: ImageModel
USES:PaintColor, DistanceHelper
 */

public class BrushTool extends AbstractPaintTool {

    private Map<Point<Integer>,Double> closestDistMap;
    private Map<Point<Integer>,PaintColor> origColorMap;
    private double hardness;

    public BrushTool() {
        closestDistMap = new HashMap<>();
        origColorMap = new HashMap<>();
    }

    @Override
    void paintPixel(int x, int y, Point<Double> newPoint, IEditableByTool imageModel, Point<Double> oldPoint){
        Point<Integer> pos = new Point<>(x,y);
        double dist = DistanceHelper.distToSegmentSquared(oldPoint, newPoint, new Point<>((double) x, (double) y));
        if (dist < (getSize() - 0.5) * (getSize() - 0.5)) {
            if(imageModel.existsInUndoBuffer(new Point<>(x,y))){
                if (closestDistMap.get(pos) <= dist){
                    return;
                }
            } else {
                imageModel.addToUndoBuffer(new Pixel(x,y,imageModel.getPixelColor(x,y)));
                origColorMap.put(pos,imageModel.getPixelColor(x, y));
                imageModel.setPixel(x, y, getPixelColor(dist,imageModel.getPixelColor(x,y)));
                closestDistMap.put(pos,dist);
                return;
            }
            imageModel.setPixel(x, y, getPixelColor(dist,origColorMap.get(pos)));
            closestDistMap.replace(pos,dist);
        }
    }

    @Override
    public void onPress(int x, int y, IEditableByTool imageModel) {
        closestDistMap.clear();
        origColorMap.clear();
        super.onPress(x,y,imageModel);
    }


    @Override
    PaintColor getPixelColor(double dist, PaintColor oldColor) {
        if (hardness > 0.99){
            return PaintColor.alphaBlend(color,oldColor);
        }
        return PaintColor.alphaBlend(new PaintColor(color.getRed(),color.getGreen(),color.getBlue(),(int)(Math.min(((255-((Math.sqrt(dist)/getSize())*255))/(1-hardness)),255)* color.getAlphaRatio())),oldColor);
    }

    @Override
    public void updateSettings(ToolSettings ts){
        super.updateSettings(ts);
        hardness = ts.getHardness();
    }
}
