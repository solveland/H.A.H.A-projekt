import controller.IPaintController;
import controller.PaintController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Paint.fxml"));
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
