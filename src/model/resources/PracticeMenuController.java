package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PracticeMenuController {

    @FXML private Button playPauseButton;

    @FXML private Button sadFaceButton;
    @FXML private Button detailsButton;
    @FXML private Button shuffleButton;
    @FXML private Button recordButton;
    @FXML private ProgressBar progressBar;
    @FXML private ListView<String> creationsListView;
    @FXML private Label creationName;
    @FXML private Button backButton;
    private MediaPlayer audioPlayer;
    private List<String> creationList;
    private String selectedName;
    private String pathToDB;

    //TODO: Make the thumbs up/down buttons,
    //TODO: Make a practice all button (or some way of playing through all items in creationsListView,
    //TODO: Pushing the pause button while media is playing just restarts it rather than pausing,
    //TODO: Maybe make details menu work if you want (or I can I don't mind),
    //TODO: If you select an audio file that is empty (really quiet/bad quality ones get wiped during silenceremove) e.g Li, then play it, playing others doesn't work.

    public void clearListView() {
        creationsListView.getItems().clear();
    }

    //Fill list with selected items
    public void setUpList(List<String> list) throws IOException {
        creationList = list;
        creationsListView.getItems().setAll(creationList);
        creationsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();

        //Only can set default if there is just 1 name selected
        creationsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            creationName.setText(creationsListView.getSelectionModel().getSelectedItem());
        });

        setUpTitle();
    }

    private void setUpTitle(){
        if (creationList!=null){
            creationsListView.getSelectionModel().select(0);
            creationName.setText(creationList.get(0));
        }
    }

    @FXML
    void backButtonClicked() throws IOException {
       // Load the new scene
        //Scene scene = backButton.getScene();
        //scene.setRoot(Menu.EXITPRACTICEMENU.loader().load());

        Scene scene = SetUp.getInstance().exitPracticeMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);

    }

    @FXML
    void detailsButtonClicked() throws IOException {
        // Load the new scene
        //Scene scene = detailsButton.getScene();
        //scene.setRoot(Menu.NAMEDETAILSMENU.loader().load());

        //Set selected name
        selectedName = creationsListView.getSelectionModel().getSelectedItem();
        //Initialise list of name to
        List<String> list = new ArrayList<>();
        //Add all files in directory to list
        File dir = new File(pathToDB + "/" + selectedName);
        File[] files = dir.listFiles();
        for(File file : files) {
            list.add(file.getName());
        }

        //Add list to name details menu
        SetUp.getInstance().nameDetailsController.setUpList(list, selectedName);

        //Switch scene
        selectedName = creationsListView.getSelectionModel().getSelectedItem();

        Scene scene = SetUp.getInstance().nameDetailsMenu;
        Stage window = (Stage) detailsButton.getScene().getWindow();
        window.setScene(scene);
    }


    //Method to delete files if coming from the NameDetails menu
    public void deleteAudioFiles(List<String> list) throws IOException {
        String dirName = SetUp.getInstance().nameDetailsController.getName();
        File dir = new File(pathToDB + "/" + dirName);
        for(String name : list) {
            File file = new File(pathToDB + "/" + dirName + "/" + name);
            file.delete();

        }

        //If there's only one file left, rename it so it's now the default
        if(Objects.requireNonNull(dir.listFiles()).length >= 1 && list.contains(dirName + ".wav")) {
            File[] fileNames = dir.listFiles();
            for(int i=1; i<=fileNames.length; i++) {
                File fileName = new File(fileNames[i].getPath());
                fileName.renameTo(new File(pathToDB + "/" + dirName + "/" + dirName + ".wav"));
                break;
            }
        }

        //Try to delete directory, will only work if non-empty - correct behaviour
        if(dir.delete()) {
            //Remove name from list if directory now empty
            creationsListView.getItems().remove(dirName);
            //Remove from dbListView also
            SetUp.getInstance().dbMenuController.removeListItem(dirName);
        }
    }

    @FXML
    void playButtonClicked() throws IOException {
        selectedName = creationsListView.getSelectionModel().getSelectedItem();
        creationName.setText(selectedName);
        if (audioPlayer == null){
            //Start playing audio
            mediaPlayerCreator();
        }//If an audio file is already playing, stop
        else if (audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
            mediaPlayerCreator();
        }
    }

    @FXML
    void recordButtonClicked() throws IOException {
        SetUp.getInstance().recordMenuController.setUpRecord(creationsListView.getSelectionModel().getSelectedItem());

        Scene scene = SetUp.getInstance().recordMenu;
        Stage window = (Stage) recordButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void shuffleButtonClicked() {
        //Shuffle list
        Collections.shuffle(creationsListView.getItems());
    }

    private void mediaPlayerCreator() throws IOException {

        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();
        Media media = new Media(new File(databasePath+"/"+selectedName+"/"+selectedName).toURI().toString() + ".wav");
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setOnPlaying(new AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
        audioPlayer.play();

        audioPlayer.currentTimeProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            Duration newDuration = (Duration) newValue;
            progressBar.setProgress(newDuration.toSeconds()/5);

        });
        audioPlayer.setOnReady(() -> progressBar.setProgress(0.0));
        audioPlayer.setOnReady(this::progressBar);
    }

    private void progressBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(audioPlayer.getTotalDuration(), new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }



    //TODO: Make this a public class?
    //AudioRunnable is a thread that runs in the background and acts as a listener for the media player to ensure buttons are enabled/disabled correctly
    public class AudioRunnable implements Runnable {

        private boolean isFinished;

        private AudioRunnable(boolean status){
            isFinished = status;
        }

        @Override
        public void run() {
            //When the media player has finished, the buttons will be enabled
            if (isFinished) {
                backButton.setDisable(false);
                recordButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {;
                backButton.setDisable(true);
                recordButton.setDisable(true);
            }
        }
    }

    @FXML
    public void sadFaceButtonClicked() {
        try {

            String selectedName = creationsListView.getSelectionModel().getSelectedItem();
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(selectedName+"\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String returnSelectedName(){
        String selectedName = creationsListView.getSelectionModel().getSelectedItem();
        return selectedName;
    }
}
