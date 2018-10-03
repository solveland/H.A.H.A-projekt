package Model;

import Model.AbstractPaintTool;
import Model.IColor;
import Model.ISize;

public class PencilTool extends AbstractPaintTool implements ISize, IColor {




    public PencilTool(int size){
        this.size = size;
    }


    @Override
    public void updateColor(int color) {
        this.color = color;
        updateBrush();
    }

    @Override
    public void updateSize(int size) {
        this.size = size;
        updateBrush();
    }

    @Override
    int getPixelColor(int x, int y) {
        return brushBuffer[y*(this.size*2 -1) + x];
    }
}
