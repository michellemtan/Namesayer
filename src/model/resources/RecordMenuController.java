package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DatabaseProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecordMenuController {

    @FXML private Button playbackButton;
    @FXML private Button recordButton;
    @FXML private Button compareButton;
    @FXML private Button continueButton;
    @FXML private ProgressBar progressBar;
    @FXML private Button micButton;
    @FXML private Button backButton;
    @FXML
    private Label recordLabel;
    private MediaPlayer audioPlayer;
    private int audioRecorded;

    public void setUpRecord(String name) {
        //Disable buttons when scene is initialised
        playbackButton.setDisable(true);
        compareButton.setDisable(true);
        continueButton.setDisable(true);

        //Instantiate audioRecorded field to keep count of number of recordings
        audioRecorded=0;

        //Set label to reflect name
        recordLabel.setText("Record Audio for " + name);
    }

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
                playbackButton.setDisable(false);
                compareButton.setDisable(false);
                continueButton.setDisable(false);
                recordButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {
                recordButton.setDisable(true);
                compareButton.setDisable(true);
                continueButton.setDisable(true);
            }
        }
    }

    @FXML
    void compareButtonClicked() throws IOException {
        String pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();
        String name = SetUp.getInstance().practiceMenuController.returnSelectedName();

        //Create list of audio files
        List<String> list = new ArrayList<>();
        File dir = new File(pathToDB + "/" + name);
        File[] files = dir.listFiles();
        for(File file : files) {
            list.add(file.getName());
        }

        //Pass list through to compare menu
        SetUp.getInstance().compareMenuController.setUpList(list, false, SetUp.getInstance().practiceMenuController.returnSelectedName());

        Scene scene = SetUp.getInstance().compareMenu;
        Stage window = (Stage) compareButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void continueButtonClicked() throws IOException {


        //Processor object to remove silence
        DatabaseProcessor dbProcessor = new DatabaseProcessor("");
        String pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();
        File audioFile = new File(System.getProperty("user.dir") + "/" + "audio.wav");

        String creationName = SetUp.getInstance().practiceMenuController.returnSelectedName();

        File dir = new File(pathToDB + "/" + creationName);
        int count = Objects.requireNonNull(dir.listFiles()).length;

        //If would be overwriting a recording, increment counter
        File[] files = dir.listFiles();
        List<String> stringFiles = new ArrayList<>();
        for(File file : files) {
            stringFiles.add(file.getName());
        }
        if(stringFiles.contains(bashify(creationName) + "(" + String.valueOf(count) + ").wav")) {
            count++;
        }
        String command = "ffmpeg -y -i " + audioFile.getPath() + " -af silenceremove=1:0:-35dB " + pathToDB + "/" + bashify(creationName) + "/" + bashify(creationName) + "\\(" + String.valueOf(count) + "\\)" + ".wav";
        dbProcessor.trimAudio(command);

        audioFile.delete();

        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) continueButton.getScene().getWindow();
        window.setScene(scene);
    }

    public String bashify(String name) {
        //Characters that break the bash command
        char invalids[] = "$/%:\\ .,-".toCharArray();
        boolean found = false;
        String bashed = "";
        char[] chars = name.toCharArray();
        for(char cha : chars) {
            for(char invalid : invalids) {
                if(cha == invalid) {
                    bashed += "\\" + cha;
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
            if(!found) {
                bashed += cha;
            }
        }
        return bashed;
    }

    @FXML
    void backButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) continueButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void playbackButtonClicked() {
        //If an audio file is already playing, stop and play the audio from the start
        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
        }

        //Create a new media player instance and set the event handlers to create a thread that listens for when the audio is playing
        Media media = new Media(new File("audio.wav").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setOnPlaying(new AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
        progressBar();
        audioPlayer.play();

    }

    @FXML
    void recordButtonClicked() {
        if (audioRecorded==0) {
            record();
        } else {
            //Confirm if the user wants to overwrite existing recording
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to overwrite your recording?", ButtonType.NO, ButtonType.YES);
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setTitle("Overwrite Recording?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                record();
            } else {
                alert.close();
            }
        }
        backButton.setDisable(true);
    }

    private void record() {

        RecordAudioService service = new RecordAudioService();
        service.setOnSucceeded(e -> {
            audioRecorded++;
        });

        service.start();
    }

    private void progressBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    void micButtonClicked() throws IOException {

        ProcessBuilder audioBuilder = new ProcessBuilder("/bin/bash", "-c", "bash myscript.sh");
        audioBuilder.start();

    }

    /**
     * Class that creates/runs the task to record the audio in the background
     */
    private class RecordAudioService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() {
                    progressBar();
                    recordButton.setDisable(true);
                    playbackButton.setDisable(true);
                    compareButton.setDisable(true);
                    continueButton.setDisable(true);
                    micButton.setDisable(true);
                    backButton.setDisable(true);

                    try {
                        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -f alsa -i default -t 5 ./audio.wav");
                        Process audio = builder.start();

                        PauseTransition delay = new PauseTransition(Duration.seconds(5));
                        delay.play();
                        delay.setOnFinished(event -> {
                            playbackButton.setDisable(false);
                            compareButton.setDisable(false);
                            continueButton.setDisable(false);
                            recordButton.setDisable(false);
                            micButton.setDisable(false);
                            backButton.setDisable(false);
                        });


                    } catch (IOException e) {
                    }

                    return null;
                }
            };
        }
    }
}

