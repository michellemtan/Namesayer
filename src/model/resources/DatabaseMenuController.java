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

    @FXML private Label databaseName; //TODO: figure out how to make this the dbName from MMC class
    @FXML private Button backBtn;
    @FXML private TreeView<String> dbTreeView;

    void initialise(String name) {
        databaseName.setText(name.substring(name.lastIndexOf("/") + 1));

        File dir = new File(name);
        File[] directoryListing = dir.listFiles();

        //Create root of tree
        TreeItem<String> root = new TreeItem<>();
        root.setExpanded(true);

            if(directoryListing != null) {
            for(File file : directoryListing) {
                if(file.isDirectory() && !file.getName().equals("uncut_files")){
                    TreeItem<String> dirName = makeBranch(file.getName(), root);
                    System.out.println(file.getName());
                }
            }
        }

        //Make tree
        dbTreeView = new TreeView<>(root);
        dbTreeView.setShowRoot(true);
    }

    public void backBtnPressed() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        backBtn.getScene().setRoot(root);
    }

    private TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }
}
