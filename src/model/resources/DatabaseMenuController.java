package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class DatabaseMenuController {

    @FXML private Label databaseName; //TODO: figure out how to make this the dbName from MMC class
    @FXML private Button backBtn;

    public void backBtnPressed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        backBtn.getScene().setRoot(root);
    }

}
