import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

public class PaintController {


    @FXML
    private Canvas canvas;
    @FXML
    private Button clearButton;

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double size = 10;
            double x = e.getX();
            double y = e.getY();
            g.fillRect(x, y, size, size);
        });

    }
@FXML
    public void clearCanvas () {
        canvas.getGraphicsContext2D().clearRect(0,0,600,600);
    }


}
