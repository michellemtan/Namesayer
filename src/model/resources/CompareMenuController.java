package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CompareMenuController {

    @FXML private Button backButton;
    @FXML private ListView<String> recordingsList;
    @FXML private Button playPauseButton;
    @FXML private Button sadFaceButton;
    @FXML private ProgressBar progressBar;
    private MediaPlayer audioPlayer;
    private boolean fromCreate;

    @FXML
    void backButtonClicked() throws IOException {
        if(fromCreate) {
            Scene scene = SetUp.getInstance().recordCreationMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else {
            Scene scene = SetUp.getInstance().recordMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void setUpList(List<String> list, boolean create) {
        fromCreate = create;
        recordingsList.getItems().setAll(list);
        recordingsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    //TODO: Make this a public class?
    //AudioRunnable is a thread that runs in the background and acts as a listener for the media player to ensure buttons are enabled/disabled correctly
    private class AudioRunnable implements Runnable {

        private boolean isFinished;

        private AudioRunnable(boolean status){
            isFinished = status;
        }

        @Override
        public void run() {
            //When the media player has finished, the buttons will be enabled
            if (isFinished) {
                backButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {
                backButton.setDisable(true);
            }
        }
    }

    @FXML
    void playPauseButtonClicked() {

        //If an audio file is already playing, stop and play the audio from the start
        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
        }

        //Create a new media player instance and set the event handlers to create a thread that listens for when the audio is playing
        Media media = new Media(new File("audio.wav").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setOnPlaying(new AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
        audioPlayer.play();

    }

    @FXML
    void sadFaceButtonClicked() {
        try {
            String selectedName = SetUp.getInstance().createMenuController.getName();
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(selectedName+"\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
