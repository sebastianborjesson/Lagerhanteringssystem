package system;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainFlow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root,800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lagerhanteringssystem");
        primaryStage.show();
    }
}
