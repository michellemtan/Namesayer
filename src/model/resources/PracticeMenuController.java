package model.resources;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    @FXML private Button playSingleButton;
    @FXML
    private ContextMenu sadContext;
    private MediaPlayer audioPlayer;
    private List<String> creationList;
    private String selectedName;
    private String pathToDB;

    //TODO: Dear Rowan,
    //TODO: I have added the bad recordings button, sorry but it will have to be a sad face
    //TODO: I have tried making the list of recordings to be pausable?, however it is very difficult
    //TODO: If you really want it though, feel free to give it a go but I wouldn't be bothered
    //TODO: I was thinking there should be a way for them to escape the play list in case they select all of them
    //TODO: I added a single play button in case the user wants to play just one of those names again

    public void clearListView() {
        creationsListView.getItems().clear();
    }

    //Fill list with selected items
    public void setUpList(List<String> list) throws IOException {
        creationList = list;
        playPauseButton.setDisable(false);
        if (creationList.size()<=1){
            playPauseButton.setDisable(true);
            shuffleButton.setDisable(true);
        }
        creationsListView.getItems().setAll(creationList);
        creationsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();


        creationsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            creationName.setText(creationsListView.getSelectionModel().getSelectedItem());
        });

        setUpTitle();
    }

    @FXML
    public void badRecordingsPressed() throws IOException {
        //Pass current class through to bad recordings
        SetUp.getInstance().badRecordingsMenuController.setPreviousScene("practiceMenu");
        Scene scene = SetUp.getInstance().badRecordingsMenu;
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(scene);
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
        SetUp.getInstance().nameDetailsController.setUpList(list, selectedName, true);

        String defaultName = SetUp.getInstance().nameDetailsController.returnDefault(selectedName);

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
        mediaPlayerCreator();
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

        List<String> audioList = new ArrayList<>(new ArrayList<>(creationsListView.getItems()));

        ObservableList<Media> mediaList = FXCollections.observableArrayList();
        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();

        //System.out.println("Creation list size: " + audioList.size());

        for (String creation : audioList) {
            //Set up the file to be played
            selectedName = creation;
            Media media = new Media(new File(databasePath + "/" + selectedName + "/" + selectedName).toURI().toString() + ".wav");
            mediaList.add(media);
        }

        playMediaTracks(mediaList);
    }


    private void playMediaTracks(List<Media> mediaList) {

        if (mediaList.size() == 0) {
            detailsButton.setDisable(false);
            playPauseButton.setDisable(false);
            shuffleButton.setDisable(false);
            backButton.setDisable(false);
            recordButton.setDisable(false);
            playSingleButton.setDisable(false);
            return;
        } else {
            backButton.setDisable(true);
            recordButton.setDisable(true);
            detailsButton.setDisable(true);
            playPauseButton.setDisable(true);
            shuffleButton.setDisable(true);
            playSingleButton.setDisable(true);
        }

        List<String> nameList = new ArrayList<>(creationList);
        creationName.setText(nameList.get(0));
        nameList.remove(0);

        /*ObservableList<String> obsList = FXCollections.observableArrayList(nameList);
        obsList.remove(0);
        System.out.println(obsList.size());
        obsList.remove(0);
        System.out.println(obsList.size());

        IntegerBinding sizeProperty = Bindings.size(obsList);
        BooleanBinding multipleElemsProperty = new BooleanBinding() {
            @Override protected boolean computeValue() {
                return nameList.size() > 1;
            }
        };

        sizeProperty.addListener((obs, old, newI) -> {
            creationsListView.getSelectionModel().select(nameList.get(0));
        });*/

        //Consume event of selecting a name so as to make the names non-selectable
        //creationsListView.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
        //creationsListView.getSelectionModel().select(count);

        audioPlayer = new MediaPlayer(mediaList.remove(0));
        audioPlayer.play();
        audioPlayer.setOnReady(() -> progressBar.setProgress(0.0));
        audioPlayer.setOnReady(this::progressBar);

        audioPlayer.setOnEndOfMedia(() -> {
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            playMediaTracks(mediaList);
        });

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
    public void playSingleButtonClicked() throws IOException {

        if (audioPlayer != null && audioPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            audioPlayer.stop();
        }

        selectedName = creationsListView.getSelectionModel().getSelectedItem();
        String defaultName = SetUp.getInstance().nameDetailsController.returnDefault(selectedName);
        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();

        Media media = new Media(new File(databasePath + "/" + selectedName + "/" + defaultName).toURI().toString() + ".wav");
        audioPlayer = new MediaPlayer(media);
        audioPlayer.setOnPlaying(new AudioRunnable(false));
        audioPlayer.setOnEndOfMedia(new AudioRunnable(true));
        audioPlayer.play();
        audioPlayer.setOnReady(() -> progressBar.setProgress(0.0));
        audioPlayer.setOnReady(this::progressBar);
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
                backButton.setDisable(false);
                recordButton.setDisable(false);
                playPauseButton.setDisable(false);
                detailsButton.setDisable(false);
                shuffleButton.setDisable(false);
                //When the media player is playing the audio file, the buttons will be disabled to prevent the user from navigating away
            } else {
                backButton.setDisable(true);
                recordButton.setDisable(true);
                playPauseButton.setDisable(true);
                detailsButton.setDisable(true);
                shuffleButton.setDisable(true);
            }
        }
    }

    @FXML
    public void sadFaceButtonClicked() throws IOException {
        try {

            String selectedName = creationsListView.getSelectionModel().getSelectedItem() + ".wav";
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(selectedName+"\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        SetUp.getInstance().badRecordingsMenuController.updateTextLog();
    }

    public String returnSelectedName(){
        String selectedName = creationsListView.getSelectionModel().getSelectedItem();
        return selectedName;
    }
}
