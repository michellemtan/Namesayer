package model.resources;

import javafx.animation.*;
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

public class RecordCreationMenuController {

    @FXML private Button backBtn;
    @FXML private Button compareButton;
    @FXML private Button playbackButton;
    @FXML private Button recordButton;
    @FXML private Button continueButton;
    @FXML private ProgressBar progressBar;
    @FXML private Button micButton;
    @FXML private Button backButton;
    private String creationName;
    private MediaPlayer audioPlayer;
    private int audioRecorded;
    private boolean newCreation;

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
                if(newCreation) {
                    compareButton.setDisable(true);
                } else {
                    compareButton.setDisable(false);
                }
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

    public void backButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().createMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    //Set initial button positions and set fields
    public void setUp(String name) throws IOException {
        newCreation = false;
        creationName = name;
        compareButton.setDisable(true);
        recordButton.setDisable(false);
        continueButton.setDisable(true);
        playbackButton.setDisable(true);
        ListView<String> listView = SetUp.getInstance().dbMenuController.getDbListView();
        if(!listView.getItems().contains(name)) {
            newCreation = true;
        }
    }

    @FXML
    void compareButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().compareMenu;
        Stage window = (Stage) compareButton.getScene().getWindow();
        window.setScene(scene);
    }

    //Process audio and add to new folder & add to list
    void continueButtonClicked() throws IOException {
        //Processor object to remove silence
        DatabaseProcessor dbProcessor = new DatabaseProcessor("");
        String pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();
        File audioFile = new File(System.getProperty("user.dir") + "/" + "audio.wav");
        new File(pathToDB + "/" + creationName).mkdir();
        String command = "ffmpeg -y -i " + audioFile.getPath() + " -af silenceremove=1:0:-35dB " + pathToDB + "/" + creationName + "/" + creationName + ".wav";
        dbProcessor.trimAudio(command);

        //Add newly created files to listview
        SetUp.getInstance().dbMenuController.addItem(creationName);

        //Switch scenes
        Scene scene = SetUp.getInstance().databaseMenu;
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
        audioPlayer.setOnPlaying(new RecordCreationMenuController.AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new RecordCreationMenuController.AudioRunnable(true));
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
    }

    private void record() {

        RecordAudioService service = new RecordAudioService();
        service.setOnSucceeded(e -> {
            audioRecorded++;
        });

        //TODO: Make the progress bar change so it slowly loads when 5 seconds is reached
        //progressBar.progressProperty().bind(service.progressProperty());

        service.start();
    }

    //TODO: FIX BACK BUTTON SO IT GOES BACK TO RECORD, NOT MAIN MENU
    void micButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().microphoneCheckMenu;
        Stage window = (Stage) micButton.getScene().getWindow();
        window.setScene(scene);

    }

    private void progressBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
        timeline.play();
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

                    try {
                        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -f alsa -i default -t 5 ./audio.wav");
                        Process audio = builder.start();

                        PauseTransition delay = new PauseTransition(Duration.seconds(5));
                        delay.play();
                        delay.setOnFinished(event -> {
                            playbackButton.setDisable(false);
                            if(newCreation) {
                                compareButton.setDisable(true);
                            } else {
                                compareButton.setDisable(false);
                            }
                            continueButton.setDisable(false);
                            recordButton.setDisable(false);
                        });


                    } catch (IOException e) {
                    }

                    return null;
                }
            };
        }
    }
}
