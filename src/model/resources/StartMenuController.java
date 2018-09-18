package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StartMenuController {

    @FXML
    private Button startButton;

    @FXML
    private Button helpButton;

    //When the startButton is clicked, the scene changes to the "MainMenu" where the user selects the database they
    //wish to practice
    @FXML
    void startButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        startButton.getScene().setRoot(root);
    }

    @FXML
    void helpButtonClicked(MouseEvent event) {
        //TO DO: Add HelpMenu with instructions
        //Parent root = FXMLLoader.load(getClass().getResource("HelpMenu.fxml"));
        //DatabaseButton.getScene().setRoot(root);
    }

}
