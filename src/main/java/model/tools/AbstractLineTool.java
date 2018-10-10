package model.tools;

import model.ImageModel;
import model.utils.PaintColor;
import model.utils.Pixel;

public abstract class AbstractLineTool implements ITool {
    PaintColor[] brushBuffer;
    Pixel startPixel;
    Pixel endPixel;
    PaintColor c = new PaintColor(0xFF, 0xFF, 0xFF, 0x55);


    @Override
    public void onDrag(int x, int y, ImageModel imageModel) {


    }

    @Override
    public void onPress(int x, int y, ImageModel imageModel) {
        startPixel = new Pixel(x, y, c);
    }

    @Override
    public void onRelease(int x, int y, ImageModel imageModel) {

    }

    protected abstract void updateBuffer(int x, int y);

}
