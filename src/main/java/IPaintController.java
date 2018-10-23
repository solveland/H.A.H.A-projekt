import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public interface IPaintController {

    void initialize();

    @FXML
    void exitApplication();
    @FXML
    void undoAction();
    @FXML
    void openFile();
    @FXML
    void saveFileAs();
    @FXML
    void saveFile();
    @FXML
    void clearCanvas();
    @FXML
    void setFillTool();
    @FXML
    void setEyedropperTool();
    @FXML
    void setBrushTool();
    @FXML
    void setEraserTool();
    @FXML
    void setSelectTool();
    @FXML
    void setZoomTool();
    @FXML
    void setShapeTool();
    @FXML
    void zoom(Boolean setZoom);
    @FXML
    void zoomIn();
    @FXML
    void zoomOut();
    @FXML
    void zoomFifty();
    @FXML
    void zoomHundred();
    @FXML
    void zoomTwoHundred();

    ToggleButton getPencilButton();
    ToggleButton getEraserButton();
    ToggleButton getEyedropperButton();
    ToggleButton getShapeButton();
    ToggleButton getBrushButton();
    ToggleButton getFillButton();
    ToggleButton getZoomButton();
    Button getUndoButton();
    ToggleButton getSelectButton();
    Boolean getMouseDown();
}
