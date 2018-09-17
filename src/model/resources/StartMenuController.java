package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StartMenuController {

    @FXML
    private Button DatabaseButton;

    @FXML
    private Button PracticeButton;

    @FXML
    private Button HelpButton;

    @FXML
    void DatabaseButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        DatabaseButton.getScene().setRoot(root);
    }

    @FXML
    void HelpButtonClicked(MouseEvent event) {
        //TO DO: Add HelpMenu with instructions
        //Parent root = FXMLLoader.load(getClass().getResource("HelpMenu.fxml"));
        //DatabaseButton.getScene().setRoot(root);
    }

    @FXML
    void PracticeButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PracticeMenu.fxml"));
        DatabaseButton.getScene().setRoot(root);
    }

}
