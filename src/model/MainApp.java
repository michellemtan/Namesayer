package model;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.redundant.Menu;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//
//        Scene scene = SetUp.getInstance().startMenu; // load menu scene
//        primaryStage.setTitle("Name Sayer");
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();

        Parent root = Menu.STARTMENU.loader().load();
        primaryStage.setTitle("Name Sayer");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
