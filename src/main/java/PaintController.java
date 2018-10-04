import Model.AbstractPaintTool;
import Model.ImageModel;
import Model.PaintLayer;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.awt.*;
import java.util.List;

public class PaintController implements LayerObserver {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView canvas;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private FlowPane layerView;

    @FXML
    private Spinner<Integer> sizeSpinner;

    @FXML
    private ToolBar brushBar;

    @FXML
    private Slider opacitySlider;

    @FXML
    private Label opacityLabel;


    private List<Node> layerViewList;

    private ImageModel image;

    private PaintView view;

    private int newLayerIndex;

    public void initialize() {
        image = new ImageModel(600, 600);
        view = new PaintView(600, 600);
        image.addObserver(view);

        layerViewList = layerView.getChildren();

        // Initialize project with white background layer.
        createLayer(0xFFFFFFFF, "Background");
        image.updateRenderedImage();
        canvas.setImage(view.getImage());
        canvas.setOnMouseDragged(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            image.onDrag(x, y);
        });

        canvas.setOnMouseReleased(e -> {
                    int x = (int) Math.floor(e.getX());
                    int y = (int) Math.floor(e.getY());
                    if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                        return;
                    }
                    image.onRelease(x, y);
                }
        );

        canvas.setOnMousePressed(e -> {
                    int x = (int) Math.floor(e.getX());
                    int y = (int) Math.floor(e.getY());
                    if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                        return;
                    }
                    image.onPress(x, y);
                }
        );



        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(e -> {
            Color temp = colorPicker.getValue();
            image.updateColor(temp);
            canvas.requestFocus();
        });
        clearCanvas();

        //Toolbar
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5) );
        sizeSpinner.setEditable(true);
        sizeSpinner.valueProperty().addListener((obs, oldvalue, newvalue) -> image.updateSize(newvalue));
        opacitySlider.valueProperty().addListener((obs, oldvalue, newvalue) -> image.updateOpacity(newvalue.doubleValue()));
        brushBar.setVisible(false);
    }


    @FXML
    public void openFile(){
        //Mycket av denna funktion måste flyttas till andra delar. pixlarna ska ändras i modellen, inte här t.ex.
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null){
            return;
        }
        BufferedImage loadedImage;
        try {
            loadedImage = ImageIO.read(file);
        } catch (java.io.IOException e) {
            //Todo: Handle this exception
            return;
        }
        view.setSize(loadedImage.getWidth(),loadedImage.getHeight());
        layerViewList.clear();
        image.deleteAllLayers();
        image.setImageSize(loadedImage.getWidth(),loadedImage.getHeight());
        canvas.setImage(view.getImage());
        canvas.setFitHeight(loadedImage.getHeight());
        canvas.setFitWidth(loadedImage.getWidth());
        createLayer(0,"Background");
        PaintLayer pl = image.getActiveLayer();
        for (int x = 0; x < loadedImage.getWidth(); x++) {
            for (int y = 0; y < loadedImage.getHeight(); y++) {
                pl.setPixel(x, y, loadedImage.getRGB(x, y));
            }
        }
        image.updateRenderedImage();
    }

    //Temporär testfunktion -> låter den vara image.getimage()....
    @FXML
    public void clearCanvas() {
        image.clearLayer();
    }

    @FXML
    public void setPencil() {
        image.setPencil();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
    }

    @FXML
    public void setFillTool() {
        image.setFillTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
    }

    @FXML
    public void setEraserTool() {
        image.setEraserTool();
        brushBar.setVisible(true);
        opacitySlider.setVisible(false);
        opacityLabel.setVisible(false);
        opacitySlider.setValue(1);
    }

    @FXML
    public void setZoomTool() {
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
        image.setZoomTool();
        layerViewList.clear();
        image.deleteAllLayers();
        createLayer(0xFF00FF00,"Background");
    }

    private void createLayer(int bgColor, String name) {
        int index = 0;

        if (!layerViewList.isEmpty())
            index = indexOfSelectedItem();

        image.createLayer(bgColor); // Create the layer in the model

        LayerItem layerItem = new LayerItem(name, image.getActiveLayer()); // Creates a layer item with an instance of the active layer in the model, which is the new layer.
        layerItem.addObserver(this); // Make the controller listen to every layer (for user input)

        layerViewList.add(index, layerItem); // Add the layer item to the pane which holds all layer items.

        selectLayer(layerItem);
    }

    // New layer button creates a transparent layer with a unique name
    @FXML
    public void newLayerAction() {
        createLayer(0, "Layer " + newLayerIndex);
        newLayerIndex++;
    }

    // Delete layer button deletes the selected layer and selects a new layer (or none if there are no layers)
    @FXML
    public void deleteLayerAction() {
        if (!layerViewList.isEmpty()) {
            int index = indexOfSelectedItem();

            layerViewList.remove(index);

            // Select the layer above the deleted layer or the topmost layer if the deleted layer is at the top.
            if (!layerViewList.isEmpty()) {
                if (index > 0)
                    index -= 1;

                LayerItem i = (LayerItem) layerViewList.get(index);
                i.setSelected();
            }

            image.deleteActiveLayer();
        }

    }

    // Deselects all selected layers
    private void deselectLayers() {
        for (Node n : layerViewList) {
            if (n.getClass().equals(LayerItem.class)) {
                LayerItem i = (LayerItem) n;
                if (i.isSelected()) {
                    i.setDeselected();
                }
            }
        }
    }

    private void selectLayer(LayerItem layer) {
        deselectLayers();
        layer.setSelected();
    }

    private int indexOfSelectedItem() {
        for (Node n : layerViewList) {
            LayerItem i = (LayerItem) n;
            if (i.isSelected()) {
                return layerViewList.indexOf(i);
            }
        }
        return -1;
    }

    // Listens to every layer for a selection
    @Override
    public void selectLayerUpdate(PaintLayer layer) {
        image.setActiveLayer(layer);
        deselectLayers();
    }

    // Listens to every layer for a layer toggle
    @Override
    public void visibleLayerToggle(PaintLayer layer) {
        image.toggleLayerVisible(layer);
    }


    private static final double MAX_SCALE = 15.0d;
    private static final double MIN_SCALE = 0.1d;

    @FXML
    public void zoom( boolean setZoom) {
        double delta = 1.05;
        image.setZoomScaleX(canvas.getScaleX());
        image.setZoomScaleY(canvas.getScaleY());
        image.setOldZoomScaleX(image.getZoomScaleX());
        image.setOldZoomScaleY(image.getZoomScaleY());

        double scaleY = image.getZoomScaleY();
        double scaleX = image.getZoomScaleX();

        if(setZoom)
        {
            scaleY *= delta;
            scaleX *= delta;
        } else {
            scaleY /= delta;
            scaleX /= delta;
        }
        scaleY = clamp(scaleY, MIN_SCALE, MAX_SCALE);
        scaleX = clamp(scaleX, MIN_SCALE, MAX_SCALE);

        double fy = (image.getZoomScaleY() / image.getOldZoomScaleY()) - 1;
        double fx = (image.getZoomScaleX() / image.getOldZoomScaleX()) - 1;

        double dx = (scrollPane.getLayoutX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
        double dy = (scrollPane.getLayoutY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));

        canvas.setScaleY(scaleY);
        canvas.setScaleX(scaleX);

        canvas.setTranslateY(canvas.getTranslateY()-fy*dy);
        canvas.setTranslateX(canvas.getTranslateX()-fx*dx);
    }

    @FXML
    public void zoomIn(){
        zoom(true);
    }
    @FXML
    public void zoomOut(){
        zoom(false);
    }

    private void setZoomPercent (int percent){
        canvas.setScaleY(percent);
        canvas.setScaleX(percent);
    }

    @FXML
    public void zoomHundred(){
        setZoomPercent(1);
    }



    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }

}
