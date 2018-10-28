package model.tools;

import model.pixel.PaintColor;

/*
AUTHOR: Henrik Tao, Anthony Tao
RESPONSIBILITY: Tool for setting the programs current color to match that of a pixel on the image
USED BY:
USES: PaintColor
 */

public class EyedropperTool implements ITool {

    private PaintColor extractedColor;

    public void onDrag(int x, int y, IEditableByTool imageModel){ }
    public void onPress(int x, int y, IEditableByTool imageModel){
    }
    public void onRelease(int x, int y, IEditableByTool imageModel){
        if ( ! (x < 0 || y < 0 || x >= imageModel.getWidth() || y >= imageModel.getHeight()) )
        {
            extractColor(x, y, imageModel);
        }
    }
    public void updateSettings(ToolSettings ts){}

    private void extractColor(int x, int y, IEditableByTool image){
        extractedColor = image.getPixelColor(x,y);
        image.setColor(extractedColor);
    }

}
