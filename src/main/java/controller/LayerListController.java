package controller;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import model.ImageModel;
import model.ImageModelObserver;
import model.pixel.PaintColor;
import model.PaintLayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import model.PaintOverlay;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
AUTHOR: Anthony Tao
RESPONSIBILITY: Keeping track of the list of layer items and making sure you can drag them around.
USED BY: PaintController
USES: PaintLayer, ImageModel
 */

public class LayerListController extends StackPane implements ImageModelObserver, LayerItemObserver, ILayerListController {

    @FXML
    private StackPane frontPane;

    @FXML
    private TitledPane titledPane;

    @FXML
    private FlowPane layerView;

    @FXML
    private Button deleteLayerButton;

    private List<Node> layerViewList;
    private ImageModel image;
    private ImageView dragImage;

    private double mouseY;

    public LayerListController(ImageModel image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LayerList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.image = image;
        layerViewList = layerView.getChildren();

        frontPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                LayerItemController source = (LayerItemController) e.getGestureSource();

                if (frontPane.getChildren().contains(dragImage))
                {
                    frontPane.getChildren().remove(dragImage);
                }
                source.setCursor(Cursor.HAND);
            }
        });

        frontPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                LayerItemController source = (LayerItemController) e.getGestureSource();

                source.setCursor(Cursor.HAND);
                e.consume();
            }
        });

        frontPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                LayerItemController source = (LayerItemController) e.getGestureSource();

                source.setCursor(Cursor.CLOSED_HAND);
                e.consume();
            }
        });

        updateLayerItemList(image.getLayerList());
    }

    /*
        Listens to the model's layer list and updates the layer GUI.
     */
    @Override
    public void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, PaintOverlay overlay, String id) {
        if (id.equals("layerUpdate")) {
            if (!layerList.isEmpty()) {
                updateLayerItemList(layerList);
                selectActiveLayer();
            } else {
                if (!layerViewList.isEmpty())
                {
                    layerViewList.clear();
                }
            }
        }
    }

    private void updateLayerItemList(List<PaintLayer> layerList) {
        List<Node> tempList = new ArrayList<>();
        tempList.addAll(layerViewList); // Copy the layer-item list to reuse layer-items.
        layerViewList.clear(); // Clear the layer-item list to later copy the model layer list (create layer-items or reuse/not use old layer-items).

        for (int i = 0; i < layerList.size(); i++) {
            PaintLayer p = layerList.get(i);

            if (tempList.isEmpty()) {
                createLayerItem(p, p.getName(), i);
                continue;
            }

            Boolean itemExists = false;
            for (Node n : tempList) {
                if (n.getClass().equals(LayerItemController.class)) {
                    LayerItemController l1 = (LayerItemController) n;

                    // If the layer and corresponding layer-item exists, add it to the layer-item list.
                    if (p.equals(l1.getLayer())) {
                        layerViewList.add(i, l1);
                        itemExists = true;
                        break;
                    }
                }
            }
            // If the layer-item does not already exist, create the layer-item.
            if (!itemExists)
            {
                createLayerItem(p, p.getName(), i);
            }
        }

        selectActiveLayer();
    }

    /*
     Only used for creating a layer-item for an existing layer in the model.
      */
    private void createLayerItem(PaintLayer layer, String layerName, int index) {
        ILayerItemController layerItem = new LayerItemController(layerName, layer);
        layerItem.addObserver(this);
        setLayerDragEvents(layerItem);
        layerViewList.add(index, layerItem.getLayerItemPane());
    }

    private void createLayer(PaintColor bgColor, String name) {
        image.createLayer(bgColor, name); // Create the layer in the model
    }

    /*
     New layer button creates a transparent layer with a unique name
      */
    @Override
    @FXML
    public void newLayerAction() {
        createLayer(PaintColor.blank, "Layer " + image.getNewLayerCount());
    }

    /*
     Delete layer button deletes the selected layer and selects a new layer (or none if there are no layers)
      */
    @Override
    @FXML
    public void deleteLayerAction() {
        if (!layerViewList.isEmpty()) {
            int index = indexOfSelectedItem();

            image.deleteActiveLayer();
        }

    }

    private void deselectLayers() {
        for (Node n : layerViewList) {
            if (n.getClass().equals(LayerItemController.class)) {
                LayerItemController i = (LayerItemController) n;
                if (i.isSelected()) {
                    i.setDeselected();
                }
            }
        }
    }

    private void selectLayer(LayerItemController layer) {
        deselectLayers();
        layer.setSelected();
    }

    private int indexOfSelectedItem() {
        for (Node n : layerViewList) {
            if (n.getClass().equals(LayerItemController.class)) {
                LayerItemController i = (LayerItemController) n;

                if (i.isSelected()) {
                    return layerViewList.indexOf(i);
                }
            }
        }
        return -1;
    }

    // Listens to every layer for a selection or toggle action.
    @Override
    public void layerUpdate(PaintLayer layer, String id) {
        switch (id) {
            case "selectLayer":
                image.setActiveLayer(layer);
                deselectLayers();
                break;
            case "toggleLayer":
                image.toggleLayerVisible(layer);
                break;
            default:
                break;
        }
    }

    protected StackPane getListPane() {
        return frontPane;
    }

    private void selectActiveLayer() {
        for (Node n : layerViewList) {
            if (n.getClass().equals(LayerItemController.class)) {
                LayerItemController l1 = (LayerItemController) n;

                if (image.getActiveLayer().equals(l1.getLayer())) {
                    selectLayer(l1);
                    break;
                }
            }
        }
    }

    private void setLayerDragEvents(ILayerItemController lI) {
        AnchorPane layerItemPane = lI.getLayerItemPane();

        // Begin drag event when dragging a layer.
        layerItemPane.setOnDragDetected(e -> {
            layerItemPane.startFullDrag();
            layerItemPane.setCursor(Cursor.CLOSED_HAND);
            dragImage = lI.getSnapShot(1.25);
            frontPane.getChildren().add(dragImage);
            mouseY = e.getSceneY();
            dragImage.setManaged(false);
            dragImage.setY(mouseY - 80);

            e.consume();
        });

        // Shows the image of the dragged layer.
        layerItemPane.setOnMouseDragged(e -> {
            if (e.getSource().getClass().equals(LayerItemController.class)) {
                double deltaY = e.getSceneY() - mouseY;
                if (frontPane.getChildren().contains(dragImage)) {
                    dragImage.setTranslateY(dragImage.getTranslateY() + deltaY);
                }
                mouseY = e.getSceneY();
            }
        });

        // When mouse drag enters a layer, show highlighted panes (top or bottom of a layer) which indicates locations you can move the dragged layer to.
        layerItemPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
            if (!e.getGestureSource().equals(layerItemPane)) {
                if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                    LayerItemController l = (LayerItemController) e.getGestureSource();

                    if (layerViewList.indexOf(l) == layerViewList.indexOf(layerItemPane) + 1) {
                        lI.getTopBorderPane().setMouseTransparent(false);
                    }
                    else if (layerViewList.indexOf(l) == layerViewList.indexOf(layerItemPane) - 1) {
                        lI.getBottomBorderPane().setMouseTransparent(false);
                    } else {
                        lI.getTopBorderPane().setMouseTransparent(false);
                        lI.getBottomBorderPane().setMouseTransparent(false);
                    }
                }

            }
            e.consume();
        });

        // Hide highlighted panes when mouse drag exits layer.
        layerItemPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (!e.getGestureSource().equals(layerItemPane)) {
                if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                    lI.getBottomBorderPane().setMouseTransparent(true);
                    lI.getTopBorderPane().setMouseTransparent(true);
                }
            }
            e.consume();
        });

        // If mouse drag releases over a top highlighted pane, move the dragged layer to the location.
        lI.getTopBorderPane().addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                LayerItemController source = (LayerItemController) e.getGestureSource();

                image.moveLayerAbove(layerViewList.indexOf(layerItemPane), source.getLayer());
            }
            e.consume();
        });

        // If mouse drag releases over a bottom highlighted pane, move the dragged layer to the location.
        lI.getBottomBorderPane().addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                LayerItemController source = (LayerItemController) e.getGestureSource();

                image.moveLayerUnder(layerViewList.indexOf(layerItemPane), source.getLayer());
            }
            e.consume();
        });
    }

    @Override
    public Button getDeleteLayerButton(){
        return deleteLayerButton;
    }

}
