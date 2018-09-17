package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StartMenuController {

    @FXML
    private Button databaseButton;

    @FXML
    private Button practiceButton;

    @FXML
    private Button helpButton;

    @FXML
    void databaseButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        databaseButton.getScene().setRoot(root);
    }

    @FXML
    void helpButtonClicked(MouseEvent event) {
        //TO DO: Add HelpMenu with instructions
        //Parent root = FXMLLoader.load(getClass().getResource("HelpMenu.fxml"));
        //DatabaseButton.getScene().setRoot(root);
    }

    @FXML
    void practiceButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PracticeMenu.fxml"));
        practiceButton.getScene().setRoot(root);
    }

}
