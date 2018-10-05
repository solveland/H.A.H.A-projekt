import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import Model.PaintLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LayerItem extends AnchorPane {

    @FXML
    AnchorPane background;

    @FXML
    private TextField layerName;

    @FXML
    private CheckBox layerVisible;

    private PaintLayer layer;
    private List<LayerObserver> observers;
    private boolean selected;
    private String selectedColor;
    private String originalColor;

    public LayerItem(String name, PaintLayer layer)
    {
        this.layer = layer;
        observers = new ArrayList<>();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LayerLook.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        layerName.setText(name);
        selectedColor = "-fx-background-color: -layer-highlight";
        originalColor = "-fx-background-color: -layer-background";
    }

    @FXML
    public void selectLayer() {
        for (LayerObserver o : observers)
        {
            o.selectLayerUpdate(layer);
        }

        setSelected();
    }

    @FXML
    public void toggleLayerVisible () {
        for (LayerObserver o : observers) {
            o.visibleLayerToggle(layer);
        }
    }

    public void addObserver(LayerObserver observer) {
        observers.add(observer);
    }

    public boolean isSelected() { return selected; }

    public void setSelected() {
        this.selected = true;
        background.setStyle(selectedColor);
    }

    public void setDeselected() {
        this.selected = false;
        background.setStyle(originalColor);
    }

    public String getName(){
        return layerName.getText();
    }

    public PaintLayer getLayer() {
        return layer;
    }
}
