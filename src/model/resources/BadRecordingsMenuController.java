package model.resources;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashSet;

public class BadRecordingsMenuController {

    @FXML private Button backButton;
    @FXML private TextArea textArea;
    private String previousScene = "";

    //Return the user to the start menu when the back button is clicked
    @FXML
    void backButtonClicked() throws IOException {
        if(previousScene.equals("practiceMenu")) {
            Scene scene = SetUp.getInstance().practiceMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else if(previousScene.equals("nameDetailsMenu")) {
            Scene scene = SetUp.getInstance().nameDetailsMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        } else if(previousScene.equals("startMenu")) {
            Scene scene = SetUp.getInstance().startMenu;
            Stage window = (Stage) backButton.getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void setPreviousScene(String name) throws IOException {
        previousScene = name;
        updateTextLog();
    }

    //This method displays the contents of the text file containing a list of bad recordings

    public void updateTextLog() throws IOException {

        //Set storing unique values of creations
        HashSet<String> hs = new HashSet<String>();

            //Read in the file containing the list of bad quality recordings
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("BadRecordings.txt")))) {
            String line;
            StringBuilder fieldContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                //Concatenate each line of the file to the StringBuilder
                if(hs.add(line)) {
                    fieldContent.append(line + "\n");
                }
            }

            //Ensure the textArea is not editable by the user
            textArea.setText(fieldContent.toString());
            textArea.setEditable(false);
            textArea.setDisable(true);
            textArea.setMouseTransparent(true);

        } catch (IOException e) {
            //If there are no bad recordings saved, create a new text file to store them
            File f = new File("BadRecordings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.flush();
            bw.close();
        }
    }
}

