package model.tools;

import model.ImageModel;
import model.PaintLayer;
import model.UndoBuffer;
import model.pixel.PaintColor;

/**
 * This class is an abstraction of a pencil/eraser.
 * It contains the shared logic used for both drawing and erasing pixels on a canvas.
 */
public abstract class AbstractPaintTool implements ITool {
    PaintColor[] brushBuffer;
    int size;
    protected PaintColor color;
    private UndoBuffer undoBuffer;
    private Shape shape = Shape.CIRCLE;
    /* TODO: When we have a broader understanding of what the tools will require we have to move functionality to this class
     */
    public void onPress(int x, int y, IModel imageModel){
        undoBuffer = new UndoBuffer(imageModel.getActiveLayer());
        changePixels(x, y, imageModel.getActiveLayer());
        imageModel.pushToUndoStack(undoBuffer);
    }
    public void onDrag(int x, int y, IModel imageModel){
        changePixels(x, y, imageModel.getActiveLayer());
    }
    public void onRelease(int x, int y, IModel imageModel){

    }


    @Override
    public void updateSettings(ToolSettings ts) {
        this.size = ts.getSize();
        this.color = ts.getPaintColor();
        this.shape = ts.getShape();
        updateBrush();
    }

    /**
     * Checks the brushbuffer for colored pixels and sets layer accordingly.
     */
    private void changePixels(int xPos, int yPos, PaintLayer layer){
        int diameter = size*2-1;
        for(int yc = 0; yc < diameter; yc++){
            for(int xc = 0; xc < diameter; xc++){
                if(brushBuffer[yc*diameter +xc].getAlpha() != 0 ){
                    int pixelX = xc+xPos - diameter/2;
                    int pixelY = yc+yPos - diameter/2;
                    if(pixelX < 0 || pixelX >= layer.getWidth() || pixelY < 0 ||pixelY >= layer.getHeight() || layer.getPixel(xc+xPos - diameter/2,yc+yPos - diameter/2) == getPixelColor(xc, yc,layer.getPixel(xc+xPos - diameter/2,yc+yPos - diameter/2))){
                        continue;
                    }
                    if (undoBuffer.contains(xc + xPos - diameter/2, yc + yPos - diameter/2)){
                        continue;
                    }
                    undoBuffer.addPixel(xc+xPos - diameter/2,yc+yPos - diameter/2,layer.getPixel(xc+xPos - diameter/2,yc+yPos - diameter/2));
                    layer.setPixel(xc+xPos - diameter/2,yc+yPos - diameter/2, getPixelColor(xc, yc,layer.getPixel(xc + xPos - diameter / 2,yc + yPos - diameter / 2)));
                }
            }
        }
    }

    /**
     *  Updates the brushBuffer depending on the shape, size and color variables. This is done by calculating the shape and then representing it as an array.
     */

    protected void updateBrush(){
        int brushDiameter = size * 2 - 1;
        PaintColor empty = new PaintColor(0,0,0,0);
        brushBuffer = new PaintColor[brushDiameter * brushDiameter];
        int midPoint = size -1;
        if(shape == Shape.CIRCLE) {
            for (int y = 0; y < brushDiameter; y++) {
                for (int x = 0; x < brushDiameter; x++) {
                    if (Math.sqrt((x - midPoint) * (x - midPoint) + (y - midPoint) * (y - midPoint)) > size - 0.5) {
                        brushBuffer[y * brushDiameter + x] = empty;
                    } else {
                        brushBuffer[y * brushDiameter + x] = color;
                    }

                }
            }
        }else if(shape == Shape.SQUARE){
            for (int y = 0; y < brushDiameter; y++) {
                for (int x = 0; x < brushDiameter; x++) {
                    brushBuffer[y * brushDiameter + x] = color;
                }
            }
        }
    }



    abstract PaintColor getPixelColor(int x, int y,PaintColor oldColor);


}
