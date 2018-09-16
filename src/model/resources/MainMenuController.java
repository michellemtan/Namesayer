package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenuController {

    @FXML private ListView<String> dbListview;
    @FXML private Button continueBtn;

    //Code to be run when add directory button is pressed, opens simple directory chooser and adds selected item to list
    public void addBtnPressed() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose database folder");
        Stage dcStage = new Stage();
        File selectedDirectory = dc.showDialog(dcStage);

        if(selectedDirectory == null) {
            System.out.println("No directory selected");
        } else {
            dbListview.getItems().add(selectedDirectory.getName());
        }
    }

    //Code to be run when delete directory is pressed, removes selected directory from directory listview
    public void deleteBtnPressed() {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            dbListview.getItems().remove(dbListview.getSelectionModel().getSelectedIndex());
        }
    }

    //Code to be run when continue is pressed, changes scene root to be database menu fxml file
    public void continueBtnPressed() throws IOException {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            System.out.println("1");
            Parent root = FXMLLoader.load(getClass().getResource("DatabaseMenu.fxml"));
            System.out.println("2");
            continueBtn.getScene().setRoot(root);
            System.out.println("3");

            //The name that should be used in label for DatabaseMenu
            String dbName = dbListview.getSelectionModel().getSelectedItems().toString();
        }
    }

}
