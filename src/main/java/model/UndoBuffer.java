package model;

import model.utils.PaintColor;
import model.utils.Pixel;
import model.utils.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UndoBuffer {
    List<Pixel> pixels;
    private PaintLayer layer;
    private HashSet<Point<Integer>> pointHashSet;

    public UndoBuffer(PaintLayer layer){
        this.layer = layer;
        pixels = new ArrayList<>();
        pointHashSet = new HashSet<>();
    }

    public void addPixel(int x, int y,PaintColor color){
        Pixel p = new Pixel(x,y,color);
        pixels.add(p);
        pointHashSet.add(p.getPosition());
    }

    public PaintLayer getLayer() {
        return layer;
    }

    public boolean contains(int x, int y){
        return pointHashSet.contains(new Point<Integer>(x,y));
    }
}
