import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    private PaintLayer layer;
    private AbstractTool activeTool;
    private List<ModelObserver> observers;
    private PencilTool pencilTool;
    private BucketFillTool bucketFillTool;

    int toolSize = 15;
    int color = 0xFF000000;

    public ImageModel(int sizeX, int sizeY) {
        layer = new PaintLayer(sizeX, sizeY);
       // activeTool = new PencilTool(toolSize);
        pencilTool = new PencilTool(toolSize);
        bucketFillTool = new BucketFillTool(color, layer);
        setActiveTool(bucketFillTool);
        if(activeTool instanceof ISizeAndColor){
            ((ISizeAndColor) activeTool).updateSizeAndColor(toolSize, color);
        }

        observers = new ArrayList<ModelObserver>();
    }

    public void updateColor(Color color){
        this.color = 0xFF000000 | ((int)(color.getRed() * 255) << 16) | ((int)(color.getGreen() * 255) << 8) | ((int)(color.getBlue() * 255));
        if(activeTool instanceof ISizeAndColor){
            ((ISizeAndColor) activeTool).updateSizeAndColor(toolSize, this.color);
        }
    }
    public void onDrag(int x, int y){
        activeTool.onDrag(x, y, layer);
        updateModel();
    }

    public void onRelease (int x, int y){
        activeTool.onRelease(x,y,layer);
        updateModel();
    }

    public void onPress (int x, int y){
        activeTool.onPress(x,y,layer);
        updateModel();
    }

    public PaintLayer getWritableLayer() {
        return layer;
    }

    public int getToolSize() {
        return toolSize;
    }

    public void setActiveTool(AbstractTool activeTool) {
        this.activeTool = activeTool;
        if(activeTool instanceof ISizeAndColor){
            ((ISizeAndColor) activeTool).updateSizeAndColor(toolSize, this.color);
        }
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public void updateModel() {
        for (ModelObserver o : observers)
            o.drawOnUpdate(layer);
    }

    public void clearLayer()
    {
        layer.clearLayer();
        updateModel();
    }

    public void setPencil (){
        setActiveTool(pencilTool);
    }

    public void setFillTool (){
        setActiveTool(bucketFillTool);
    }

}
