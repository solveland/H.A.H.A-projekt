package model.tools;

import model.utils.PaintColor;
import model.utils.Shape;

public class ToolSettings {
    private PaintColor color;
    private int size;
    private Shape shape;

    public ToolSettings(int size){
        this.size = size;
        shape = Shape.CIRCLE;
    }

    public void setColor(PaintColor color) {
        this.color = color;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setShape(String shape){
        if(shape.equals("Circle")) {
            this.shape = Shape.CIRCLE;
        }else if(shape.equals("Square")) {
            this.shape = Shape.SQUARE;
        }
    }
    public PaintColor getPaintColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public Shape getShape() {
        return shape;
    }
}
