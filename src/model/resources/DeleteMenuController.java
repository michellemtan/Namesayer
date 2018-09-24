package model.resources;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteMenuController {

    @FXML private Button backBtn;
    @FXML private ListView<String> deleteListView;
    @FXML private Label deleteLabel;
    @FXML
    private Label numberLabel;
    private List<String> toDelete;
    private String previousScenes;
    private int numberRecordings;
    private List<String> deletedNames;

    public void backBtnPressed() throws IOException {
        //Clear list view
        deleteListView.getItems().clear();

        //Change to previous scene
        if(previousScenes.equals("practiceDetails") || previousScenes.equals("dbDetails")) {
            Scene scene = SetUp.getInstance().nameDetailsMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
    }

    //This method sets up the database list view to contain the recordings selected to be deleted
    public void setUpList(List<String> list, String source) {
        previousScenes = source;

        //Populate the list view
        toDelete = list;
        deleteListView.getItems().addAll(list);

        //Consume event of selecting a name so as to make the names non-selectable
        deleteListView.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

        //Change labels if only one recording is to be deleted
        if (previousScenes.equals("practiceDetails") || previousScenes.equals("dbDetails")) {
            deleteLabel.setText("Delete Files?");
            numberLabel.setText("");
        } else if (list.size() == 1) {
            checkNumber(list);
            deleteLabel.setText("Delete Name?");
            if (numberRecordings == 1) {
                numberLabel.setText("There is one name containing one recording to be deleted:");
            } else {
                numberLabel.setText("There is one name containing " + numberRecordings + " recordings to be deleted:");
            }
            //If deleting from the database menu, the label will show an explanation of the number of names and the recordings contained within
        } else {
            checkNumber(list);
            deleteLabel.setText("Delete Names?");
            numberLabel.setText("There are " + deleteListView.getItems().size() + " names containing " + numberRecordings + " recordings to be deleted:");
        }
    }

    //This method checks how many files there are to be deleted
    private void checkNumber(List<String> creationslist) {

        //Get path to file directory
        String pathToDB = null;
        try {
            pathToDB = SetUp.getInstance().dbMenuController.getPathToDB();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        numberRecordings = 0;

        //Iterate through number of files
        for (String selectedName : creationslist) {
            File dir = new File(pathToDB + "/" + selectedName);
            File[] files = dir.listFiles();
            for (File file : files) {
                list.add(file.getName());
                numberRecordings++;
            }
        }
    }

    public void confirmBtnPressed() throws IOException {
        List<String> toBeDeleted;
        toBeDeleted = deleteListView.getItems();

        //Update the audio ratings file
        SetUp.getInstance().audioRatingsController.deleteName(toBeDeleted);

        //If user has come from details menu, call different delete method
        if(previousScenes.equals("practiceDetails")) {
            List<String> creation = new ArrayList<String>();
            creation.add(SetUp.getInstance().nameDetailsController.getName());

            //Check how many recordings there are for the selected name
            checkNumber(creation);

            SetUp.getInstance().nameDetailsController.clearListView();
            SetUp.getInstance().practiceMenuController.deleteAudioFiles(toDelete.get(0));
            //Switch scenes back

            //If all the recordings have been deleted, return the user to select more names
            if (numberRecordings == 1 && SetUp.getInstance().practiceMenuController.returnListSize() == 0) {
                //If the user has deleted the only recording contained in the details menu
                Scene scene = SetUp.getInstance().databaseMenu;
                Stage window = (Stage) backBtn.getScene().getWindow();
                window.setScene(scene);
            } else {
                //Change scene back to practice menu
                Scene scene = SetUp.getInstance().practiceMenu;
                Stage window = (Stage) backBtn.getScene().getWindow();
                window.setScene(scene);
            }
        } else if(previousScenes.equals("dbDetails")) {
            SetUp.getInstance().nameDetailsController.clearListView();
            SetUp.getInstance().practiceMenuController.deleteAudioFiles(toDelete.get(0));
            //Switch scenes back
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        } else {
            SetUp.getInstance().dbMenuController.deleteFiles(toDelete);
            //Switch scenes back
            Scene scene = SetUp.getInstance().databaseMenu;
            Stage window = (Stage) backBtn.getScene().getWindow();
            window.setScene(scene);
        }
        //Clear list view
        deleteListView.getItems().clear();
    }
}

