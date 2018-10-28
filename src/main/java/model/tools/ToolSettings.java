package model.tools;

import model.pixel.PaintColor;

/*
AUTHOR: August Sölveland
RESPONSIBILITY: A collection of settings that different tools might use
USED BY: All tools
USES: PaintColor
 */

public class ToolSettings {
    private PaintColor color;
    private int size;
    private Shape shape;
    private double hardness;
    private int shapeSize;


    public ToolSettings(int size){
        this.size = size;
        shape = Shape.CIRCLE;
        shapeSize = 1;
    }

    public void setColor(PaintColor color) {
        this.color = color;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLine(){
       this.shape = Shape.LINE;
    }
    public void setTriangle(){
        this.shape = Shape.TRIANGLE;
    }
    public void setRectangle(){
        this.shape = Shape.RECTANGLE;
    }
    public void setSquare(){
        this.shape = Shape.SQUARE;
    }
    public void setCircle(){
        this.shape = Shape.CIRCLE;
    }
    public void setEllipse(){ this.shape = Shape.ELLIPSE; }



    public PaintColor getPaintColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public Shape getShape() {
        return shape;
    }

    public double getHardness() {
        return hardness;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public int getShapeSize() {
        return shapeSize;
    }

    public void setShapeSize(int shapeSize) {
        this.shapeSize = shapeSize;
    }
}
