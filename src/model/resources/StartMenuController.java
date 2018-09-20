package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import java.io.IOException;

public class StartMenuController {

    @FXML
    private Button startButton;
    @FXML
    private Button instructionsButton;
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
    void instructionsButtonClicked() throws IOException {
//        Scene scene = SetUp.getInstance().instructionsMenu;
//        Stage window = (Stage) instructionsButton.getScene().getWindow();
//        window.setScene(scene);

        // Load the new scene
        Scene scene = instructionsButton.getScene();
        scene.setRoot(Menu.INSTRUCTIONSMENU.loader().load());
    }


    //TO DO:
    @FXML
    void micButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().microphoneCheckMenu;
        Stage window = (Stage) micButton.getScene().getWindow();
        window.setScene(scene);

    }

    @FXML
    void sadFaceButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().badRecordingsMenu;
        Stage window = (Stage) sadFaceButton.getScene().getWindow();
        window.setScene(scene);

    }


}
