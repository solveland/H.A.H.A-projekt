import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.PaintLayer;

public interface ILayerItemController {

    @FXML
    void onSelectLayer();
    @FXML
    void onToggleVisible();

    Boolean isSelected();
    void setSelected();
    void setDeselected();
    PaintLayer getLayer();
    Pane getTopBorderPane();
    Pane getBottomBorderPane();
    ImageView getSnapShot(double scale);
    void addObserver(LayerItemObserver observer);
    AnchorPane getLayerItemPane();
}
