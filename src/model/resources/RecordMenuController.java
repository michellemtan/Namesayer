package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RecordMenuController {

    @FXML
    private Button playbackButton;

    @FXML
    private Button recordButton;

    @FXML
    private Button compareButton;

    @FXML
    private Button continueButton;

    @FXML
    void compareButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CompareMenu.fxml"));
        compareButton.getScene().setRoot(root);

    }

    @FXML
    void continueButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PlayMenu.fxml"));
        continueButton.getScene().setRoot(root);
    }

    @FXML
    void playbackButtonClicked(MouseEvent event) {

    }

    @FXML
    void recordButtonClicked(MouseEvent event) {

    }

}
