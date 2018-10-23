import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import model.ImageModel;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Paint.fxml"));
        IPaintController pc = new PaintController();
        loader.setController(pc);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setMaximized(true);


        // Stylesheet CSS  REMOVE
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toExternalForm());
        stage.setScene(scene);


        stage.setTitle("Paint++");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
