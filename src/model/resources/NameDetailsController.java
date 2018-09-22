package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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
    private String dirName;
    private MediaPlayer audioPlayer;

    public String getName() {
        return dirName;
    }

    //Builds list of audio files within 'name' folder
    public void setUpList(List<String> list, String name) {
        dirName = name;
        nameName.setText(dirName);
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Collections.sort(nameListView.getItems());

        setDefaultBtn.setDisable(true);
        deleteBtn.setDisable(true);
        playButton.setDisable(true);

        //Only can set default if there is just 1 name selected
        nameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(nameListView.getSelectionModel().getSelectedItems().size() == 1 && nameListView.getItems().size() != 1) {
                setDefaultBtn.setDisable(false);
            } else {
                setDefaultBtn.setDisable(true);

            }
            deleteBtn.setDisable(false);
            playButton.setDisable(false);
        });
    }

    public void backBtnPressed() throws IOException {
        //Clear list view
        nameListView.getItems().clear();

        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
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

        String selectedName = dirName;
        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();

        Media media = new Media(new File(databasePath+"/"+selectedName+"/"+selectedName).toURI().toString() + ".wav");
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
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {

                deleteBtn.setDisable(true);
                setDefaultBtn.setDisable(true);
                backBtn.setDisable(true);
            }
        }
    }

    //Changes these commands to have backslash before so bash works
    public String bashify(String name) {
        //Characters that break the bash command
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

    public void setDefaultClicked() throws IOException {
        String titleName = nameName.getText();
        String selectedName = nameListView.getSelectionModel().getSelectedItem();

        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();


        String startName = bashify(databasePath + "/" + titleName+ "/" + selectedName);
        String defaultName = bashify(databasePath + "/" + titleName+ "/" + titleName);
        String defaultNameTemp = bashify(databasePath + "/" + titleName+ "/" + titleName+"_t");


        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "mv " + defaultName + " " + defaultNameTemp + "; mv " + startName + " " + defaultName + "; mv " + defaultNameTemp + " " + startName);
            Process renameTemp = builder.start();
            renameTemp.waitFor();

            nameListView.refresh();

        } catch (IOException e) {
            System.out.println("ERROR");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void clearListView() {
        //Clear list view
        nameListView.getItems().clear();
    }

}
