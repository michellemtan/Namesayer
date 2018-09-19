package model.resources;

import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

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
    private Label creationNameLabel;

    @FXML
    private Label creationNumber;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void backCreationButtonClicked(MouseEvent event) {

    }

    @FXML
    void backMenuButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) backMenuButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void nextCreationButtonClicked(MouseEvent event) {

    }

    @FXML
    void playPauseButtonClicked(MouseEvent event) {
        /*Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -i audio.wav");
                    Process audio = builder.start();
                } catch (IOException e) {
                }
                return null;
            }
        };*/

        MediaPlayer audioPlayer;
        Media media = new Media(new File("path/to/audio.file").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        audioPlayer.play();
    }

    @FXML
    void recordButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().recordMenu;
        Stage window = (Stage) recordButton.getScene().getWindow();
        window.setScene(scene);
    }


    @FXML
    void replayAudioButtonClicked(MouseEvent event) {

    }

    @FXML
    void sadButtonClicked(MouseEvent event) {
        try {
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append("Creation");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @FXML
        void backMenuButtonClicked(MouseEvent event) throws IOException {
            checkExit();
        }

    //TODO: Have alert or pop up window confirming if the user wants to exit?
        private void checkExit() throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to stop practicing?", ButtonType.NO, ButtonType.YES);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setTitle("Exit Practice?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                Scene scene = SetUp.getInstance().practiceMenu;
                Stage window = (Stage) backMenuButton.getScene().getWindow();
                window.setScene(scene);
            } else {
                alert.close();
            }

        }

    }



