package model.resources;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PracticeMenuController {

    @FXML
    private Button playPauseButton;

    @FXML
    private Button detailsButton;

    @FXML
    private Button shuffleButton;

    @FXML
    private Button recordButton;

    @FXML
    private ProgressBar progressBar;

    private MediaPlayer audioPlayer;

    @FXML
    private ListView<String> creationsListView;

    @FXML
    private Label creationName;

    @FXML
    private Button backButton;

    private ObservableList<String> creationList;

    private String selectedName;


    //Fill list with selected items
    public void setUpList(ObservableList<String> list) {
        //An observable list ensures the list is always correctly updated (no duplicates)
        creationList = list;
        creationsListView.setItems(creationList);
        setUpTitle();
    }

    @FXML
    void backButtonClicked(MouseEvent event) throws IOException {
//        // Load the new scene
//        Scene scene = backButton.getScene();
//        scene.setRoot(Menu.EXITPRACTICEMENU.loader().load());

        Scene scene = SetUp.getInstance().exitPracticeMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);

    }

    @FXML
    void detailsButtonClicked(MouseEvent event) throws IOException {
//        // Load the new scene
//        Scene scene = detailsButton.getScene();
//        scene.setRoot(Menu.NAMEDETAILSMENU.loader().load());

        Scene scene = SetUp.getInstance().nameDetailsMenu;
        Stage window = (Stage) detailsButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void playButtonClicked(MouseEvent event) throws IOException {
        if (audioPlayer == null){
            //Start playing audio
            mediaPlayerCreator();
        }//If an audio file is already playing, stop
        else if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            audioPlayer.stop();
            mediaPlayerCreator();
        }
    }

    @FXML
    void recordButtonClicked(MouseEvent event) throws IOException {
//        Scene scene = recordButton.getScene();
//        scene.setRoot(Menu.RECORDMENU.loader().load());
        Scene scene = SetUp.getInstance().recordMenu;
        Stage window = (Stage) recordButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void shuffleButtonClicked(MouseEvent event) {
        if (creationList.size()>1){
            ArrayList<String> shuffleList = new ArrayList<String>(creationList.subList(0, creationList.size()-1));
            Collections.shuffle(shuffleList);
            creationList = FXCollections.observableArrayList(shuffleList);
            creationsListView.setItems(creationList);
            setUpTitle();
        }
    }

    private void mediaPlayerCreator() throws IOException {

        String databasePath = SetUp.getInstance().databaseSelectMenuController.returnPath();
        Media media = new Media(new File(databasePath+"/"+selectedName+"/"+selectedName).toURI().toString());
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

    private void setUpTitle(){
        if (creationList!=null){
            creationsListView.getSelectionModel().select(0);
            creationName.setText(creationList.get(0));
        }
    }

    @FXML
    void initialize (){
        creationsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        creationsListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectedName = creationList.get(creationsListView.getSelectionModel().getSelectedIndex());
                creationName.setText(selectedName);

            }
        });
    }
}
