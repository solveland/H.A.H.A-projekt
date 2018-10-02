package Model;

import Model.BucketFillTool;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ImageModel {

    private PaintLayer layer;
    private ITool activeTool;
    private List<ModelObserver> observers;
    private PencilTool pencilTool;
    private BucketFillTool bucketFillTool;
    private EraserTool eraserTool;

    int toolSize = 64;
    int color = 0xFF000000;

    public ImageModel(int sizeX, int sizeY) {
        layer = new PaintLayer(sizeX, sizeY);
        pencilTool = new PencilTool(toolSize);
        bucketFillTool = new BucketFillTool(color);
        eraserTool = new EraserTool(toolSize);
        setActiveTool(bucketFillTool);
        observers = new ArrayList<ModelObserver>();
    }

    public void updateColor(Color color){
        this.color = 0xFF000000 | ((int)(color.getRed() * 255) << 16) | ((int)(color.getGreen() * 255) << 8) | ((int)(color.getBlue() * 255));
        if(activeTool instanceof IColor){
            ((IColor) activeTool).updateColor(this.color);
        }
    }

    public void updateSize(int size){
        this.toolSize = size;
        if(activeTool instanceof ISize){
            ((ISize) activeTool).updateSize(toolSize);
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

    public void setActiveTool(ITool activeTool) {
        this.activeTool = activeTool;
        if(activeTool instanceof ISize){
            ((ISize) activeTool).updateSize(toolSize);
        }
        if(activeTool instanceof IColor){
            ((IColor) activeTool).updateColor(color);
        }
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public void updateModel() {
        if (layer.isChanged()){
            for (ModelObserver o : observers) {
                o.drawOnUpdate(layer, layer.getChangedMinX(), layer.getChangedMaxX(), layer.getChangedMinY(), layer.getChangedMaxY());
            }
            layer.resetChangeTracker();
        }

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

    public void setEraserTool(){ setActiveTool(eraserTool);}

}
