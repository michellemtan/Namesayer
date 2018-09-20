package model.redundant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.resources.SetUp;

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