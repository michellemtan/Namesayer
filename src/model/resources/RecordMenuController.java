package model.resources;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    //TO DO
    void initialize (){
        playbackButton.setDisable(true);
        compareButton.setDisable(true);
        continueButton.setDisable(true);
    }

    @FXML
    void compareButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CompareMenu.fxml"));
        compareButton.getScene().setRoot(root);

    }

    @FXML
    void continueButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("PlayMenu.fxml"));
        continueButton.getScene().setRoot(root);
    }

    @FXML
    void playbackButtonClicked(MouseEvent event) {
        progressBar.setProgress(0);

    }

    @FXML
    void recordButtonClicked(MouseEvent event) {
        record();
    }

    private void record() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                progressBar.setProgress(0);
                recordButton.setDisable(true);
                playbackButton.setDisable(true);
                compareButton.setDisable(true);
                continueButton.setDisable(true);

                try {
                    ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -f alsa -ac 1 -ar 44100 -i default -t 00:00:05 audio.wav");
                    Process audio = builder.start();

                    PauseTransition delay = new PauseTransition(Duration.seconds(5));
                    delay.play();
                    askRerecord();
                    delay.setOnFinished(event -> {});

                } catch (IOException e) {
                }
                return null;
            }
        };

        //TO FIX!
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable,
                                Worker.State oldValue, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    progressBar.setProgress(1);
                    recordButton.setDisable(false);
                    playbackButton.setDisable(false);
                    compareButton.setDisable(false);
                    continueButton.setDisable(false);
                }
            }
        });

        //start Task
        new Thread(task).start();
    }


    //This method asks the user if they want to record their audio again
    private void askRerecord() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to record your audio again?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("Record Audio");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            record();
        }
    }
}

