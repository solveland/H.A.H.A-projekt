package model.tools;

import model.pixel.PaintColor;

public class EyedropperTool implements ITool {


    public void onDrag(int x, int y, IModel imageModel){ }
    public void onPress(int x, int y, IModel imageModel){
    }
    public void onRelease(int x, int y, IModel imageModel){
        if ( ! (x < 0 || y < 0 || x >= imageModel.getWidth() || y >= imageModel.getHeight()) )
        {
            extractColor(x, y, imageModel);
        }
    }
    public void updateSettings(ToolSettings ts){}

    private void extractColor(int x, int y, IModel image){
        PaintColor extractedColor = image.getPixelColor(x,y);
        image.setColor(extractedColor);
    }

}
