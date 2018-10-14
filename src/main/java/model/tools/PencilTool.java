package model.tools;

import model.pixel.PaintColor;

public class PencilTool extends AbstractPaintTool  {



    @Override
    PaintColor getPixelColor(int x, int y,PaintColor oldPixel) {
        return PaintColor.alphaBlend(brushBuffer[y*(this.size*2 - 1) + x],oldPixel);
    }
}
