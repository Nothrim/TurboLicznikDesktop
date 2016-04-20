package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("core.fxml"));
            primaryStage.setTitle("Turbo Licznik");
            primaryStage.setScene(new Scene(root, 416, 700));
            primaryStage.getScene().getStylesheets().add("main.css");
            primaryStage.show();
        }
        catch (Exception e){e.printStackTrace();}
    }


    public static void main(String[] args) {
        launch(args);
    }
}
