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



    private WritableImage image;


    int size = 15;
    int color = 0xFF000000;

    AbstractTool activeTool;
    PencilTool pencilTool;

    public void initialize() {
        image = new WritableImage(600, 600);
        pencilTool = new PencilTool(size);
        setActiveTool(pencilTool);
        canvas.setImage(image);
        canvas.setOnMouseDragged(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            activeTool.onDrag(x, y, image);
        });


        //Color Palette
        colorPicker.setValue(Color.BLACK);

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                Color temp = colorPicker.getValue();
                color = 0xFF000000 | ((int)(temp.getRed() * 255) << 16) | ((int)(temp.getGreen() * 255) << 8) | ((int)(temp.getBlue() * 255));
                if(activeTool instanceof ISizeAndColor) {
                    ((ISizeAndColor) activeTool).updateSizeAndColor(size, color);
                }
            }
        });
        clearCanvas();
    }



    //Temporär testfunktion
@FXML
    public void clearCanvas () {
        int[] buffer = new int[600*600];
        for(int i = 0; i < 600*600;i++){
            buffer[i] = 0xFFFFFFFF;
        }
        image.getPixelWriter().setPixels(0,0,600,600, PixelFormat.getIntArgbInstance(),buffer,0,600);
    }

    private void setActiveTool(AbstractTool tool){
        activeTool = tool;
        if(tool instanceof ISizeAndColor){
            ((ISizeAndColor) tool).updateSizeAndColor(size,color);
        }
    }


}