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
    @FXML private TreeView<String> dbTreeView;
    private TreeItem<String> rootItem;

    //TODO: should jonothan and Jonothan be the same name!?
    void initialize(String path) {
        databaseName.setText(path.substring(path.lastIndexOf("/") + 1));

        File dir = new File(path);
        File[] directoryListing = dir.listFiles();

        //Create root of tree
        rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        List<String> names = new ArrayList<>();

        //Add all names to tree
        if(directoryListing != null) {
            for(File file : directoryListing) {
                if(file.isDirectory() && !file.getName().equals("uncut_files")){
                    names.add(file.getName());
                }
            }
        }

        //Sort list case insensitive
        names.sort(String.CASE_INSENSITIVE_ORDER);

        //Make tree items for each item in list and it's children & add to root
        for(String name : names){
            TreeItem<String> dirName = makeBranch(name, rootItem);
            buildChildren(path + "/" + name, name, dirName);
        }

        //Make tree
        dbTreeView.setRoot(rootItem);
        dbTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        dbTreeView.setShowRoot(false);
    }

    private void buildChildren(String path, String name, TreeItem<String> parent) {
        //Iterate through audio files in named folder, rename them and make them a TreeItem
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        //Build children for folders with more than 1 child
        if(directoryListing != null && directoryListing.length > 1) {
            for(int i=0; i<directoryListing.length; i++) {
                if(i!=0) {
                    TreeItem<String> fileName = makeBranch(name + "(" + String.valueOf(i) + ")", parent);
                } else {
                    TreeItem<String> originalName = makeBranch(name, parent);
                }

            }
        }
    }

    public void selectAllPressed() {
        dbTreeView.getSelectionModel().selectAll();
    }

    public void deleteBtnPressed() throws IOException {
        if(dbTreeView.getSelectionModel().getSelectedIndex() != -1) {

            //TODO: Selecting the folder name for an expanded folder doesn't add any of its children.
            //TODO: Look into directing with tool tip
            List<String> toDelete = new ArrayList<>(); //List of names to be deleted from current database
            //Add selected items to list to delete
            for(TreeItem<String> treeItem : dbTreeView.getSelectionModel().getSelectedItems()) {
                if(treeItem.getChildren().size() <= 1) {
                    toDelete.add(treeItem.getValue());
                } else if(treeItem.getParent() == rootItem && !treeItem.isExpanded()) {
                    for(TreeItem<String> child : treeItem.getChildren()) {
                        toDelete.add(child.getValue());
                    }
                }
            }
            //Pass list into deleteMenuController
            SetUp.getInstance().deleteMenuController.setUpList(toDelete);

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) deleteBtn.getScene().getWindow();
            window.setScene(scene);
        }
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
