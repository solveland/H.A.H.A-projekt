package model.tools;

import model.pixel.PaintColor;

/*
AUTHOR: August Sölveland
RESPONSIBILITY: Tool for drawing with sharp edges
USED BY:
USES:
 */

public class PencilTool extends AbstractPaintTool  {

    @Override
    PaintColor getPixelColor(double dist,PaintColor oldPixel) {
        return PaintColor.alphaBlend(color,oldPixel);
    }
}
