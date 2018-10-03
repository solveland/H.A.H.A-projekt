import Model.AbstractPaintTool;
import Model.ImageModel;
import Model.PaintLayer;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public class PaintController implements LayerObserver {


    @FXML
    private ImageView canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private FlowPane layerView;

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
            image.onRelease(x, y);}
            );

        canvas.setOnMousePressed(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            image.onPress(x, y);}
        );


        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(e -> {
            Color temp = colorPicker.getValue();
            image.updateColor(temp);
            canvas.requestFocus();
        });
        clearCanvas();
    }

    //Temporär testfunktion -> låter den vara image.getimage()....
@FXML
    public void clearCanvas () {
        image.clearLayer();
    }

    @FXML
    public void setPencil (){
        image.setPencil();
    }

    @FXML
    public void setFillTool (){
        image.setFillTool();
    }

    @FXML
    public void setEraserTool(){image.setEraserTool();}

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
        for (Node n : layerViewList)
        {
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
        for (Node n : layerViewList){
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
    public void visibleLayerToggle(PaintLayer layer)
    {
        image.toggleLayerVisible(layer);
    }

}
