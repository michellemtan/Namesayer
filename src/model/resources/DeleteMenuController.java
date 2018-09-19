package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DeleteMenuController {

    @FXML private Button backBtn;
    @FXML private ListView<String> deleteListView;
    @FXML private Label deleteLabel;

    public void backBtnPressed() throws IOException {
        //Clear list view
        deleteListView.getItems().clear();

        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void setUpList(List<String> list) {
        //Change label if only 1 to delete selected
        if(list.size() == 1) {
            deleteLabel.setText("Delete Creation?");
        } else {
            deleteLabel.setText("Delete Creations?");
        }

        for(String name : list) {
            deleteListView.getItems().add(name);
        }
    }

    public void confirmBtnPressed() {

    }
}
