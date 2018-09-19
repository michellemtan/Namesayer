package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompareMenuController {

    @FXML
    private Button backButton;

    @FXML
    private ListView<?> recordingsList;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button sadFaceButton;

    @FXML
    private ProgressBar progressBar;

    private MediaPlayer audioPlayer;

    @FXML
    void backButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().recordMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void playPauseButtonClicked(MouseEvent event) {

        //If an audio file is already playing, stop and play the audio from the start
        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
        }

        Media media = new Media(new File("audio.wav").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        audioPlayer.play();

    }

    @FXML
    void sadFaceButtonClicked(MouseEvent event) {
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

}
