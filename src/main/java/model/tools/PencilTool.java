package model;

import model.utils.PaintColor;

public class PencilTool extends AbstractPaintTool implements ISize, IColor {


    @Override
    public void updateColor(PaintColor color) {
        this.color = color;
        updateBrush();
    }

    @Override
    public void updateSize(int size) {
        this.size = size;
        updateBrush();
    }


    @Override
    PaintColor getPixelColor(int x, int y,PaintColor oldPixel) {
        return PaintColor.alphaBlend(brushBuffer[y*(this.size*2 - 1) + x],oldPixel);
    }
}
