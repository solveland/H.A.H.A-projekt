import Model.ImageModel;
import Model.PaintLayer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.List;

public class PaintController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView canvas;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ColorPicker colorPicker;

    private ImageModel image;

    private PaintView view;

    private LayerListController lController;

    public void initialize() {
        image = new ImageModel(600, 600);
        view = new PaintView(600, 600);
        lController = new LayerListController(image);
        image.addObserver(view);
        image.addObserver(lController);
        borderPane.setRight(lController.getLayerPane());

        image.createLayer(0xFFFFFFFF, "Background"); // Should be moved to the method where we initialize a new project.

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
        lController.getLayerList().clear();
        image.deleteAllLayers();
        image.setImageSize(loadedImage.getWidth(),loadedImage.getHeight());
        canvas.setImage(view.getImage());
        canvas.setFitHeight(loadedImage.getHeight());
        canvas.setFitWidth(loadedImage.getWidth());
        image.createLayer(0,"Background");
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
    }

    @FXML
    public void setFillTool() {
        image.setFillTool();
    }

    @FXML
    public void setEraserTool() {
        image.setEraserTool();
    }

    @FXML
    public void setZoomTool() {
        image.setZoomTool();
        lController.getLayerList().clear();
        image.deleteAllLayers();
        image.createLayer(0xFF00FF00,"Background");
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
