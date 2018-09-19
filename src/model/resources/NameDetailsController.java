package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NameDetailsController {

    @FXML private Label nameName;
    @FXML private ListView<String> nameListView;
    @FXML private Button backBtn;
    @FXML private Button setDefaultBtn;

    public void setName(String name) {
        nameName.setText(name);
    }

    public void setUpList(List<String> list) {
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if(nameListView.getItems().size() == 1) {
            setDefaultBtn.setDisable(true);
        } else {
            setDefaultBtn.setDisable(false);
        }
    }

    public void selectAllButtonClicked() {
        nameListView.getSelectionModel().selectAll();
    }

    public void backBtnPressed() throws IOException {
        //Clear list view
        nameListView.getItems().clear();

        Scene scene = SetUp.getInstance().databaseMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void deleteBtnPressed() throws IOException {
        if(nameListView.getSelectionModel().getSelectedIndex() != -1) {
            List<String> toDelete = new ArrayList<>(nameListView.getSelectionModel().getSelectedItems());
            SetUp.getInstance().deleteMenuController.setUpList(toDelete);

            //Clear list view
            nameListView.getItems().clear();

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }
}
