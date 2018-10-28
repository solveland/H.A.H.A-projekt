package model.tools;

import model.pixel.PaintColor;

/*
AUTHOR: Henrik Tao, Anthony Tao
RESPONSIBILITY: Tool for setting the programs current color to match that of a pixel on the image
USED BY: ImageModel
USES: PaintColor
 */

/**
 * EyedropperTool class for eyedropper feature.
 */

public class EyedropperTool implements ITool {


    public void onDrag(int x, int y, IEditableByTool imageModel, Boolean altDown){ }
    public void onPress(int x, int y, IEditableByTool imageModel, Boolean altDown){
    }
    public void onRelease(int x, int y, IEditableByTool imageModel, Boolean altDown){
        if ( ! (x < 0 || y < 0 || x >= imageModel.getWidth() || y >= imageModel.getHeight()) )
        {
            extractColor(x, y, imageModel);
        }
    }
    public void updateSettings(ToolSettings ts){}


    /**
     * The method extractColor extracts the color from a pixel and sets the current color to the extracted color.
     * @param x an x-coordinate value of clicked pixel
     * @param y an y-coordinate value of clicked pixel
     * @param image is a reference to the IModel interface
     */

    private void extractColor(int x, int y, IEditableByTool image){
        PaintColor extractedColor = image.getPixelColor(x,y);
        image.setColor(extractedColor);
    }

}
