package model;

import model.tools.*;
import model.utils.PaintColor;
import model.utils.Pixel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ImageModel {

    private List<PaintLayer> layerList;
    private PaintLayer activeLayer;
    private List<ModelObserver> observers;

    private int width;
    private int height;
    private PaintLayer renderedImage;

    private double zoomScaleX;
    private double zoomScaleY;
    private double oldZoomScaleY;
    private double oldZoomScaleX;

    private ITool activeTool;

    private PencilTool pencilTool;
    private BucketFillTool bucketFillTool;
    private EraserTool eraserTool;
    private ZoomTool zoomTool;

    private ToolSettings ts = new ToolSettings(5);


    private int newLayerCount;
    private Stack<UndoBuffer> undoBufferStack;

    public ImageModel(int sizeX, int sizeY) {
        width = sizeX;
        height = sizeY;



        pencilTool = new PencilTool();
        bucketFillTool = new BucketFillTool();
        eraserTool = new EraserTool();
        zoomTool = new ZoomTool();
        setActiveTool(pencilTool);

        layerList = new LinkedList<>();

        renderedImage = new PaintLayer(sizeX, sizeY, new PaintColor(0,0,0,0), null);

        observers = new ArrayList<>();


        createLayer(new PaintColor(255,255,255), "Background"); // Should be moved to the method where we initialize a new project.

        newLayerCount = 0;

        undoBufferStack = new Stack<>();
    }


    public void setImageSize(int width, int height){
        this.width = width;
        this.height = height;
        renderedImage = new PaintLayer(width,height,new PaintColor(0,0,0,0), null);
    }

    public List<PaintLayer> getLayerList() {
        return layerList;
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void updateCanvas() {
        if (!layerList.isEmpty() && activeLayer.isChanged())
            updateRenderedRect();

        if (renderedImage.isChanged()) {
            int minX = renderedImage.getChangedMinX();
            int maxX = renderedImage.getChangedMaxX();
            int minY = renderedImage.getChangedMinY();
            int maxY = renderedImage.getChangedMaxY();

            for (ModelObserver o : observers) {
                o.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, "imageUpdate");
            }

            renderedImage.resetChangeTracker();
        }
    }

    public void onDrag(int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onDrag(x, y, this);
            updateCanvas();
        }
    }

    public void onRelease(int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onRelease(x, y, this);
            updateCanvas();
        }
    }

    public void onPress(int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onPress(x, y, this);
            updateCanvas();
        }
    }

    private void setActiveTool(ITool activeTool) {
        this.activeTool = activeTool;
        activeTool.updateSettings(ts);
    }

    public void setSize(int size){
        ts.setSize(size);
        activeTool.updateSettings(ts);
    }

    public void setColor(PaintColor color){
        ts.setColor(color);
        activeTool.updateSettings(ts);
    }




    public void setToolShape(String s){
        ts.setShape(s);
        activeTool.updateSettings(ts);
    }

    private void updateLayerGUI() {
        int minX = renderedImage.getChangedMinX();
        int maxX = renderedImage.getChangedMaxX();
        int minY = renderedImage.getChangedMinY();
        int maxY = renderedImage.getChangedMaxY();

        for (ModelObserver o : observers) {
            o.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, "layerUpdate");
        }
    }

    public void clearLayer()
    {
        activeLayer.clearLayer();
        updateCanvas();
    }

    public void pushToUndoStack(UndoBuffer buffer){
        undoBufferStack.push(buffer);
    }

    public void undo(){
        if(undoBufferStack.empty()){
            return;
        }
        UndoBuffer buffer = undoBufferStack.pop();
        for(Pixel p : buffer.pixels){
            buffer.getLayer().setPixel(p.getX(),p.getY(),p.getColor());
        }
        updateRenderedImage();
    }

    public void activatePencilTool(){
        setActiveTool(pencilTool);
    }

    public void activateFillTool(){
        setActiveTool(bucketFillTool);
    }

    public void activateEraserTool(){
        setActiveTool(eraserTool);
    }

    public void activateZoomTool(){
        setActiveTool(zoomTool);
    }

    public double getZoomScaleY(){
        return zoomScaleY;
    }
    public double getZoomScaleX(){
        return zoomScaleX;
    }
    public void setZoomScaleY(double zoomValueY){
        this.zoomScaleY = zoomValueY;
    }
    public void setZoomScaleX(double zoomValueX){
        this.zoomScaleX = zoomValueX;
    }

    public double getOldZoomScaleY() {
        return oldZoomScaleY;
    }

    public void setOldZoomScaleY(double oldZoomValueY) {
        this.oldZoomScaleY = oldZoomValueY;
    }

    public double getOldZoomScaleX() {
        return oldZoomScaleX;
    }

    public void setOldZoomScaleX(double oldZoomValueX) {
        this.oldZoomScaleX = oldZoomValueX;
    }
    //// LAYER ///////

    private List<PaintLayer> reversedLayerList() {
        LinkedList<PaintLayer> reversedList = new LinkedList<>();

        for (PaintLayer l : layerList) {
            reversedList.push(l);
        }

        return reversedList;
    }

    // Toggles a layer and updates the view
    public void toggleLayerVisible(PaintLayer layer) {
        layer.toggleVisible();
        updateRenderedImage();
    }

    public int getNewLayerCount() {
        return newLayerCount;
    }

    public void createLayer(PaintColor bgColor, String name) {
        int index = 0;

        if (!layerList.isEmpty())
            index = indexOfActiveLayer();

        PaintLayer newLayer = new PaintLayer(width, height, bgColor, name);
        layerList.add(index, newLayer);

        setActiveLayer(newLayer);

        updateRenderedImage();
        updateLayerGUI();

        newLayerCount++;
    }

    // Deletes the active layer and selects a new active layer or null if no layers exist.
    public void deleteActiveLayer(){
        if (!layerList.isEmpty()) {
            int index = indexOfActiveLayer();

            clearLayer();
            undoBufferStack.removeIf(undoBuffer ->{
                return undoBuffer.getLayer() == activeLayer;
            });
            layerList.remove(index);

            if (!layerList.isEmpty()) {
                if (index > 0) {
                    index -= 1;
                }
                activeLayer = layerList.get(index);
            } else {
                activeLayer = null;
                renderTransparent();
            }

            updateLayerGUI();
        }
    }

    public void setActiveLayer(PaintLayer layer) {
        activeLayer = layer;
    }


    public void deleteAllLayers(){
        layerList.clear();
        activeLayer = null;
        updateLayerGUI();
    }

    public PaintLayer getActiveLayer() {
        return activeLayer;
    }

    private int indexOfActiveLayer() {
        if (!layerList.isEmpty()) {
            return layerList.indexOf(activeLayer);
        }
        return -1;
    }

    ///// LAYER END ////// RENDER START //////

    public void updateRenderedImage() {
        // Clear the image before we draw new pixels.
        renderImage(0,width,0,height);
    }

    private void updateRenderedRect() {
        int minX = activeLayer.getChangedMinX();
        int maxX = activeLayer.getChangedMaxX();
        int minY = activeLayer.getChangedMinY();
        int maxY = activeLayer.getChangedMaxY();
        renderImage(minX,maxX,minY,maxY);
    }

    private void renderImage(int minX,int maxX,int minY,int maxY){
        // Clear the rectangle before we draw new pixels.
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                renderedImage.setPixel(x, y, PaintColor.blank);
            }
        }

        // Draws the from bottom layer to top layer to make a top layer render over the bottom layer.
        if(!layerList.isEmpty()) {
            for (PaintLayer l : reversedLayerList()) {
                if (l.isVisible()) {
                    for (int x = minX; x < maxX; x++) {
                        for (int y = minY; y < maxY; y++) {
                            if ((l.getPixel(x, y).getAlpha() != 0))
                                renderedImage.setPixel(x, y, PaintColor.alphaBlend(l.getPixel(x, y),renderedImage.getPixel(x,y)));
                        }
                    }
                }
            }
        }
        for(PaintLayer layer : layerList){
            layer.resetChangeTracker();
        }
        updateCanvas();
    }

    private void renderTransparent() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                renderedImage.setPixel(x, y, PaintColor.blank);
            }
        }

        updateCanvas();
    }

    ///// RENDER END //////

}
