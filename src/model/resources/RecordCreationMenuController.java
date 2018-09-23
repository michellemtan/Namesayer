package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
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

public class RecordCreationMenuController {

    @FXML private Button backBtn;
    @FXML private Button compareButton;
    @FXML private Button playbackButton;
    @FXML private Button recordButton;
    @FXML private Button continueButton;
    @FXML private ProgressBar progressBar;
    @FXML private Button micButton;
    @FXML
    private Label recordLabel;
    private String creationName;
    private  String pathToDB;
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
        //Initialise all buttons and strings required
        newCreation = false;
        creationName = name;
        compareButton.setDisable(true);
        recordButton.setDisable(false);
        continueButton.setDisable(true);
        playbackButton.setDisable(true);
        pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();
        progressBar.setProgress(0.0);
        ListView<String> listView = SetUp.getInstance().dbMenuController.getDbListView();
        if(!listView.getItems().contains(name)) {
            newCreation = true;
        }

        recordLabel.setText("Record Audio for " + creationName);
    }

    @FXML
    void compareButtonClicked() throws IOException {
        //Create list of audio files
        List<String> list = new ArrayList<>();
        File dir = new File(pathToDB + "/" + creationName);
        File[] files = dir.listFiles();
        for(File file : files) {
            list.add(file.getName());
        }

        //Pass list through to compare menu
        SetUp.getInstance().compareMenuController.setUpList(list, true, creationName);


        //Switch to compare scene
        Scene scene = SetUp.getInstance().compareMenu;
        Stage window = (Stage) compareButton.getScene().getWindow();
        window.setScene(scene);
    }

    //Process audio and add to new folder & add to list
    @FXML
    void continueButtonClicked() throws IOException {
        //Processor object to remove silence
        DatabaseProcessor dbProcessor = new DatabaseProcessor("");
        File audioFile = new File(System.getProperty("user.dir") + "/" + "audio.wav");

        //This is for NEW creation if newCreation = true)
        if(newCreation) {
            new File(pathToDB + "/" + creationName).mkdir();
            String command = "ffmpeg -y -i " + audioFile.getPath() + " -af silenceremove=1:0:-35dB " + pathToDB + "/" + bashify(creationName) + "/" + bashify(creationName) + ".wav";
            dbProcessor.trimAudio(command);
            //Add newly created files to listview
            SetUp.getInstance().dbMenuController.addItem(creationName);
            //Code here for adding to EXISTING creation (newCreation = false)
        } else {
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
        }

        audioFile.delete();
        //Switch scenes
        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) continueButton.getScene().getWindow();
        window.setScene(scene);
    }

    //Changes these commands to have backslash before so bash works
    public String bashify(String name) {
        //Characters that break the bash command TODO: test if these are all of them
        char invalids[] = "$/%:\\ .,-()@".toCharArray();
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
    void playbackButtonClicked() {
        //If an audio file is already playing, stop and play the audio from the start
        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
        }

        //Create a new media player instance and set the event handlers to create a thread that listens for when the audio is playing
        Media media = new Media(new File("audio.wav").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        progressBar();
        audioPlayer.setOnPlaying(new RecordCreationMenuController.AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new RecordCreationMenuController.AudioRunnable(true));
        audioPlayer.play();

    }

    @FXML
    void recordButtonClicked() {
        backBtn.setDisable(true);
        RecordAudioService service = new RecordAudioService();
        service.start();
    }

    @FXML
    void micButtonClicked() throws IOException {
//        Scene scene = SetUp.getInstance().microphoneCheckMenu;
//        Stage window = (Stage) micButton.getScene().getWindow();
//        window.setScene(scene);

        ProcessBuilder audioBuilder = new ProcessBuilder("/bin/bash", "-c", "bash myscript.sh");
        audioBuilder.start();

    }

    public void progressBar() {
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
                    micButton.setDisable(true);

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
                            backBtn.setDisable(false);
                            micButton.setDisable(false);
                        });


                    } catch (IOException e) {
                    }

                    return null;
                }
            };
        }
    }
}
