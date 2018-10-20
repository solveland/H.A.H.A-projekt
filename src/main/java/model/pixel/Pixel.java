package model.pixel;

public class Pixel {
    private final Point<Integer> position;
    private final PaintColor color;

    public Pixel(int x, int y, PaintColor color){
        position = new Point(x,y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }
        if (!(o instanceof Pixel)){
            return false;
        }

        Pixel p = (Pixel)o;


        return p.position.equals(position);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + position.hashCode();
        //hash = 31 * hash + color.hashCode();
        return hash;
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
