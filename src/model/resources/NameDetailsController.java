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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class NameDetailsController {

    @FXML private Label nameName;
    @FXML private ListView<String> nameListView;
    @FXML private Button backBtn;
    @FXML private Button setDefaultBtn;
    @FXML private Button playButton;
    @FXML private Button deleteBtn;
    @FXML private ProgressBar progressBar;
    @FXML private Label defaultLabel;
    private String previousScene;
    private String dirName;
    private MediaPlayer audioPlayer;
    private HashMap<String, String> defaultNames;


    //TODO: HI ROWAN
    //TODO: PLEASE MOVE THIS THANK YOU! ok sure thing
    private void toMoveIntoDelete() {
        Alert error = new Alert(Alert.AlertType.ERROR, "The default audio file cannot be deleted.", ButtonType.OK);
        error.setGraphic(null);
        error.setHeaderText("ERROR: Invalid Deletion");
        error.setTitle("Invalid Deletion");
        error.showAndWait();
    }


    public String getName() {
        return dirName;
    }

    public int getSize() {
        System.out.println(nameListView.getItems() + String.valueOf(nameListView.getItems().size()));
        return nameListView.getItems().size();
    }

    //Builds list of audio files within 'name' folder
    public void setUpList(List<String> list, String name, String source) throws IOException {

        //Clear list view
        nameListView.getItems().clear();
        dirName = name;
        previousScene = source;
        //Set default label to represent default
        defaultLabel.setText("Default: " + returnDefault(dirName));
        nameName.setText(dirName);
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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

    @FXML
    public void badRecordingsPressed() throws IOException {
        //Pass current class through to bad recordings
        SetUp.getInstance().badRecordingsMenuController.setPreviousScene("nameDetailsMenu");
        Scene scene = SetUp.getInstance().badRecordingsMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void backBtnPressed() throws IOException {
        //Clear list view
        nameListView.getItems().clear();
        //Go to right scene depending where previous scene was
        if(previousScene.equals("practice")) {
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
            List<String> toDelete = new ArrayList<>(nameListView.getSelectionModel().getSelectedItems());             //Pass list of files to delete through to delete menu
                if(previousScene.equals("practice")) {
                    SetUp.getInstance().deleteMenuController.setUpList(toDelete, "practiceDetails");
                } else {
                    SetUp.getInstance().deleteMenuController.setUpList(toDelete, "dbDetails");
                }

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
    public void sadFaceButtonClicked() throws IOException {
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

        SetUp.getInstance().badRecordingsMenuController.updateTextLog();
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

    public void setDefaultOnDelete(String newDefault) {
        if (defaultNames == null) {
            defaultNames = new HashMap<>();
        }
        defaultNames.put(nameName.getText(), newDefault);
    }

    public void setDefaultClicked() {
        String titleName = nameName.getText();
        String selectedName = nameListView.getSelectionModel().getSelectedItem().replaceAll(".wav", "");

        if (defaultNames == null) {
            defaultNames = new HashMap<>();
        }

        defaultNames.put(titleName, selectedName + ".wav");
        defaultLabel.setText("Default: " + selectedName + ".wav");
    }

    public void clearListView() {
        //Clear list view
        nameListView.getItems().clear();
    }

    public String getNewDefault(String directoryName) throws IOException {
        File dir = new File(SetUp.getInstance().dbMenuController.getPathToDB() + "/" + directoryName);
        File[] files = dir.listFiles();
        //if name.wav is a file, set that as default. Else, just first file in list
        List<String> fileNames = new ArrayList<>();
        List<File> fileList = Arrays.asList(files);
        for(File file : fileList) {
            fileNames.add(file.getName());
        }
        if(fileNames.contains(directoryName + ".wav")) {
            return directoryName + ".wav";
        }
        return files[0].getName();
    }

    public String returnDefault(String title) throws IOException {

        if (defaultNames == null) {
            return getNewDefault(title);
        } else if (defaultNames.containsKey(title)) {
            defaultLabel.setText("Default: " + defaultNames.get(title) + ".wav");
            return defaultNames.get(title);
        } else {
            System.out.println("this shouldn't print");
            return title;
        }
    }
}