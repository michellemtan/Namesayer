package model.resources;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;

public class PlayMenuController {

    @FXML
    private Button smileButton;

    @FXML
    private Button sadButton;

    @FXML
    private Button backMenuButton;

    @FXML
    private Button nextCreationButton;

    @FXML
    private Button backCreationButton;

    @FXML
    private Button replayAudioButton;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button recordButton;

    @FXML
    void backCreationButtonClicked(MouseEvent event) {

    }

    @FXML
    void backMenuButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PracticeMenu.fxml"));
        backMenuButton.getScene().setRoot(root);
    }

    @FXML
    void nextCreationButtonClicked(MouseEvent event) {

    }

    @FXML
    void playPauseButtonClicked(MouseEvent event) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -i audio.wav");
                    Process audio = builder.start();
                } catch (IOException e) {
                }
                return null;
            }
        };
    }

    @FXML
    void recordButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RecordMenu.fxml"));
        recordButton.getScene().setRoot(root);
    }

    @FXML
    void replayAudioButtonClicked(MouseEvent event) {

    }

    @FXML
    void sadButtonClicked(MouseEvent event) {

    }

}
