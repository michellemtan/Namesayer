package app.resources;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {

    @FXML
    private ListView<String> dbListview;

    //Code to be run when add directory button is pressed, opens simple directory chooser and adds selected item to list
    public void addBtnClicked() {
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

    //Code to be run when delete directory is press, removes selected directory from directory listview
    public void deleteBtnClicked() {
        if(dbListview.getSelectionModel().getSelectedIndex() != -1){
            dbListview.getItems().remove(dbListview.getSelectionModel().getSelectedIndex());
        }
    }

}
