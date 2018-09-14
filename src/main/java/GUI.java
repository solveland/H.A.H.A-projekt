import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.awt.*;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    javafx.scene.control.Button button;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PhotoChop");

        button = new Button();
        button.setText("Hej");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout);

        primaryStage.setMaximized(true);

        primaryStage.setScene(scene);

        primaryStage.show();



    }
}
