package model.pixel;

public class Point <T> {
    private final T x;
    private final T y;

    public Point (T x, T y){
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }


    public T getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point<?> point = (Point<?>) o;

        if (!getX().equals(point.getX())) return false;
        return getY().equals(point.getY());
    }

    @Override
    public int hashCode() {
        int result = getX().hashCode();
        result = 31 * result + getY().hashCode();
        return result;
    }
}