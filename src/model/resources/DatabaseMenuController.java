package model.resources;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: Should we rename this class to creations list or something else?
public class DatabaseMenuController {

    @FXML private Label databaseName;
    @FXML private Button backBtn;
    @FXML private Button deleteBtn;
    @FXML private Button practiceButton;
    @FXML private ListView<String> dbListView;
    @FXML private Button createButton;
    @FXML private Button defaultButton;
    @FXML private Button selectAllButton;
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


    @FXML
    void selectAllButtonClicked(MouseEvent event) {
        dbListView.getSelectionModel().selectAll();
        deleteBtn.setDisable(false);
        practiceButton.setDisable(false);
    }

    public void deleteBtnPressed() throws IOException {
        if (dbListView.getSelectionModel().getSelectedIndex() != -1) {
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
        for (String name : list) {
            //Delete all files inside folder (.delete doesn't work on non-empty directories)
            File dir = new File(pathToDB + "/" + name);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
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

    //This method determines if the buttons are disabled or not, depending on the state of the tree list view
    public void checkButtonBehaviour() {
        if (dbListView.getSelectionModel().getSelectedItems().isEmpty()) {
            //If no item in the list is selected, the delete and continue buttons should be disabled
            deleteBtn.setDisable(true);
            defaultButton.setDisable(true);
            selectAllButton.setDisable(true);
            practiceButton.setDisable(true);
        } else if (dbListView.getSelectionModel().getSelectedItems().size() > 1) {
            defaultButton.setDisable(true);
        } else {
            //If the tree view list is not empty and a creation has been selected, allow the user to delete creations etc.
            deleteBtn.setDisable(false);
            defaultButton.setDisable(false);
            selectAllButton.setDisable(false);
            practiceButton.setDisable(false);
        }
    }

    //TODO: Add alert or disable create button?
    @FXML
    void createButtonClicked(MouseEvent event) {
        //To be implemented in assignment 4
    }

    @FXML
    void defaultButtonClicked(MouseEvent event) {

    }
}
