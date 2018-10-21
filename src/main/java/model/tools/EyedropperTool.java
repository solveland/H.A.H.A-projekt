package model.tools;

import model.pixel.PaintColor;

public class EyedropperTool implements ITool {

    private PaintColor extractedColor;

    public void onDrag(int x, int y, IModel imageModel){ }
    public void onPress(int x, int y, IModel imageModel){
    }
    public void onRelease(int x, int y, IModel imageModel){
        if ( ! (x < 0 || y < 0 || x >= imageModel.getActiveLayer().getWidth() || y >= imageModel.getActiveLayer().getHeight()) )
        {
            extractColor(x, y, imageModel);
        }
    }
    public void updateSettings(ToolSettings ts){}

    private void extractColor(int x, int y, IModel image){
        extractedColor = image.getActiveLayer().getPixel(x,y);
        image.setColor(extractedColor);
    }
}
