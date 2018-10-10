import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import Model.PaintLayer;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayerItem extends AnchorPane implements Serializable {

    @FXML
    private AnchorPane layerItemPane;

    @FXML
    private Label layerName;

    @FXML
    private CheckBox layerVisible;

    @FXML
    private TextField editableText;

    @FXML
    private Pane bottomBorderPane;

    @FXML
    private Pane topBorderPane;

    private PaintLayer layer;
    private List<LayerObserver> observers;
    private boolean selected;

    private String selectedColor;
    private String originalColor;
    private String borderUpColor;
    private String borderDownColor;

    public LayerItem(String name, PaintLayer layer)
    {
        this.layer = layer;
        observers = new ArrayList<>();

        this.setCursor(Cursor.HAND);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LayerLook.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        layerName.setText(name);
        selectedColor = "-fx-background-color: -layer-highlight;";
        originalColor = "-fx-background-color: -layer-background;";
        borderUpColor = "-fx-border-color: white -dark-black -dark-black -dark-black;";
        borderDownColor = "-fx-border-color: -dark-black -dark-black white -dark-black;";

        editableText.focusedProperty().addListener(new ChangeListener<Boolean>() {
           @Override
           public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
               if (newPropertyValue) {
                   editableText.setText(layerName.getText());
               } else {
                   editableText.setVisible(false);
                   layerName.setText(editableText.getText());
               }
           }
        });

        editableText.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                layerItemPane.requestFocus();
        });

        layerName.setOnMouseClicked(e -> {
            if(e.getButton().equals(MouseButton.PRIMARY)){
                if(e.getClickCount() == 2){
                    editableText.setVisible(true);
                    editableText.requestFocus();
                }
            }
        });

        setBorderEvents();
    }

    @FXML
    public void onSelectLayer() {
        for (LayerObserver o : observers)
        {
            o.layerUpdate(layer, "selectLayer");
        }

        setSelected();
    }

    @FXML
    public void onToggleVisible() {
        for (LayerObserver o : observers) {
            o.layerUpdate(layer, "toggleLayer");
        }
    }

    private void onDragDetected() {
        for (LayerObserver o : observers) {
            o.layerUpdate(layer, "dragDetected");
        }
    }

    private void onDragDone() {
        for (LayerObserver o : observers) {
            o.layerUpdate(layer, "dragDone");
        }
    }

    public void addObserver(LayerObserver observer) {
        observers.add(observer);
    }

    public boolean isSelected() { return selected; }

    public void setSelected() {
        this.selected = true;
        layerItemPane.setStyle(selectedColor);
    }

    public void setDeselected() {
        this.selected = false;
        layerItemPane.setStyle(originalColor);
    }

    public String getName(){
        return layerName.getText();
    }

    public PaintLayer getLayer() {
        return layer;
    }

    public AnchorPane getLayerItemPane() {
        return layerItemPane;
    }

    public Pane getTopBorderPane() {
        return topBorderPane;
    }

    public Pane getBottomBorderPane() {
        return bottomBorderPane;
    }

    private void setBorder(String side) {
        switch (side) {
            case "Up":
                topBorderPane.setStyle("-fx-border-width: 1 0 0 0;" +  borderUpColor);
                break;
            case "Down":
                bottomBorderPane.setStyle("-fx-border-width: 0 0 1 0;" + borderDownColor);
                break;
            case "Original":
                topBorderPane.setStyle("-fx-border-width: 0;");
                bottomBorderPane.setStyle("-fx-border-width: 0;");
                break;
            default:
                break;
        }
    }

    /*
        Show bottom/top border highlight when a layer is dragged to another layer.
     */
    private void setBorderEvents() {
        topBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItem.class)) {
                setBorder("Up");
                e.consume();
            }
        });

        topBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItem.class)) {
                setBorder("Original");
                e.consume();
            }
        });

        bottomBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItem.class)) {
                setBorder("Down");
                e.consume();
            }
        });

        bottomBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItem.class)) {
                setBorder("Original");
                e.consume();
            }
        });
    }

    public ImageView imageCopy() {
        Bounds bounds = layerItemPane.getLayoutBounds();

        WritableImage image = new WritableImage(
                (int) Math.round(bounds.getWidth() * 1.25),
                (int) Math.round(bounds.getHeight() * 1.25));

        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(1.25, 1.25));

        ImageView view = new ImageView(layerItemPane.snapshot(spa, image));
        view.setFitWidth(bounds.getWidth());
        view.setFitHeight(bounds.getHeight());
        view.setOpacity(0.3);
        view.setMouseTransparent(true);

        return view;
    }

}
