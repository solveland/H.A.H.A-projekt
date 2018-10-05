package Model;

import java.util.ArrayList;
import java.util.List;

public class UndoBuffer {
    public List<Pixel> pixels;
    private PaintLayer layer;

    public UndoBuffer(PaintLayer layer){
        this.layer = layer;
        pixels = new ArrayList<>();
    }

    public void addPixel(int x, int y,int color){
        pixels.add(new Pixel(x,y,color));
    }

    public PaintLayer getLayer() {
        return layer;
    }
}
