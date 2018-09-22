package model.resources;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DeleteMenuController {

    @FXML private Button backBtn;
    @FXML private ListView<String> deleteListView;
    @FXML private Label deleteLabel;
    private List<String> toDelete;
    private String previousScenes;

    //TODO: Delete name? Contains x subrecordings

    public void backBtnPressed() throws IOException {
        //Clear list view
        deleteListView.getItems().clear();

        if(previousScenes.equals("practiceDetails") || previousScenes.equals("dbDetails")) {
            Scene scene = SetUp.getInstance().nameDetailsMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void setUpList(List<String> list, String source) {
        previousScenes = source;
        //Change label if only 1 to delete selected
        if(list.size() == 1) {
            deleteLabel.setText("Delete Name?");
        } else {
            deleteLabel.setText("Delete Names?");
        }

        if(previousScenes.equals("practiceDetails") || previousScenes.equals("dbDetails")) {
            deleteLabel.setText("Delete files?");
        }

        toDelete = list;
        deleteListView.getItems().addAll(list);
        //Consume event of selecting a name so as to make the names non-selectable
        deleteListView.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);
    }

    public void confirmBtnPressed() throws IOException {
        //If come from details menu call different delete method
        if(previousScenes.equals("practiceDetails")) {
            SetUp.getInstance().nameDetailsController.clearListView();
            SetUp.getInstance().practiceMenuController.deleteAudioFiles(toDelete.get(0));
            //Switch scenes back
            Scene scene = SetUp.getInstance().practiceMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else if(previousScenes.equals("dbDetails")) {
            SetUp.getInstance().nameDetailsController.clearListView();
            SetUp.getInstance().practiceMenuController.deleteAudioFiles(toDelete.get(0));
            //Switch scenes back
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            SetUp.getInstance().dbMenuController.deleteFiles(toDelete);
            //Switch scenes back
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
        //Clear list view
        deleteListView.getItems().clear();
    }
}
