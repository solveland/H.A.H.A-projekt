import javafx.event.Event;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PaintController {


    @FXML
    private ImageView canvas;

    @FXML
    private ColorPicker colorPicker;


    private ImageModel image;



    public void initialize() {
        image = new ImageModel(600, 600);

        canvas.setImage(image.getImage());
        canvas.setOnMouseDragged(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            image.onDrag(x, y);
        });


        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(e -> {
            Color temp = colorPicker.getValue();
            image.updateColor(temp);
        });
        clearCanvas();
    }



    //Temporär testfunktion -> låter den vara image.getimage()....
@FXML
    public void clearCanvas () {
        int[] buffer = new int[600*600];
        for(int i = 0; i < 600*600;i++){
            buffer[i] = 0xFFFFFFFF;
        }
        image.getImage().getPixelWriter().setPixels(0,0,600,600, PixelFormat.getIntArgbInstance(),buffer,0,600);
    }

    private void setActiveTool(AbstractTool tool){
        image.setActiveTool(tool);
    }


}
