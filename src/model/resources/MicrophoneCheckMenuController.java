package model.resources;

import javafx.animation.PauseTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MicrophoneCheckMenuController {

    @FXML
    private Button backButton;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button recordButton;

    @FXML
    private Button playButton;

    private RecordAudioService service;

    private MediaPlayer audioPlayer;

    @FXML
    void backButtonClicked(MouseEvent event) throws IOException {
        Scene scene = SetUp.getInstance().startMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);
    }

    @FXML
    void playButtonClicked(MouseEvent event) {
        Media mediaPick = new Media(new File(System.getProperty("user.dir") + "/sample").toURI().toString() + ".mp4");
        audioPlayer = new MediaPlayer(mediaPick);
        mediaView.setMediaPlayer(audioPlayer);
        audioPlayer.play();
    }

    @FXML
    void recordButtonClicked(MouseEvent event) {
        service = new RecordAudioService();
        service.start();

    }

    //Class that creates/runs the task to record the audio in the background
    private class RecordAudioService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws IOException {
                    //Disable buttons to prevent user from leaving menu while recording
                    recordButton.setDisable(true);
                    playButton.setDisable(true);
                    backButton.setDisable(true);

                    try {
                        //Create a BASH process to record the sample audio
                        ProcessBuilder audioBuilder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -f alsa -i default -t 5 ./sampleaudio.wav");
                        Process audio = audioBuilder.start();

                        PauseTransition delay = new PauseTransition(Duration.seconds(5));
                        delay.play();
                        delay.setOnFinished(event -> {
                            //Create the video to be played
                            CreateVideoService videoService = new CreateVideoService();
                            videoService.start();
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;

                }
            };
        }
    }

    //Class that creates/runs the task to create the video in the background
        private class CreateVideoService extends Service<Void> {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws IOException {
                        //Create a process calling BASH commands to create a video of the recording's volume levels
                        ProcessBuilder videoBuilder = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -y -i sampleaudio.wav -filter_complex \"[0:a]showvolume=f=0.5:c=VOLUME:b=4:w=1000:h=320,format=yuv420p[v]\" -map \"[v]\" -map 0:a samplevideo.mp4");
                        try {
                            Process video = videoBuilder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Allow for some processing time
                        PauseTransition delay = new PauseTransition(Duration.seconds(3));
                        delay.play();

                        //Video has been created, let the user have control of buttons again
                        playButton.setDisable(false);
                        backButton.setDisable(false);
                        recordButton.setDisable(false);

                        return null;
                    }
                };
            }
        }

        @FXML
        void initialize(){
            //This ensures that the user records a sample audio file before they can press play
            playButton.setDisable(true);
        }
}
