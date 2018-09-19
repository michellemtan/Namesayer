package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class StartMenuController {

    @FXML
    private Button startButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button micButton;
    @FXML
    private Button sadFaceButton;

    //When the startButton is clicked, the scene changes to the "MainMenu" where the user selects the database they
    //wish to practice
    @FXML
    void startButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().databaseSelectMenu;
        Stage window = (Stage) startButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void helpButtonClicked() {
        //TODO: Add HelpMenu with instructions
    }


    @FXML
    void micButtonClicked(MouseEvent event) {

    }

    @FXML
    void sadFaceButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().badRecordingsMenu;
        Stage window = (Stage) sadFaceButton.getScene().getWindow();
        window.setScene(scene);

    }


}
