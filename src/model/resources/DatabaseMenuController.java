package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: Should we rename this class to creations list or something else? That sounds like a good idea
public class DatabaseMenuController {

    @FXML private Label databaseName;
    @FXML private Button backBtn;
    @FXML private Button deleteBtn;
    @FXML private Button practiceButton;
    @FXML private ListView<String> dbListView;
    @FXML private Button createButton;
    @FXML private Button selectAllButton;
    private String pathToDB;

    public ListView<String> getDbListView() {
        return dbListView;
    }

    public String getPathToDB() {
        return pathToDB;
    }

    public void addItem(String name) {
        dbListView.getItems().add(name);
        dbListView.getItems().sort(String.CASE_INSENSITIVE_ORDER);
    }

    //TODO: refactor controllers and fxml files into different packages
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
            }
        }

        /*//Set up cell factory for right-click > details specific to each name
        dbListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind((Bindings.format("Details... ")));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                //Switch scenes
                Scene scene = null;
                try {
                    scene = SetUp.getInstance().nameDetailsMenu;
                    SetUp.getInstance().nameDetailsController.setName(cell.itemProperty().get());
                    SetUp.getInstance().nameDetailsController.setUpList(getChildrenFromParent(cell.itemProperty().get()), cell.itemProperty().get());
                } catch (IOException e) {
                }
                Stage window = (Stage) deleteBtn.getScene().getWindow();
                window.setScene(scene);

            });
            contextMenu.getItems().add(editItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });*/

        //Sort name and add to list view
        dbListView.getItems().addAll(names);
        dbListView.getItems().sort(String.CASE_INSENSITIVE_ORDER);
        dbListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private List<String> getChildrenFromParent(String name) {
        //Create list
        List<String> children = new ArrayList<>();
        File dir = new File(pathToDB + "/" + name);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(File file : directoryListing) {
                children.add(file.getName());
            }
        }
        children.sort(String.CASE_INSENSITIVE_ORDER);
        return children;
    }

    public void selectAllButtonClicked() {
        dbListView.getSelectionModel().selectAll();
        deleteBtn.setDisable(false);
        practiceButton.setDisable(false);
    }

    public void deleteBtnPressed() throws IOException {
        if (dbListView.getSelectionModel().getSelectedIndex() != -1) {
            //TODO: Look into directing with tool tip
            List<String> toDelete = new ArrayList<>(dbListView.getSelectionModel().getSelectedItems());
            //Pass list into deleteMenuController
            SetUp.getInstance().deleteMenuController.setUpList(toDelete, false);

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    //Method to delete files if coming from this menu
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

    //Method to delete files if coming from the NameDetails menu
    public void deleteAudioFiles(List<String> list) throws IOException {
        String dirName = SetUp.getInstance().nameDetailsController.getName();
        File dir = new File(pathToDB + "/" + dirName);
        for(String name : list) {
            File file = new File(pathToDB + "/" + dirName + "/" + name);
            file.delete();

        }
        //Try to delete directory, will only work if non-empty - correct behaviour
        if(dir.delete()) {
            //Remove name from list if directory now empty
            dbListView.getItems().remove(dirName);
        }
    }

    public void backBtnPressed() throws IOException {
        Scene scene = SetUp.getInstance().databaseSelectMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    public void practiceButtonClicked() throws IOException {
        //Only change scenes if they've actually selected something
        if(dbListView.getSelectionModel().getSelectedItems().size() > 0) {
            //Populate list in next scene with selected items
            List<String> toDelete = new ArrayList<>(dbListView.getSelectionModel().getSelectedItems());
            SetUp.getInstance().practiceMenuController.setUpList(toDelete);


            Scene scene = SetUp.getInstance().practiceMenu;
            Stage window = (Stage) practiceButton.getScene().getWindow();
            window.setScene(scene);
        }
    }

    //This method determines if the buttons are disabled or not, depending on the state of the tree list view
    public void checkButtonBehaviour() {
        if (dbListView.getSelectionModel().getSelectedItems().isEmpty()) {
            //If no item in the list is selected, the delete and continue buttons should be disabled
            deleteBtn.setDisable(true);
            selectAllButton.setDisable(true);
            practiceButton.setDisable(true);
        } else {
            //If the tree view list is not empty and a creation has been selected, allow the user to delete creations etc.
            deleteBtn.setDisable(false);
            selectAllButton.setDisable(false);
            practiceButton.setDisable(false);
        }
    }

    //TODO: if 1 item selected and create is pressed auto-fill text field in next window
    public void createButtonClicked() throws IOException {
        Scene scene = SetUp.getInstance().createMenu;
        Stage window = (Stage) createButton.getScene().getWindow();
        window.setScene(scene);
    }
}
