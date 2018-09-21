package model.resources;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PracticeMenuController {

    @FXML private Button playPauseButton;
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

    //TODO: Dear Michelle,
    //TODO: Some fixes that must now be made to this class are:
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
        setUpTitle();
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
        //Try to delete directory, will only work if non-empty - correct behaviour
        if(dir.delete()) {
            //Remove name from list if directory now empty
            creationsListView.getItems().remove(dirName);
        }
    }

    @FXML
    void playButtonClicked() throws IOException {
        selectedName = creationsListView.getSelectionModel().getSelectedItem();
        creationName.setText(creationsListView.getSelectionModel().getSelectedItem());
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
        //Scene scene = recordButton.getScene();
        //scene.setRoot(Menu.RECORDMENU.loader().load());
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
    }

    private void setUpTitle(){
        if (creationList!=null){
            creationsListView.getSelectionModel().select(0);
            creationName.setText(creationList.get(0));
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
                backButton.setDisable(false);
                recordButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {;
                backButton.setDisable(true);
                recordButton.setDisable(true);
            }
        }
    }
}
