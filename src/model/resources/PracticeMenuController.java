package model.resources;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class PracticeMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button backButton;

    @FXML
    private CheckBox randomiseCheckBox;

    @FXML
    void backButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StartMenu.fxml"));
        backButton.getScene().setRoot(root);
    }

    @FXML
    void randomiseClicked(MouseEvent event) {

    }

    @FXML
    void startButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PlayMenu.fxml"));
        startButton.getScene().setRoot(root);
    }

}
