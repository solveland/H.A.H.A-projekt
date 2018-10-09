

import model.ImageModel;
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
import model.utils.PaintColor;
import view.PaintView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PaintController {

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
    private ImageModel image;
    private PaintView view;
    private LayerListController lController;

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }

    public void initialize() {
        image = new ImageModel(600, 600);
        view = new PaintView(600, 600);
        lController = new LayerListController(image);
        image.addObserver(view);
        image.addObserver(lController);
        borderPane.setRight(lController.getLayerPane());


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
        });

        canvas.setOnMousePressed(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            image.onPress(x, y);
        });

        /* Tar bort denna så länge eftersom jag flyttar tillbaka allt till modellen
        stackPane.setOnMousePressed(e ->{
                    if(activeTool == zoomTool) {
                        if (e.isAltDown())
                            zoom(false);
                        else
                        zoom(true);
                    }
        });*/


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

    }

    private void sendColorState(){
        Color javafxcolor = colorPicker.getValue();
        PaintColor color = new PaintColor(javafxcolor.getRed(),javafxcolor.getGreen(),javafxcolor.getBlue(),opacitySlider.getValue());
        image.setColor(color);
    }

    @FXML
    public void undoButton() {
        image.undo();
    }

    @FXML
    public void openFile() {
        //Mycket av denna funktion måste flyttas till andra delar. pixlarna ska ändras i modellen, inte här t.ex.
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        BufferedImage loadedImage;
        try {
            loadedImage = ImageIO.read(file);
        } catch (java.io.IOException e) {
            //Todo: Handle this exception
            return;
        }
        view.setSize(loadedImage.getWidth(), loadedImage.getHeight());
        lController.getLayerList().clear();
        image.deleteAllLayers();
        image.setImageSize(loadedImage.getWidth(), loadedImage.getHeight());
        canvas.setImage(view.getImage());
        canvas.setFitHeight(loadedImage.getHeight());
        canvas.setFitWidth(loadedImage.getWidth());
        image.createLayer(PaintColor.blank, "Background");
        PaintLayer pl = image.getActiveLayer();
        for (int x = 0; x < loadedImage.getWidth(); x++) {
            for (int y = 0; y < loadedImage.getHeight(); y++) {
                pl.setPixel(x, y, new PaintColor(loadedImage.getRGB(x, y)));
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

        image.activatePencilTool();
        opacitySlider.setVisible(true);
        opacityLabel.setVisible(true);
        brushBar.setVisible(true);
    }


    ////////////////////////////////////////// ZOOM ///////////////////////////////////////////////

    @FXML
    public void setFillTool() {

        image.activateFillTool();
        brushBar.setVisible(false);
        opacitySlider.setValue(1);
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
    public void setSelectTool(){
        image.activateSelectTool();
    }

    @FXML
    public void setZoomTool() {
        image.activateZoomTool();
        brushBar.setVisible(false);
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

        /*
        double fy = (image.getZoomScaleY() / image.getOldZoomScaleY()) - 1;
        double fx = (image.getZoomScaleX() / image.getOldZoomScaleX()) - 1;

        double dx = (scrollPane.getLayoutX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
        double dy = (scrollPane.getLayoutY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));
        */

        canvas.setScaleY(scaleY);
        canvas.setScaleX(scaleX);
        /*
        canvas.setTranslateY(canvas.getTranslateY()-fy*dy);
        canvas.setTranslateX(canvas.getTranslateX()-fx*dx);
        */
    }

    @FXML
    public void zoomIn() {
        zoom(true);
    }

    @FXML
    public void zoomOut() {
        zoom(false);
    }

    private void setZoomPercent(double percent) {
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



    /////////////////////////////////////////////////////////////////////

}
