package model.tools;

import model.utils.PaintColor;

public class EraserTool extends AbstractPaintTool {


    public EraserTool(){
        this.color = new PaintColor(255,255,255);
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