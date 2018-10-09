package model;

import model.utils.PaintColor;
import model.utils.Pixel;

import java.util.ArrayList;
import java.util.List;

public class UndoBuffer {
    public List<Pixel> pixels;
    private PaintLayer layer;

    public UndoBuffer(PaintLayer layer){
        this.layer = layer;
        pixels = new ArrayList<>();
    }

    public void addPixel(int x, int y,PaintColor color){
        pixels.add(new Pixel(x,y,color));
    }

    public PaintLayer getLayer() {
        return layer;
    }

    public boolean contains(int x, int y){
        //TODO: Implement a hash set to speed this up
        for(Pixel p : pixels){
            if (p.getX() == x && p.getY() == y){
                return true;
            }
        }
        return false;
    }
}
