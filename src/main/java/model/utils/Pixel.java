package model.utils;

public class Pixel {
    private final Point<Integer> position;
    private final PaintColor color;

    public Pixel(int x, int y, PaintColor color){
        position = new Point(x,y);
        this.color = color;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public Point<Integer> getPosition(){
        return position;
    }

    public PaintColor getColor() {
        return color;
    }
}
