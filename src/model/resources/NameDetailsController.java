package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameDetailsController {

    //TODO: Maybe add a .wav so the user knows that they're audio wav files

    @FXML private Label nameName;
    @FXML private ListView<String> nameListView;
    @FXML private Button backBtn;
    @FXML private Button setDefaultBtn;
    @FXML private Button playButton;
    @FXML private Button deleteBtn;
    @FXML private ProgressBar progressBar;
    private boolean fromPractice;
    private String dirName;
    private MediaPlayer audioPlayer;

    public String getName() {
        return dirName;
    }

    //Builds list of audio files within 'name' folder
    public void setUpList(List<String> list, String name, boolean source) {
        //Clear list view
        nameListView.getItems().clear();
        dirName = name;
        fromPractice = source;
        nameName.setText(dirName);
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Collections.sort(nameListView.getItems());

        setDefaultBtn.setDisable(true);
        deleteBtn.setDisable(true);
        playButton.setDisable(true);

        //Only can set default if default is not selected
        nameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setDefaultBtn.setDisable(false);
            deleteBtn.setDisable(false);
            playButton.setDisable(false);
        });
    }

    @FXML
    public void badRecordingsPressed() throws IOException {
        //Pass current class through to bad recordings
        SetUp.getInstance().badRecordingsMenuController.setPreviousScene("nameDetailsMenu");
        Scene scene = SetUp.getInstance().badRecordingsMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void backBtnPressed() throws IOException {
        nameListView.getItems().clear();
        //Go to right scene depending where previous scene was
        if(fromPractice) {
            Scene scene = SetUp.getInstance().practiceMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }

    }

    public void deleteBtnPressed() throws IOException {
        if(nameListView.getSelectionModel().getSelectedIndex() != -1) {
            List<String> toDelete = new ArrayList<>(nameListView.getSelectionModel().getSelectedItems());
            //Pass list of files to delete through to delete menu
            SetUp.getInstance().deleteMenuController.setUpList(toDelete, true);

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    @FXML
    public void playButtonClicked() throws IOException {

        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            audioPlayer.stop();
        }

        String selectedName = nameListView.getSelectionModel().getSelectedItem().replaceAll(".wav","");
        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();

        Media media = new Media(new File(databasePath+"/"+dirName+"/"+selectedName).toURI().toString() + ".wav");
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setOnPlaying(new AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
        audioPlayer.play();
        audioPlayer.setOnReady(() -> progressBar.setProgress(0.0));
        audioPlayer.setOnReady(this::progressBar);
    }

    private void progressBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame((audioPlayer.getTotalDuration().add(Duration.millis(1000))), new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    public void sadFaceButtonClicked() {
        try {
            String selectedName = nameListView.getSelectionModel().getSelectedItem();
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(selectedName + "\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                deleteBtn.setDisable(false);
                setDefaultBtn.setDisable(false);
                backBtn.setDisable(false);
                playButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {

                deleteBtn.setDisable(true);
                setDefaultBtn.setDisable(true);
                backBtn.setDisable(true);
                playButton.setDisable(true);
            }
        }
    }

    public void setDefaultClicked() throws IOException {
        //Create files to be moved + temp file
        File originalDefault = new File(SetUp.getInstance().dbMenuController.getPathToDB() + "/" + nameName.getText() + "/" + nameName.getText() + ".wav");
        File newDefault = new File(SetUp.getInstance().dbMenuController.getPathToDB() + "/" + nameName.getText() + "/" + nameListView.getSelectionModel().getSelectedItem());
        File tempFile = new File(SetUp.getInstance().dbMenuController.getPathToDB() + "/" + nameName.getText() + "/tempfile.wav");

        //Rename files
        originalDefault.renameTo(tempFile);
        newDefault.renameTo(originalDefault);
        tempFile.renameTo(newDefault);
    }



    public void clearListView() {
        //Clear list view
        nameListView.getItems().clear();
    }

}
