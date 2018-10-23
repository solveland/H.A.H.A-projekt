import javafx.fxml.FXML;
import javafx.scene.control.Button;

public interface ILayerListController {

    @FXML
    void newLayerAction();
    @FXML
    void deleteLayerAction();
    @FXML
    Button getDeleteLayerButton();
}
