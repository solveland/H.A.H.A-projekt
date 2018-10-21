package model;

import model.pixel.PaintColor;
import model.pixel.Pixel;
import model.pixel.Point;

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
        addPixel(new Pixel(x,y,color));
    }

    public void addPixel(Pixel p){
        pixels.add(p);
        pointHashSet.add(p.getPosition());
    }

    public PaintLayer getLayer() {
        return layer;
    }

    public boolean contains(Point<Integer> p){
        return pointHashSet.contains(p);
    }
}
