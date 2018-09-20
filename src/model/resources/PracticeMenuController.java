package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PracticeMenuController {

    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private CheckBox randomiseCheckBox;

    private Parent parentRoot;

    //TODO: back button take back to database menu, not start menu
    @FXML
    void backButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void randomiseClicked() {

    }

    @FXML
    void startButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().playMenu;
        Stage window = (Stage) startButton.getScene().getWindow();
        window.setScene(scene);
    }
}
