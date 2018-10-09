package model.utils;

public class Pixel {
    private int x;
    private int y;
    private PaintColor color;

    public Pixel(int x, int y, PaintColor color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PaintColor getColor() {
        return color;
    }
}
