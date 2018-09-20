package model.redundant;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import model.resources.SetUp;

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

    private MediaPlayer audioPlayer;

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

        if (audioPlayer == null){
            //Start playing audio
            mediaPlayerCreator();
        }//If an audio file is already playing, stop
        else if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.pause();
            //If audio is already stopped, play from where it was paused
        } else if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            audioPlayer.play();
        }
    }


    //TODO: ADD FEATURE WHERE IF YOU ENTER A NEW SCREEN, YOU CAN PRESS PLAY AND REPLAY IS DISABLED
    @FXML
    void recordButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().recordMenu;
        Stage window = (Stage) recordButton.getScene().getWindow();
        window.setScene(scene);
    }


    @FXML
    void replayAudioButtonClicked(MouseEvent event) {

        //If an audio file is already playing, stop and play the audio from the start
        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
        }

    mediaPlayerCreator();
    }

    @FXML
    void sadButtonClicked(MouseEvent event) {
        try {
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append("Creation"+"\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                playPauseButton.setDisable(true);
                backMenuButton.setDisable(false);
                backCreationButton.setDisable(false);
                nextCreationButton.setDisable(false);
                recordButton.setDisable(false);
                replayAudioButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {
                playPauseButton.setDisable(false);
                backMenuButton.setDisable(true);
                backCreationButton.setDisable(true);
                nextCreationButton.setDisable(true);
                recordButton.setDisable(true);
            }
        }
    }

        @FXML
        void backMenuButtonClicked(MouseEvent event) throws IOException {
            //checkExit();
            Scene scene = SetUp.getInstance().exitPracticeMenu;
            Stage window = (Stage) backMenuButton.getScene().getWindow();
            window.setScene(scene);
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

        void initialize() {
            replayAudioButton.setDisable(true);
        }

        private void mediaPlayerCreator(){

            Media media = new Media(new File("audio.wav").toURI().toString());
            audioPlayer = new MediaPlayer(media);
            audioPlayer.setOnPlaying(new AudioRunnable(false));
            audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
            audioPlayer.play();

            audioPlayer.currentTimeProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    Duration newDuration = (Duration) newValue;
                    progressBar.setProgress(newDuration.toSeconds()/5);

                }
            });
            audioPlayer.setOnReady(new Runnable() {
                public void run() {
                    progressBar.setProgress(0.0);
                }
            });
        }
    }



