package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NameDetailsController {

    //TODO: Maybe add a .wav so the user knows that they're audio wav files

    @FXML private Label nameName;
    @FXML private ListView<String> nameListView;
    @FXML private Button backBtn;
    @FXML private Button setDefaultBtn;

    private String dirName;

    public void setName(String name) {
        dirName = name;
        nameName.setText(name);
    }

    public String getName() {
        return dirName;
    }


    //TODO: BUILD LIST WITHOUT UGLY PREFIXES
    //Builds list of audio files within 'name' folder
    public void setUpList(List<String> list, String name) {
        dirName = name;
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if(nameListView.getItems().size() == 1) {
            setDefaultBtn.setDisable(true);
        } else {
            setDefaultBtn.setDisable(false);
        }
    }

    public void backBtnPressed() throws IOException {
        //Clear list view
        nameListView.getItems().clear();

        Scene scene = SetUp.getInstance().practiceMenu;
        Stage window = (Stage) backBtn.getScene().getWindow();
        window.setScene(scene);
    }

    //TODO: MAKE IT SO DELETE BUTTON GOES BACK TO PRACTICE, NOT MAIN MENU
    public void deleteBtnPressed() throws IOException {
        if(nameListView.getSelectionModel().getSelectedIndex() != -1) {
            List<String> toDelete = new ArrayList<>(nameListView.getSelectionModel().getSelectedItems());
            //Pass list of files to delete through to delete menu
            SetUp.getInstance().deleteMenuController.setUpList(toDelete, true);

            //Clear list view
            nameListView.getItems().clear();

            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void setDefaultClicked() throws IOException {
        String titleName = nameName.getText();
        String selectedName = nameListView.getSelectionModel().getSelectedItem();

        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();

        String startName = databasePath + "/" + titleName+ "/" + selectedName;
        String defaultName = databasePath + "/" + titleName+ "/" + titleName;
        String defaultNameTemp = databasePath + "/" + titleName+ "/" + titleName+"_t";

        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "mv " + defaultName + " " + defaultNameTemp);
            Process renameTemp = builder.start();
            renameTemp.waitFor();

            ProcessBuilder builder1 = new ProcessBuilder("/bin/bash", "-c", "mv " + startName + " " + defaultName);
            Process renameToDefault = builder1.start();
            renameToDefault.waitFor();

            ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", "mv " + defaultNameTemp + " " + startName);
            Process renameToTemp = builder2.start();
            renameTemp.waitFor();


            //TODO: UPDATE LIST
            nameListView.refresh();

        } catch (IOException e) {
            System.out.println("ERROR");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
