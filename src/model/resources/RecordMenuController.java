package model.resources;

import javafx.animation.PauseTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class RecordMenuController {

    @FXML
    private Button playbackButton;

    @FXML
    private Button recordButton;

    @FXML
    private Button compareButton;

    @FXML
    private Button continueButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button micButton;

    private TaskService service;

    //TO DO
    void initialize (){
        playbackButton.setDisable(true);
        compareButton.setDisable(true);
        continueButton.setDisable(true);
    }

    @FXML
    void compareButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().compareMenu;
        Stage window = (Stage) compareButton.getScene().getWindow();
        window.setScene(scene);

    }

    @FXML
    void continueButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().playMenu;
        Stage window = (Stage) continueButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void playbackButtonClicked(MouseEvent event) {
        MediaPlayer audioPlayer;
        Media media = new Media(new File("audio.wav").toURI().toString());
        audioPlayer = new MediaPlayer(media);
        audioPlayer.play();

    }

    @FXML
    void recordButtonClicked(MouseEvent event) {
        record();
    }

    private void record() {

        //TODO: Find a way to ask the user to record again
        service = new TaskService();
        service.setOnSucceeded(e -> {
            askRerecord();
        });

        //TODO: Make the progress bar change so it slowly loads when 5 seconds is reached
        progressBar.progressProperty().bind(service.progressProperty());

        service.start();
    }

    //This method asks the user if they want to record their audio again
    private void askRerecord() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to record your audio again?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("Re-record Audio?");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            record();
        }
    }

    @FXML
    void micButtonClicked(MouseEvent event) {

    }

    /**
     * Class that creates/runs the task to record the audio in the background
     */
    private class TaskService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws IOException {

                    recordButton.setDisable(true);
                    playbackButton.setDisable(true);
                    compareButton.setDisable(true);
                    continueButton.setDisable(true);

                    try {
                        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -f alsa -i  default -t 5 audio.wav");
                        Process audio = builder.start();

                        PauseTransition delay = new PauseTransition(Duration.seconds(5));
                        delay.play();
                        delay.setOnFinished(event -> {
                            playbackButton.setDisable(false);
                            compareButton.setDisable(false);
                            continueButton.setDisable(false);
                            recordButton.setDisable(false);
                            progressBar.progressProperty().unbind();
                            progressBar.progressProperty().set(1.0);
                        });


                    } catch (IOException e) {
                    }

                    return null;
                }
            };
        }
    }
}

