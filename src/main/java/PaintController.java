
import Services.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;

import javafx.scene.input.MouseButton;
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

public class PaintController implements ImageModelObserver {

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
    private ComboBox<String> shapeBox;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem item1;
    @FXML
    private Label label;

    private ImageModel image;
    private PaintView view;

    private File openedFile = null;

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }

    public void initialize() {
        image = new ImageModel(600, 600);
        view = new PaintView(600, 600);
        LayerListController lController = new LayerListController(image);
        image.addObserver(view);
        image.addObserver(lController);
        borderPane.setRight(lController.getListPane());
        contextMenu = new ContextMenu();
        item1 = new MenuItem("Deselect");

        image.addObserver(this);

        canvas.setImage(view.getImage());

        setImageDragEvent(canvas);
        setImagePressEvent(canvas);
        setImageReleaseEvent(canvas);

        stackPane.setOnMousePressed(e ->{
                    if(image.getActiveTool().equals(image.getZoomTool())) {
                        if (e.isAltDown())
                            zoom(false);
                        else
                        zoom(true);
                    }
        });


        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(e -> {
            sendColorState();
        });

        clearCanvas();

        //Toolbar
        sizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
        sizeSpinner.setEditable(true);
        sizeSpinner.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setSize(newvalue));
        opacitySlider.valueProperty().addListener((obs, oldvalue, newvalue) -> sendColorState());
        brushBar.setVisible(false);
        shapeBox.getItems().addAll("Circle", "Square");
        view.populateShapeComboBox(shapeBox);
        shapeBox.valueProperty().addListener((obs, oldvalue, newvalue) -> image.setToolShape(newvalue));

        sendColorState();

        setBrushTool();

        //Deselect selected area - rightclick
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                image.deselectArea();
            }
        });

        contextMenu.getItems().add(item1);


        canvas.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                if (image.getActiveLayer().hasSelection()) {
                    contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
                }
            }
        });

    }


    private void sendColorState() {
        Color javafxcolor = colorPicker.getValue();
        PaintColor color = new PaintColor(javafxcolor.getRed(), javafxcolor.getGreen(), javafxcolor.getBlue(), opacitySlider.getValue());
        image.setColor(color);
    }

    @FXML
    public void undoButton() {
        image.undo();
    }

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

    @FXML
    public void saveFile() {
        if (openedFile == null) {
            saveFileAs();
            return;
        }
        ServiceFactory.getSaveAndLoader().saveFile(openedFile, image.getLayerList());
    }

    @FXML
    public void clearCanvas() {
        image.clearLayer();
    }

    @FXML
    public void setPencil() {
        image.activatePencilTool();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
    }

    @FXML
    public void setFillTool() {

        image.activateFillTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
    }

    @FXML
    public void setEyedropperTool(){
        image.activateEyedropperTool();
        brushBar.setVisible(false);
    }

    @FXML public void setBrushTool(){
        image.activateBrushTool();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
    }

    @FXML
    public void setEraserTool() {

        image.activateEraserTool();
        brushBar.setVisible(true);
        opacitySlider.setVisible(false);
        opacityLabel.setVisible(false);
        opacitySlider.setValue(1);
    }

    @FXML
    public void setSelectTool() {
        image.activateSelectTool();
        brushBar.setVisible(false);
    }

    @FXML
    public void setZoomTool() {
        image.activateZoomTool();
        brushBar.setVisible(false);
    }

    @FXML
    public void setShapeTool() {
        image.activateShapeTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);

    }

    @FXML
    public void zoom(boolean setZoom) {
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

    @FXML
    public void zoomIn() {
        zoom(true);
    }

    @FXML
    public void zoomOut() {
        zoom(false);
    }

    public void setZoomPercent(double percent) {
        canvas.setScaleY(percent);
        canvas.setScaleX(percent);
    }

    @FXML
    public void zoomFifty() {
        setZoomPercent(0.5);
    }

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

    /////////////////////////////////////////////////////////////////////

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
        });
    }

}
