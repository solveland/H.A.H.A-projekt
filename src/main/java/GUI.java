import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Paint.fxml"))));
        stage.setMaximized(true);


        // Stylesheet CSS
        Parent root = FXMLLoader.load(getClass().getResource("Paint.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Stylesheet.css").toExternalForm());
        stage.setScene(scene);


        stage.setTitle("Paint");
        stage.show();
    }









    public static void main(String[] args) {
        launch(args);
    }
}
