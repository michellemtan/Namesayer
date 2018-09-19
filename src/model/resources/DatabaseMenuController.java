package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMenuController {

    @FXML private Label databaseName;
    @FXML private Button backBtn;
    @FXML private Button deleteBtn;
    @FXML private Button practiceButton;
    @FXML private ListView<String> dbListView;
    private String pathToDB;

    //TODO: should jonothan and Jonothan be the same name!?
    void initialize(String path) {
        pathToDB = path;
        databaseName.setText(path.substring(path.lastIndexOf("/") + 1));
        List<String> names = new ArrayList<>();

        //Iterate through folders and create list of names
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                names.add(child.getName());
                //dbListView.getItems().add(child.getName());
            }
        }

        //Sort name and add to list view
        names.sort(String.CASE_INSENSITIVE_ORDER);
        dbListView.getItems().addAll(names);
        dbListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void selectAllPressed() {
        dbListView.getSelectionModel().selectAll();
    }

    public void deleteBtnPressed() throws IOException {
        if(dbListView.getSelectionModel().getSelectedIndex() != -1) {
            //TODO: Look into directing with tool tip
            List<String> toDelete = new ArrayList<>(dbListView.getSelectionModel().getSelectedItems());
            //Pass list into deleteMenuController
            SetUp.getInstance().deleteMenuController.setUpList(toDelete);

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void deleteFiles(List<String> list) {
        for(String name : list) {
            //Delete all files inside folder (.delete doesn't work on non-empty directories)
            File dir = new File(pathToDB + "/" + name);
            File[] directoryListing = dir.listFiles();
            if(directoryListing != null) {
                for (File file : directoryListing) {
                    File toDelete = new File(file.getPath());
                    toDelete.delete();
                }
            }
            //Delete now empty folder
            dir.delete();
        }

        //Remove names from list
        dbListView.getItems().removeAll(list);
    }

    public void backBtnPressed() throws IOException {
        Scene scene = SetUp.getInstance().databaseSelectMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }


    @FXML
    void practiceButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) practiceButton.getScene().getWindow();
        window.setScene(scene);
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }
}
