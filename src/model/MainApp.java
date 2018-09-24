package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.resources.SetUp;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene scene = SetUp.getInstance().startMenu; // load menu scene
        primaryStage.setTitle("Name Sayer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        //xmlns="http://javafx.com/javafx" xmlns:fx="http://javafx.com/fxml"
    }
}
