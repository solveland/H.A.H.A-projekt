package Model;

public class Pixel {
    private int x;
    private int y;
    private int color;

    public Pixel(int x, int y, int color){
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

    public int getColor() {
        return color;
    }
}
