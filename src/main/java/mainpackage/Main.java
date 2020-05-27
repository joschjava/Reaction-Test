package mainpackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        primaryStage.setTitle("Reaction Test");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.setScene(new Scene(root, 300, 300));
        controller.initialiseAfterLoading();
        primaryStage.show();
    }
}
