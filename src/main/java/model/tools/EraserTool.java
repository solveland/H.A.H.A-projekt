package model.tools;

import model.pixel.PaintColor;

public class EraserTool extends AbstractPaintTool {


    public EraserTool(){
        this.color = new PaintColor(255,255,255);
    }

    @Override
    PaintColor getPixelColor(double dist,PaintColor oldPixel) {
        return PaintColor.blank;
    }
}