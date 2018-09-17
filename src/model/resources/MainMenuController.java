package model.resources;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.DatabaseProcessor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MainMenuController implements Initializable {

    @FXML private ListView<String> dbListview;
    @FXML private Button continueBtn;
    private Preferences addPref = Preferences.userRoot();
    private Stage progressStage;
    private TaskService service;

    //Code to be run when add directory button is pressed, opens simple directory chooser and adds selected item to list
    //TODO: instead of directory path make directory name displayed, and implement numbers for matching name dirs. Possibly
    //store keys in hash map in initialise to do this?
    public void addBtnPressed() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose database folder");
        Stage dcStage = new Stage();
        File selectedDirectory = dc.showDialog(dcStage);

        if (selectedDirectory != null) {
            //Add selected file to preferences to be saved
            dbListview.getItems().add(selectedDirectory.getPath());
            addPref.put(selectedDirectory.getPath(), selectedDirectory.getPath());
        }
    }

    //Code to be run when delete directory is pressed, removes selected directory from directory listview and preferences
    public void deleteBtnPressed() {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            addPref.remove(dbListview.getSelectionModel().getSelectedItem());
            dbListview.getItems().remove(dbListview.getSelectionModel().getSelectedIndex());
        }
    }

    //Code to be run when continue is pressed, changes scene root to be database menu fxml file
    public void continueBtnPressed() {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            //The name that should be used in label for DatabaseMenu
            String dbName = dbListview.getSelectionModel().getSelectedItem();

            service.restart();
        }
    }

    //Initialize method is called when the fxml file is loaded, this code just iterates through previously loaded
    //databases and ensures they're still in the listview.
    //TODO: sometimes a garbage value appears in preferences, not too sure why/where it comes from.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] prefKeys = new String[0];
        try {
            prefKeys = addPref.keys();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        for(String key : prefKeys) {
            dbListview.getItems().add(addPref.get(key, key));
        }

        service = new TaskService();
        service.setOnScheduled(e -> progressStage.show());
        service.setOnSucceeded(e -> progressStage.hide());

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(service.progressProperty());
        progressBar.setPrefSize(200, 50);

        progressStage = new Stage();
        progressStage.setTitle("Processing Database");
        progressStage.setScene(new Scene(new StackPane(progressBar), 400, 150));
        progressStage.setAlwaysOnTop(true);

        //Code to clear preferences
        /*try {
            addPref.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }*/
    }


    /**
     * Class that creates/runs the task to process the database in the background
     */
    private class TaskService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws IOException {
                    //Instantiate database processor and start processing
                    DatabaseProcessor processor = new DatabaseProcessor(dbListview.getSelectionModel().getSelectedItem());
                    processor.processDB();

                    //Load new scene upon completion
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DatabaseMenu.fxml"));
                    Parent root = loader.load();

                    DatabaseMenuController controller = loader.getController();
                    //Pass database location to controller for database menu
                    controller.initialise(dbListview.getSelectionModel().getSelectedItem());
                    continueBtn.getScene().setRoot(root);
                    return null;
                }
            };
        }
    }
}
