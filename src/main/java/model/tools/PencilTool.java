package model.tools;

import model.pixel.PaintColor;

public class PencilTool extends AbstractPaintTool  {

    @Override
    PaintColor getPixelColor(double dist,PaintColor oldPixel) {
        return PaintColor.alphaBlend(color,oldPixel);
    }
}
