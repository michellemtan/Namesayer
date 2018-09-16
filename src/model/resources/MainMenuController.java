package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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
    private String[] prefKeys;

    //Code to be run when add directory button is pressed, opens simple directory chooser and adds selected item to list
    public void addBtnPressed() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose database folder");
        Stage dcStage = new Stage();
        File selectedDirectory = dc.showDialog(dcStage);

        if (selectedDirectory != null) {
            dbListview.getItems().add(selectedDirectory.getName());
            //Add selected file to preferences to be saved
            addPref.put(selectedDirectory.getPath(), selectedDirectory.getName());

        }
    }

    //Code to be run when delete directory is pressed, removes selected directory from directory listview and preferences
    public void deleteBtnPressed() {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            for(String key : prefKeys) {
                if(addPref.get(key, "").equals(dbListview.getSelectionModel().getSelectedItem())) {
                    addPref.remove(key);
                    System.out.println("Here");
                }
            }
            dbListview.getItems().remove(dbListview.getSelectionModel().getSelectedIndex());
        }
    }

    //Code to be run when continue is pressed, changes scene root to be database menu fxml file
    public void continueBtnPressed() throws IOException {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            Parent root = FXMLLoader.load(getClass().getResource("DatabaseMenu.fxml"));
            continueBtn.getScene().setRoot(root);

            //The name that should be used in label for DatabaseMenu
            String dbName = dbListview.getSelectionModel().getSelectedItem().toString();
        }
    }

    //Initialize method is called when the fxml file is loaded, this code just iterates through previously loaded
    //databases and ensures they're still in the listview
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prefKeys = new String[0];
        try {
            prefKeys = addPref.keys();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        for(String key : prefKeys) {
            dbListview.getItems().add(addPref.get(key, key));
        }

        //Code to clear preferences
        /*try {
            addPref.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }*/
    }
}
