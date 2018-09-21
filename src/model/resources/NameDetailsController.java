package model.resources;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.Collections;
import java.util.List;

public class NameDetailsController {

    //TODO: Maybe add a .wav so the user knows that they're audio wav files

    @FXML private Label nameName;
    @FXML private ListView<String> nameListView;
    @FXML private Button backBtn;
    @FXML private Button setDefaultBtn;

    @FXML private Button deleteBtn;
    private String dirName;

    public String getName() {
        return dirName;
    }


    //TODO: BUILD LIST WITHOUT UGLY PREFIXES
    //Builds list of audio files within 'name' folder
    public void setUpList(List<String> list, String name) {
        dirName = name;
        nameName.setText(dirName);
        nameListView.getItems().addAll(list);
        nameListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Collections.sort(nameListView.getItems());

        setDefaultBtn.setDisable(true);
        deleteBtn.setDisable(true);

        //Only can set default if there is just 1 name selected
        nameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(nameListView.getSelectionModel().getSelectedItems().size() == 1 && nameListView.getItems().size() != 1) {
                setDefaultBtn.setDisable(false);
                deleteBtn.setDisable(false);
            } else {
                setDefaultBtn.setDisable(true);
                deleteBtn.setDisable(false);
            }
        });
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


            //Switch scenes
            Scene scene = SetUp.getInstance().deleteMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    //Changes these commands to have backslash before so bash works
    public String bashify(String name) {
        //Characters that break the bash command TODO: test if these are all of them



        char invalids[] = "$/%:\\ .,-()".toCharArray();
        boolean found = false;
        String bashed = "";
        char[] chars = name.toCharArray();
        for(char cha : chars) {
            for(char invalid : invalids) {
                if(cha == invalid) {
                    bashed += "\\" + cha;
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
            if(!found) {
                bashed += cha;
            }
        }
        return bashed;
    }

    public void setDefaultClicked() throws IOException {
        String titleName = nameName.getText();
        String selectedName = nameListView.getSelectionModel().getSelectedItem();

        String databasePath = SetUp.getInstance().dbMenuController.getPathToDB();


        String startName = bashify(databasePath + "/" + titleName+ "/" + selectedName);
        String defaultName = bashify(databasePath + "/" + titleName+ "/" + titleName);
        String defaultNameTemp = bashify(databasePath + "/" + titleName+ "/" + titleName+"_t");


        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", "mv " + defaultName + " " + defaultNameTemp + "; mv " + startName + " " + defaultName + "; mv " + defaultNameTemp + " " + startName);
            Process renameTemp = builder.start();
            renameTemp.waitFor();

            nameListView.refresh();

        } catch (IOException e) {
            System.out.println("ERROR");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void clearListView() {
        //Clear list view
        nameListView.getItems().clear();
    }

    public void setDefaultBtnPressed() {

    }
}
