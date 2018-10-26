package model;

import model.pixel.Point;
import model.tools.*;
import model.pixel.PaintColor;
import model.pixel.Pixel;

import java.util.*;
import java.util.List;

/*
AUTHOR: Henrik Tao, Anthony Tao, August Sölveland, Hampus Ekberg
RESPONSIBILITY: A broad connection point between the different parts of the model
USED BY: controller.PaintController, controller.LayerListController, Main
USES: Most other classes in the model package
 */

public class ImageModel implements IModel{

    private List<PaintLayer> layerList;
    private PaintLayer activeLayer;
    private List<ImageModelObserver> observers;

    private int width;
    private int height;
    private PaintLayer renderedImage;

    private double zoomScaleX;
    private double zoomScaleY;
    private double oldZoomScaleY;
    private double oldZoomScaleX;

    private PaintOverlay selectOverlay;
    private PaintOverlay shapeOverlay;
    private PaintOverlay renderedOverlay;
    private List<PaintOverlay> overlayList;

    private ITool activeTool;

    private PencilTool pencilTool;
    private BrushTool brushTool;
    private BucketFillTool bucketFillTool;
    private EraserTool eraserTool;
    private ZoomTool zoomTool;
    private SelectTool selectTool;
    private ShapeTool shapeTool;
    private EyedropperTool eyedropperTool;

    private ToolSettings ts = new ToolSettings(5);


    private int newLayerCount;
    private Stack<UndoBuffer> undoBufferStack;

    public ImageModel(int sizeX, int sizeY) {
        width = sizeX;
        height = sizeY;

        // Tools
        shapeTool = new ShapeTool();
        pencilTool = new PencilTool();
        bucketFillTool = new BucketFillTool();
        eraserTool = new EraserTool();
        zoomTool = new ZoomTool();
        brushTool = new BrushTool();
        selectTool = new SelectTool();
        eyedropperTool = new EyedropperTool();
        setActiveTool(pencilTool);

        // Overlay
        overlayList = new ArrayList<>();
        selectOverlay = new PaintOverlay();
        shapeOverlay = new PaintOverlay();
        overlayList.add(shapeOverlay);
        overlayList.add(selectOverlay);

        renderedOverlay = new PaintOverlay();

        observers = new ArrayList<>();

        // Layers
        layerList = new LinkedList<>();
        renderedImage = new PaintLayer(sizeX, sizeY, new PaintColor(0,0,0,0), null);
        createLayer(new PaintColor(255,255,255), "Background"); // Should be moved to the method where we initialize a new project.

        newLayerCount = 0;
        undoBufferStack = new Stack<>();
    }



    public List<PaintLayer> getLayerList() {
        return layerList;
    }

    public void addObserver(ImageModelObserver observer) {
        observers.add(observer);
    }

    public void updateCanvas() {
        if (!layerList.isEmpty()) {
            updateRenderedRect();
        }

        if (renderedImage.isChanged()) {
            int minX = renderedImage.getChangedMinX();
            int maxX = renderedImage.getChangedMaxX();
            int minY = renderedImage.getChangedMinY();
            int maxY = renderedImage.getChangedMaxY();

            for (ImageModelObserver o : observers) {
                o.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, ts.getPaintColor(), renderedOverlay,"imageUpdate");
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

    public ITool getActiveTool() {
        return activeTool;
    }

    public ZoomTool getZoomTool(){return zoomTool;}

    public void setSize(int size){
        ts.setSize(size);
        activeTool.updateSettings(ts);
    }
    public void setShapeSize(String s){
        switch (s) {
            case "Small":
                ts.setShapeSize(1);
                break;
            case "Medium":
                ts.setShapeSize(2);
                break;
            case "Large":
                ts.setShapeSize(3);
                break;
            default:
                ts.setShapeSize(1);
        }
        activeTool.updateSettings(ts);
    }

    public void setColor(PaintColor color){
        ts.setColor(color);
        activeTool.updateSettings(ts);
        updateColorPicker();
    }

    private void updateColorPicker(){
        int minX = renderedImage.getChangedMinX();
        int maxX = renderedImage.getChangedMaxX();
        int minY = renderedImage.getChangedMinY();
        int maxY = renderedImage.getChangedMaxY();

        for(ImageModelObserver observer: observers){
            observer.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, ts.getPaintColor(), renderedOverlay, "colorPickerUpdate" );
        }
    }

    public void setToolShape(String s){
        switch(s) {
            case "Circle":
                ts.setCircle();
                break;
            case "Ellipse":
                ts.setEllipse();
                break;
            case "Square":
                ts.setSquare();
                break;
            case "Rectangle":
                ts.setRectangle();
                break;
            case "Triangle":
                ts.setTriangle();
                break;
            case "Line":
                ts.setLine();
                break;
            default: ts.setCircle();
        }
        activeTool.updateSettings(ts);
    }

    public ToolSettings getTs() {
        return ts;
    }

    @Override
    public void setPixel(int x, int y, PaintColor color) {
        getActiveLayer().setPixel(x, y, color);
    }

    @Override
    public PaintColor getPixelColor(int x, int y) {
        return getActiveLayer().getPixel(x, y);
    }

    @Override
    public void addToShapeOverlay(List<Pixel> pixelList) {
        shapeOverlay.setNewOverlay(pixelList);
    }

    @Override
    public void addToSelectOverlay(List<Pixel> pixelList) {
        selectOverlay.setNewOverlay(pixelList);
    }


    public void undo(){
        if(undoBufferStack.empty()){
            return;
        }
        UndoBuffer buffer = undoBufferStack.pop();
        // Need to deselect layer temporarily because the layer being selected can block setPixel
        boolean oldSelected = buffer.getLayer().hasSelection();
        buffer.getLayer().setSelection(false);
        for(Pixel p : buffer.pixels){
            buffer.getLayer().setPixel(p.getX(),p.getY(),p.getColor());
        }
        buffer.getLayer().setSelection(oldSelected);
        updateCanvas();
    }

    public void activateShapeTool(){
        setActiveTool(shapeTool);
    }

    public void activatePencilTool(){
        setActiveTool(pencilTool);
    }

    public void activateBrushTool(){
        setActiveTool(brushTool);
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

    public void activateSelectTool(){setActiveTool(selectTool);}

    public void activateEyedropperTool(){setActiveTool(eyedropperTool);}

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

    private void updateLayerGUI() {
        int minX = renderedImage.getChangedMinX();
        int maxX = renderedImage.getChangedMaxX();
        int minY = renderedImage.getChangedMinY();
        int maxY = renderedImage.getChangedMaxY();

        for (ImageModelObserver o : observers) {
            o.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, ts.getPaintColor(), renderedOverlay, "layerUpdate");
        }
    }

    public void clearLayer()
    {
        activeLayer.clearLayer();
        updateCanvas();
    }

    public void setHardness(double hardness) {
        ts.setHardness(hardness);
        activeTool.updateSettings(ts);
    }

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

        if (!layerList.isEmpty()) {
            index = indexOfActiveLayer();
        }

        PaintLayer newLayer = new PaintLayer(width, height, bgColor, name);
        layerList.add(index, newLayer);

        setActiveLayer(newLayer);

        updateRenderedImage();
        updateLayerGUI();

        newLayerCount++;
    }

    // Deletes the active layer and selects a new active layer or none if no layers exist.
    public void deleteActiveLayer(){
        if (!layerList.isEmpty()) {
            int index = indexOfActiveLayer();

            undoBufferStack.removeIf(undoBuffer ->{
                return undoBuffer.getLayer() == activeLayer;
            });
            layerList.remove(index);

            // Select the layer above the deleted layer or the topmost layer if the deleted layer is at the top.
            if (!layerList.isEmpty()) {
                if (index > 0) {
                    index -= 1;
                }
                updateRenderedImage();
                setActiveLayer(layerList.get(index));
            } else {
                setActiveLayer(null);
                renderTransparent();
            }

            updateLayerGUI();
        }
    }

    public void setActiveLayer(PaintLayer layer) {
        if(activeLayer != null){
            if(activeLayer.hasSelection() && layer != null){
                layer.selectArea(activeLayer.getSelectedStartPoint(), activeLayer.getSelectedEndPoint());
            }
        }

        activeLayer = layer;
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

    public void moveLayerIndex(int index, PaintLayer movingLayer) {
        int i = index;

        if (i == layerList.indexOf(movingLayer)) {
            return;
        }

        if (i >= layerList.size()) {
            i = layerList.size() - 1;
        }

        layerList.remove(movingLayer);
        layerList.add(i, movingLayer);
        setActiveLayer(movingLayer);

        updateRenderedImage();
        updateLayerGUI();
    }

    public void moveLayerAbove(int index, PaintLayer movingLayer){
        int newIndex = index;

        if (layerList.indexOf(movingLayer) < newIndex)
            newIndex -= 1;

        moveLayerIndex(newIndex, movingLayer);
    }

    public void moveLayerUnder(int index, PaintLayer movingLayer){
        int newIndex = index +1;

        if (layerList.indexOf(movingLayer) < newIndex)
            newIndex -= 1;

        moveLayerIndex(newIndex, movingLayer);
    }

    ///// LAYER END ////// RENDER START //////

    public void updateRenderedImage() {
        renderImage(0,width,0,height);
        updateCanvas();
    }

    private void updateRenderedRect() {
        int minX = activeLayer.getChangedMinX();
        int maxX = activeLayer.getChangedMaxX();
        int minY = activeLayer.getChangedMinY();
        int maxY = activeLayer.getChangedMaxY();
        renderImage(minX,maxX,minY,maxY);
    }

    private void renderImage(int minX,int maxX,int minY,int maxY){
        // Fill in a checkered background to make transparent parts easier to see
        PaintColor bg1 = new PaintColor(180,180,180);
        PaintColor bg2 = new PaintColor(130,130,130);
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                renderedImage.setPixel(x, y, ((x/20 + y/20) % 2 == 0)? bg1 : bg2);
            }
        }

        // Draws the image from bottom layer to top layer to make a top layer render over the bottom layer.
        if(!layerList.isEmpty()) {
            for (PaintLayer l : reversedLayerList()) {
                if (l.isVisible()) {
                    for (int x = minX; x < maxX; x++) {
                        for (int y = minY; y < maxY; y++) {
                            if ((l.getPixel(x, y).getAlpha() != 0)) {
                                renderedImage.setPixel(x, y, PaintColor.alphaBlend(l.getPixel(x, y), renderedImage.getPixel(x, y)));
                            }
                        }
                    }
                }
            }
        }

        for(PaintLayer layer : layerList){
            layer.resetChangeTracker();
        }
    }

    public void loadImage(List<PaintLayer> newLayerList){
        layerList = newLayerList;
        setActiveLayer(newLayerList.get(0));
        updateLayerGUI();
        width = newLayerList.get(0).getWidth();
        height = newLayerList.get(0).getHeight();
        renderedImage = new PaintLayer(width,height,PaintColor.blank, null);
        updateCanvas();
    }

    private void renderTransparent() {
        PaintColor bg1 = new PaintColor(180,180,180);
        PaintColor bg2 = new PaintColor(130,130,130);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                renderedImage.setPixel(x, y, ((x/20 + y/20) % 2 == 0)? bg1 : bg2);
            }
        }
        updateCanvas();
    }

    public PaintLayer getRenderedImage() {
        PaintLayer imageCopy = new PaintLayer(renderedImage.getWidth(), renderedImage.getHeight(), renderedImage.getBgColor(), "");
        for (int x = 0; x < renderedImage.getWidth(); x++) {
            for (int y = 0; y < renderedImage.getHeight(); y++) {
                imageCopy.setPixel(x, y, renderedImage.getPixel(x, y));
            }
        }

        return imageCopy;
    }

    public void renderOverlay() {
        renderedOverlay.getOverlay().clear();
        renderedOverlay.getOldOverlay().clear();

        for (PaintOverlay overlay : overlayList) {
            renderedOverlay.getOldOverlay().addAll(overlay.getOldOverlay());
        }

        for(PaintOverlay overlay: overlayList) {
            List<Pixel> old = overlay.getOldOverlay();
            old.clear();
        }

        // SelectTool
        for(PaintOverlay overlay: overlayList) {
            renderedOverlay.getOverlay().addAll(overlay.getOverlay());
        }

        updateOverlay();
    }

    @Override
    public void openNewUndoBuffer() {
        undoBufferStack.push(new UndoBuffer(activeLayer));
    }

    @Override
    public void addToUndoBuffer(Pixel pixel) {
        undoBufferStack.peek().addPixel(pixel);
    }

    @Override
    public boolean existsInUndoBuffer(Point<Integer> point) {
        return undoBufferStack.peek().contains(point);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void selectArea(Point<Integer> start, Point<Integer> end) {
        activeLayer.selectArea(start,end);
    }

    public void updateOverlay() {
        int minX = renderedImage.getChangedMinX();
        int maxX = renderedImage.getChangedMaxX();
        int minY = renderedImage.getChangedMinY();
        int maxY = renderedImage.getChangedMaxY();

        for (ImageModelObserver o : observers) {
            o.notifyObservers(renderedImage, minX, maxX, minY, maxY, layerList, ts.getPaintColor(), renderedOverlay, "overlayUpdate");
        }
    }

    ///// RENDER END //////

    public void deselectArea(){
        selectOverlay.getOverlay().clear();
        selectOverlay.getOldOverlay().clear();
        activeLayer.setSelection(false);
        updateRenderedImage();
    }

}
