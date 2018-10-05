package Model;

import Model.AbstractPaintTool;

public class EraserTool extends AbstractPaintTool implements ISize {


    public EraserTool(int size){
        this.size = size;
        this.color = 0xFFFFFFFF;
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
    int getPixelColor(int x, int y) {
        return 0;
    }
}