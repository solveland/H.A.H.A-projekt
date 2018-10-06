package Model;

import Model.AbstractPaintTool;

public class EraserTool extends AbstractPaintTool implements ISize {


    public EraserTool(){
        this.color = new PaintColor(255,255,255);
    }


    public void updateSize(int size) {
        this.size = size;
        updateBrush();
    }

    /*
        TODO: This will vary when we have layers, it will make it transcendent if you erase on a layer, but white at the bottom layer.
        This is up for discussion atm.
     */

    @Override
    PaintColor getPixelColor(int x, int y,PaintColor oldPixel) {
        return PaintColor.blank;
    }
}