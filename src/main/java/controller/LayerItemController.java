package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import model.PaintLayer;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/*
AUTHOR: Anthony Tao
RESPONSIBILITY: Keeping track of the items representing a layer in the layer list of the program
USED BY: LayerListController
USES: PaintLayer
*/

/**
 * LayerItemController handles GUI for layer items in the layer pane.
 */

public class LayerItemController extends AnchorPane implements ILayerItemController {

    @FXML
    private AnchorPane layerItemPane;

    @FXML
    private Label layerName;


    @FXML
    private TextField editableText;

    @FXML
    private Pane bottomBorderPane;

    @FXML
    private Pane topBorderPane;

    private PaintLayer layer;
    private List<LayerItemObserver> observers;
    private Boolean selected;

    private String selectedColor;
    private String originalColor;
    private String borderColor;

    public LayerItemController(String name, PaintLayer layer) {
        this.layer = layer;
        observers = new ArrayList<>();
        selected = false;

        this.setCursor(Cursor.HAND);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LayerLook.fxml"));
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
        borderColor = "-fx-border-color: white";

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
            if (e.getCode().equals(KeyCode.ENTER)) {
                layerItemPane.requestFocus();
            }
        });

        layerName.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
                editableText.setVisible(true);
                editableText.requestFocus();
            }
        });

        setBorderEvents();
    }

    @Override
    @FXML
    public void onSelectLayer() {
        for (LayerItemObserver o : observers) {
            o.layerUpdate(layer, "selectLayer");
        }

        setSelected();
    }

    @Override
    @FXML
    public void onToggleVisible() {
        for (LayerItemObserver o : observers) {
            o.layerUpdate(layer, "toggleLayer");
        }
    }

    @Override
    public void addObserver(LayerItemObserver observer) {
        observers.add(observer);
    }

    @Override
    public Boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected() {
        this.selected = true;
        layerItemPane.setStyle(selectedColor);
    }

    @Override
    public void setDeselected() {
        this.selected = false;
        layerItemPane.setStyle(originalColor);
    }

    @Override
    public PaintLayer getLayer() {
        return layer;
    }

    @Override
    public Pane getTopBorderPane() {
        return topBorderPane;
    }

    @Override
    public Pane getBottomBorderPane() {
        return bottomBorderPane;
    }

    private void setBorder(String side) {
        switch (side) {
            case "Top":
                topBorderPane.setStyle("-fx-border-width: 1 0 0 0;" + borderColor);
                break;
            case "Bottom":
                bottomBorderPane.setStyle("-fx-border-width: 0 0 1 0;" + borderColor);
                break;
            case "noBorder":
                topBorderPane.setStyle("-fx-border-width: 0 0 0 0;");
                bottomBorderPane.setStyle("-fx-border-width: 0 0 0 0;");
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
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                setBorder("Top");
                e.consume();
            }
        });

        topBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                setBorder("noBorder");
                e.consume();
            }
        });

        bottomBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                setBorder("Bottom");
                e.consume();
            }
        });

        bottomBorderPane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, e -> {
            if (e.getGestureSource().getClass().equals(LayerItemController.class)) {
                setBorder("noBorder");
                e.consume();
            }
        });
    }

    @Override
    public ImageView getSnapShot(double scale) {
        Bounds bounds = layerItemPane.getLayoutBounds();

        WritableImage image = new WritableImage(
                (int) Math.round(bounds.getWidth() * scale),
                (int) Math.round(bounds.getHeight() * scale));

        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(javafx.scene.transform.Transform.scale(scale, scale));

        ImageView view = new ImageView(layerItemPane.snapshot(spa, image));
        view.setFitWidth(bounds.getWidth());
        view.setFitHeight(bounds.getHeight());
        view.setOpacity(0.3);
        view.setMouseTransparent(true);

        return view;
    }

    @Override
    public AnchorPane getLayerItemPane() {
        return layerItemPane;
    }

}
