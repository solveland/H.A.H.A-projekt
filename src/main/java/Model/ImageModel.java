package Model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImageModel {

    private List<PaintLayer> layerList;
    private PaintLayer activeLayer;
    private ITool activeTool;
    private List<ModelObserver> observers;
    private PencilTool pencilTool;
    private BucketFillTool bucketFillTool;
    private EraserTool eraserTool;
    private ZoomTool zoomTool;
    private int width;
    private int height;
    private PaintLayer renderedImage;

    private int toolSize = 64;
    private int color = 0xFF000000;

    private double zoomScaleX;
    private double zoomScaleY;
    private double oldZoomScaleY;
    private double oldZoomScaleX;

    private int newLayerCount;

    public ImageModel(int sizeX, int sizeY) {
        width = sizeX;
        height = sizeY;

        activeLayer = null;

        layerList = new LinkedList<>();

        renderedImage = new PaintLayer(sizeX, sizeY, 0, null);

        pencilTool = new PencilTool(toolSize);
        bucketFillTool = new BucketFillTool(color);
        eraserTool = new EraserTool(toolSize);
        zoomTool = new ZoomTool();
        setActiveTool(bucketFillTool);

        observers = new ArrayList<>();

        newLayerCount = 0;

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

    public void setImageSize(int width, int height){
        this.width = width;
        this.height = height;
        renderedImage = new PaintLayer(width,height,0, null);
    }

    public void onDrag(int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onDrag(x, y, activeLayer);
            updateCanvas();
        }
    }

    public void onRelease (int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onRelease(x, y, activeLayer);
            updateCanvas();
        }
    }

    public void onPress (int x, int y){
        if (!(activeLayer == null)) {
            activeTool.onPress(x, y, activeLayer);
            updateCanvas();
        }
    }

    public List<PaintLayer> getLayerList() {
        return layerList;
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

    ////////////////////////////////////////////SET_TOOLS_START/////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setPencil (){
        setActiveTool(pencilTool);
    }

    public void setFillTool (){
        setActiveTool(bucketFillTool);
    }

    public void setEraserTool(){ setActiveTool(eraserTool); }

    public void setZoomTool(){setActiveTool(zoomTool);}

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

    public void createLayer(int bgColor, String name) {
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
            layerList.remove(index);

            if (!layerList.isEmpty()) {
                if (index > 0)
                    index -= 1;

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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                renderedImage.setPixel(x, y, 0);
            }
        }

        if (!layerList.isEmpty()) {
            for (PaintLayer l : reversedLayerList()) {
                if (l.isVisible()) {
                    for (int x = 0; x < l.getWidth(); x++) {
                        for (int y = 0; y < l.getHeight(); y++) {
                            if (l.getPixel(x, y) != 0)
                                renderedImage.setPixel(x, y, l.getPixel(x, y));
                        }
                    }
                }
            }
        }
        updateCanvas();
    }

    private void updateRenderedRect() {
        int minX = activeLayer.getChangedMinX();
        int maxX = activeLayer.getChangedMaxX();
        int minY = activeLayer.getChangedMinY();
        int maxY = activeLayer.getChangedMaxY();

        // Clear the rectangle before we draw new pixels.
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                renderedImage.setPixel(x, y, 0);
            }
        }

        // Draws the from bottom layer to top layer to make a top layer render over the bottom layer.
        for (PaintLayer l : reversedLayerList()) {
            if (l.isVisible()) {
                for (int x = minX; x < maxX; x++) {
                    for (int y = minY; y < maxY; y++) {
                        if ((l.getPixel(x, y) != 0))
                            renderedImage.setPixel(x, y, l.getPixel(x, y));
                    }
                }
            }
        }

        activeLayer.resetChangeTracker();
    }

    private void renderTransparent() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                renderedImage.setPixel(x, y, 0);
            }
        }

        updateCanvas();
    }

    ///// RENDER END //////

}
