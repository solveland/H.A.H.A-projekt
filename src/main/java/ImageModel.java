import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    private PaintLayer layer;
    private AbstractTool activeTool;
    private List<ModelObserver> observers;

    int toolSize = 15;
    int color = 0xFF000000;

    public ImageModel(int sizeX, int sizeY) {
        layer = new PaintLayer(sizeX, sizeY);
        activeTool = new PencilTool(toolSize);
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
    public PaintLayer getWritableLayer() {
        return layer;
    }

    public int getToolSize() {
        return toolSize;
    }

    public void setActiveTool(AbstractTool activeTool) {
        this.activeTool = activeTool;
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
}
