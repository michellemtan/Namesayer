package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class DatabaseMenuController {

    @FXML private Label databaseName; //TODO: figure out how to make this the dbName from MMC class
    @FXML private Button backBtn;
    @FXML private Button deleteBtn;
    @FXML private Button practiceButton;
    @FXML private TreeView<String> dbTreeView;

    //TODO: make folders only containing 1 item to be not expandable
    void initialize(String path) {
        databaseName.setText(path.substring(path.lastIndexOf("/") + 1));

        File dir = new File(path);
        File[] directoryListing = dir.listFiles();

        //Create root of tree
        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);

        //Add all names to tree
        if(directoryListing != null) {
            for(File file : directoryListing) {
                if(file.isDirectory() && !file.getName().equals("uncut_files")){
                    TreeItem<String> dirName = makeBranch(file.getName(), rootItem);
                    buildTree(file.getPath(), file.getName(), dirName);
                }
            }
        }

        //Make tree
        dbTreeView.setRoot(rootItem);
        dbTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dbTreeView.setShowRoot(false);
    }

    private void buildTree(String path, String name, TreeItem<String> parent) {
        //Iterate through audio files in named folder, rename them and make them a TreeItem
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(int i=0; i<directoryListing.length; i++) {
                if(i==0) {
                    TreeItem<String> fileName = makeBranch(name, parent);
                } else {
                    TreeItem<String> fileName = makeBranch(name + "(" + String.valueOf(i) + ")", parent);
                }
            }
        }
    }

    public void selectAllPressed() {
        dbTreeView.getSelectionModel().selectAll();
    }

    public void deleteBtnPressed() throws IOException {
        if(dbTreeView.getSelectionModel().getSelectedIndex() != -1) {
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void backBtnPressed() throws IOException {
        Scene scene = SetUp.getInstance().mainMenu;
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
