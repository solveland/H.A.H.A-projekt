import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class PaintController {


    @FXML
    private Canvas canvas;

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double size = 10;
            double x = e.getX();
            double y = e.getY();
            g.fillRect(x, y, size, size);
        });
    }

}
