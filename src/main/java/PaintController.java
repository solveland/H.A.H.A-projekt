import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PaintController {


    @FXML
    private ImageView canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button bucketToolButton;


    private ImageModel image;

    private PaintView view;

    public void initialize() {
        image = new ImageModel(600, 600);
        view = new PaintView(600, 600);
        image.addObserver(view);

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
            image.onRelease(x, y);}
            );

        canvas.setOnMousePressed(e -> {
            int x = (int) Math.floor(e.getX());
            int y = (int) Math.floor(e.getY());
            if (y >= canvas.getFitHeight() || y < 0 || x >= canvas.getFitWidth() || x < 0) {
                return;
            }
            image.onPress(x, y);}
        );


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
        //view.getImage().getPixelWriter().setPixels(0,0,600,600, PixelFormat.getIntArgbInstance(),buffer,0,600);
        image.clearLayer();
    }


    private void setActiveTool(AbstractPaintTool tool){
        image.setActiveTool(tool);
    }

    @FXML
    public void setPencil (){
        image.setPencil();
    }

    @FXML
    public void setFillTool (){
        image.setFillTool();
    }

    @FXML
    public void setEraserTool(){image.setEraserTool();}

}
