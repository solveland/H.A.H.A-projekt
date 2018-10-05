import Model.ImageModel;
import Model.ModelObserver;
import Model.PaintLayer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LayerListController extends StackPane implements ModelObserver, LayerObserver {

    @FXML
    private StackPane layerPane;

    @FXML
    private FlowPane layerView;

    private List<Node> layerViewList;

    private ImageModel image;

    public LayerListController(ImageModel image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LayerList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.image = image;
        layerViewList = layerView.getChildren();

    }

    @Override
    public void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, String id) {
        if (id.equals("layerUpdate")) {
            if (!layerList.isEmpty()) {
                List<Node> tempList = new ArrayList<>();
                tempList.addAll(layerViewList);
                layerViewList.clear();

                for (int i = 0; i < layerList.size(); i++) {
                    PaintLayer p = layerList.get(i);

                    if (tempList.isEmpty()) {
                        createLayerItem(p, p.getName(), i);
                        continue;
                    }

                    boolean itemExists = false;
                    for (Node n : tempList) {

                        if (n.getClass().equals(LayerItem.class)) {
                            LayerItem l1 = (LayerItem) n;

                            if (p.equals(l1.getLayer())) {
                                layerViewList.add(i, l1);
                                itemExists = true;
                                break;
                            }
                        }
                    }
                    if (!itemExists) {
                        createLayerItem(p, p.getName(), i);
                    }
                }

                selectActiveLayer();

            } else {
                if (!layerViewList.isEmpty())
                    layerViewList.clear();
            }
        }
    }

    private void createLayer(int bgColor, String name) {
        int index = 0;

        if (!layerViewList.isEmpty())
            index = indexOfSelectedItem();

        image.createLayer(bgColor, name); // Create the layer in the model
    }

    // Used for creating a layer item for an existing layer in the model.
    private void createLayerItem(PaintLayer layer, String layerName, int index) {
        LayerItem layerItem = new LayerItem(layerName, layer);
        layerItem.addObserver(this);

        layerViewList.add(index, layerItem);
    }

    // New layer button creates a transparent layer with a unique name
    @FXML
    public void newLayerAction() {
        createLayer(0, "Layer " + image.getNewLayerCount());
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
            if (n.getClass().equals(LayerItem.class)) {
                LayerItem i = (LayerItem) n;

                if (i.isSelected()) {
                    return layerViewList.indexOf(i);
                }
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

    public StackPane getLayerPane() {
        return layerPane;
    }

    public List<Node> getLayerList() {
        return layerViewList;
    }

    private void selectActiveLayer() {
        for (Node n : layerViewList) {
            if (n.getClass().equals(LayerItem.class)) {
                LayerItem l1 = (LayerItem) n;

                if (image.getActiveLayer().equals(l1.getLayer())) {
                    selectLayer(l1);
                    break;
                }
            }
        }
    }

}
