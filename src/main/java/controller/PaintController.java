package controller;

import services.ServiceFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.*;

import model.ImageModel;
import model.ImageModelObserver;
import model.PaintLayer;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.pixel.PaintColor;
import model.PaintOverlay;
import view.PaintView;

import java.io.File;
import java.util.List;

/*
AUTHOR: Anthony Tao, Henrik Tao, August Sölveland, Hampus Ekberg
RESPONSIBILITY: The main controller class
USED BY:
USES: ImageModel, PaintView
 */

public class PaintController implements ImageModelObserver, IPaintController {

    private static final double MAX_SCALE = 15.0d;
    private static final double MIN_SCALE = 0.1d;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView canvas;
    @FXML
    private StackPane stackPane;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private FlowPane layerView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Spinner<Integer> sizeSpinner;
    @FXML
    private ToolBar brushBar;
    @FXML
    private Slider opacitySlider;
    @FXML
    private Label opacityLabel;
    @FXML
    private Label hardnessLabel;
    @FXML
    private ComboBox<String> shapeBox;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem deselectItem;
    @FXML
    private Label label;
    @FXML
    private Slider hardnessSlider;

    //shapeToolbar
    @FXML
    private ToolBar shapeBar;

    @FXML
    private ComboBox<String> sizeBox1;

    @FXML
    private ComboBox<String> shapeBox1;


    //BUTTONS
    @FXML
    private ToggleButton brushButton;
    @FXML
    private ToggleButton shapeButton;
    @FXML
    private ToggleButton eyedropperButton;
    @FXML
    private ToggleButton eraserButton;
    @FXML
    private Button undoButton;
    @FXML
    private ToggleButton fillButton;
    @FXML
    private ToggleButton pencilButton;
    @FXML
    private ToggleButton zoomButton;
    @FXML
    private ToggleButton selectButton;

    private Boolean mouseDown;
    private ImageModel image;
    private PaintView view;
    private File openedFile = null;

    public void initialize() {
        view = new PaintView(600, 600);
        image = new ImageModel(600, 600);
        LayerListController lController = new LayerListController(image);
        image.addObserver(view);
        image.addObserver(lController);
        image.updateRenderedImage();
        borderPane.setRight(lController.getListPane());
        contextMenu = new ContextMenu();
        deselectItem = new MenuItem("Deselect");

        ShortcutController sC = new ShortcutController(this, lController);

        mouseDown = false;

        image.addObserver(this);

        canvas.setImage(view.getImage());

        setImageDragEvent(canvas);
        setImagePressEvent(canvas);
        setImageReleaseEvent(canvas);

        stackPane.setOnMousePressed(e -> {
            if (image.getActiveTool().equals(image.getZoomTool())) {
                if (e.isAltDown()){
                    zoom(false);
                }

                else {
                    zoom(true);
                }

            }
        });

        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(e -> {
            sendColorState();
        });


        //brushBar
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
        sizeSpinner.setEditable(true);
        sizeSpinner.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setSize(newvalue));
        opacitySlider.valueProperty().addListener((obs, oldvalue, newvalue) -> sendColorState());
        brushBar.setVisible(false);
        shapeBox.getItems().addAll("Circle", "Square");
        shapeBox.getSelectionModel().selectFirst();
        view.populateShapeComboBox(shapeBox);
        shapeBox.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setToolShape(newvalue));
        hardnessSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            image.setHardness(newValue.doubleValue());
        });
        hardnessSlider.setValue(0.5);

        //shapeBar
        shapeBox1.getItems().addAll("Line", "Rectangle", "Triangle", "Ellipse");
        sizeBox1.getItems().addAll("Small", "Medium", "Large");
        view.populateSizeComboBox(sizeBox1);
        view.populateShapeComboBox1(shapeBox1);
        shapeBox1.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setToolShape(newvalue));
        sizeBox1.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setShapeSize(newvalue));
        shapeBar.setVisible(false);

        sendColorState();

        // Left Toolbar toggle group
        ToggleGroup tg = new ToggleGroup();
        pencilButton.setToggleGroup(tg);
        brushButton.setToggleGroup(tg);
        eraserButton.setToggleGroup(tg);
        shapeButton.setToggleGroup(tg);
        fillButton.setToggleGroup(tg);
        zoomButton.setToggleGroup(tg);
        selectButton.setToggleGroup(tg);
        eyedropperButton.setToggleGroup(tg);

        sendColorState();
        setBrushTool();

        //Deselect selected area - right click
        deselectItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                image.deselectArea();
            }
        });

        contextMenu.getItems().add(deselectItem);


        canvas.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if (image.getActiveLayer().hasSelection()) {
                    contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }

    private static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    @Override
    @FXML
    public void exitApplication() {
        Platform.exit();
    }

    private void sendColorState() {
        Color javafxcolor = colorPicker.getValue();
        PaintColor color = new PaintColor(javafxcolor.getRed(), javafxcolor.getGreen(), javafxcolor.getBlue(), opacitySlider.getValue());
        image.setColor(color);
    }

    @Override
    @FXML
    public void undoAction() {
        image.undo();
    }

    @Override
    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Image (*.jpg)", "*.jpg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bitmap Image (*.bmp)", "*.bmp"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG image (*.png)", "*.png"));
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        List<PaintLayer> layerList = ServiceFactory.getSaveAndLoader().openFile(file);
        if (layerList == null || layerList.isEmpty()) {
            return;
        }
        openedFile = file;
        loadImage(layerList);

    }

    private void loadImage(List<PaintLayer> layerList) {
        int width = layerList.get(0).getWidth();
        int height = layerList.get(0).getHeight();
        view.setSize(width, height);
        canvas.setImage(view.getImage());
        canvas.setFitHeight(height);
        canvas.setFitWidth(width);
        image.loadImage(layerList);
    }

    @Override
    @FXML
    public void saveFileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG image (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        ServiceFactory.getSaveAndLoader().saveFile(file, image.getLayerList());
        openedFile = file;
    }

    @Override
    @FXML
    public void saveFile() {
        if (openedFile == null) {
            saveFileAs();
            return;
        }
        ServiceFactory.getSaveAndLoader().saveFile(openedFile, image.getLayerList());
    }

    @Override
    @FXML
    public void clearCanvas() {
        image.clearLayer();
    }

    @FXML
    public void setPencil() {
        shapeBar.setVisible(false);
        image.activatePencilTool();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
        hardnessSlider.setVisible(false);
        hardnessLabel.setVisible(false);
        pencilButton.setSelected(true);
    }

    @Override
    @FXML
    public void setFillTool() {
        shapeBar.setVisible(false);
        image.activateFillTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
        fillButton.setSelected(true);
    }

    @Override
    @FXML
    public void setEyedropperTool(){
        shapeBar.setVisible(false);
        image.activateEyedropperTool();
        brushBar.setVisible(false);
        eyedropperButton.setSelected(true);
    }

    @Override
    @FXML
    public void setBrushTool(){
        shapeBar.setVisible(false);
        image.activateBrushTool();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
        hardnessSlider.setVisible(true);
        hardnessLabel.setVisible(true);
        brushButton.setSelected(true);
    }

    @Override
    @FXML
    public void setEraserTool() {
        shapeBar.setVisible(false);
        image.activateEraserTool();
        brushBar.setVisible(true);
        opacitySlider.setVisible(false);
        opacityLabel.setVisible(false);
        opacitySlider.setValue(1);
        hardnessSlider.setVisible(false);
        hardnessLabel.setVisible(false);
        eraserButton.setSelected(true);
    }

    @Override
    @FXML
    public void setSelectTool() {
        shapeBar.setVisible(false);
        image.activateSelectTool();
        brushBar.setVisible(false);
        selectButton.setSelected(true);
    }

    @Override
    @FXML
    public void setZoomTool() {
        shapeBar.setVisible(false);
        image.activateZoomTool();
        brushBar.setVisible(false);
        zoomButton.setSelected(true);
    }

    @Override
    @FXML
    public void setShapeTool() {
        image.activateShapeTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
        shapeButton.setSelected(true);
        shapeBar.setVisible(true);
    }

    @Override
    @FXML
    public void zoom(Boolean setZoom) {
        double delta = 1.05;
        image.setZoomScaleX(canvas.getScaleX());
        image.setZoomScaleY(canvas.getScaleY());
        image.setOldZoomScaleX(image.getZoomScaleX());
        image.setOldZoomScaleY(image.getZoomScaleY());

        double scaleY = image.getZoomScaleY();
        double scaleX = image.getZoomScaleX();

        if (setZoom) {
            scaleY *= delta;
            scaleX *= delta;
        } else {
            scaleY /= delta;
            scaleX /= delta;
        }
        scaleY = clamp(scaleY, MIN_SCALE, MAX_SCALE);
        scaleX = clamp(scaleX, MIN_SCALE, MAX_SCALE);

        canvas.setScaleY(scaleY);
        canvas.setScaleX(scaleX);
    }

    @Override
    @FXML
    public void zoomIn() {
        zoom(true);
    }

    @Override
    @FXML
    public void zoomOut() {
        zoom(false);
    }

    private void setZoomPercent(double percent) {
        canvas.setScaleY(percent);
        canvas.setScaleX(percent);
    }

    @Override
    @FXML
    public void zoomFifty() {
        setZoomPercent(0.5);
    }

    @Override
    @FXML
    public void zoomHundred() {
        setZoomPercent(1);
    }

    @FXML
    public void zoomTwoHundred() {
        setZoomPercent(2);
    }

    @Override
    public void notifyObservers(PaintLayer layer, int minX, int maxX, int minY, int maxY, List<PaintLayer> layerList, PaintColor color, PaintOverlay overlay, String id){
        if(id.equals("colorPickerUpdate")){
            Color c = new Color(color.getRedRatio(),color.getGreenRatio(), color.getBlueRatio(), color.getAlphaRatio());
            colorPicker.setValue(c);
        }
    }

    private void setImageDragEvent (ImageView imageView) {
        imageView.setOnMouseDragged(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                return;
            }
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            image.onDrag(x, y);
        });
    }

    private void setImageReleaseEvent (ImageView imageView) {
        imageView.setOnMouseReleased(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                return;
            }
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            image.onRelease(x, y);
            mouseDown = false;
        });
    }

    private void setImagePressEvent (ImageView imageView) {
        imageView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                return;
            }
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            image.onPress(x, y);
            mouseDown = true;
        });
    }

    @Override
    public ToggleButton getPencilButton() {
        return pencilButton;
    }

    @Override
    public ToggleButton getEraserButton() {
        return eraserButton;
    }

    @Override
    public ToggleButton getEyedropperButton() {
        return eyedropperButton;
    }

    @Override
    public ToggleButton getShapeButton() {
        return shapeButton;
    }

    @Override
    public ToggleButton getBrushButton() {
        return brushButton;
    }

    @Override
    public ToggleButton getFillButton() {
        return fillButton;
    }

    @Override
    public ToggleButton getZoomButton() {
        return zoomButton;
    }

    @Override
    public Button getUndoButton() {
        return undoButton;
    }

    @Override
    public ToggleButton getSelectButton() {
        return selectButton;
    }

    @Override
    public Boolean getMouseDown() {
        return mouseDown;
    }

}
