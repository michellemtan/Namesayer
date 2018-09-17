package model.resources;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.io.IOException;

public class DatabaseMenuController {

    @FXML private Label databaseName;
    @FXML private Button backBtn;
    @FXML private TreeView<String> dbTreeView;

    void initialise(String path) {
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
        dbTreeView.setShowRoot(false);
    }

    public void buildTree(String path, String name, TreeItem<String> parent) {
        //Iterate through audio files in named folder, rename them and make them a TreeItem
        File dir = new File(path);
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(int i=0; i<directoryListing.length; i++) {
                if(i==0) {
                    directoryListing[i].renameTo(new File(path + "/"  + name));
                    TreeItem<String> fileName = makeBranch(name, parent);
                } else {
                    directoryListing[i].renameTo(new File(path + "/"  + name + "(" + String.valueOf(i) + ")"));
                    TreeItem<String> fileName = makeBranch(name + "(" + String.valueOf(i) + ")", parent);
                }
            }
        }
    }

    public void backBtnPressed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        backBtn.getScene().setRoot(root);
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(false);
        parent.getChildren().add(item);
        return item;
    }
}
